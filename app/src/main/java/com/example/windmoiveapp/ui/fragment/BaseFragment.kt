package com.example.windmoiveapp.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.windmoiveapp.R
import com.example.windmoiveapp.extension.ProgressDialogApiRequest
import com.example.windmoiveapp.extension.hideKeyboard
import com.example.windmoiveapp.model.BaseResource
import com.example.windmoiveapp.model.ErrorMessage
import com.example.windmoiveapp.model.Status
import com.example.windmoiveapp.network.NetWork.isNetWorkAvailable
import com.example.windmoiveapp.ui.MainActivity
import timber.log.Timber

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private val TAG by lazy { this::class.java.name }

    private var _binding: VB? = null

    protected val binding get() = _binding!!

    private var isViewCreated: Boolean = false

    private var isReloadData = true

    protected var mainActivity: MainActivity? = null

    protected var listPermission: ArrayList<String> = arrayListOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

/*    @get: LayoutRes
    abstract val layoutId: Int*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.tag(TAG).d("onAttach: ")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG).d("onAttach: ")
        if (activity is MainActivity) {
            mainActivity = activity as MainActivity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = onCreateViewBinding(inflater, container, savedInstanceState)
        }
        return binding.root
    }

    abstract fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): VB

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewInitialized(view, savedInstanceState, isViewCreated)
        isViewCreated = true
    }

    abstract fun onViewInitialized(view: View, savedInstanceState: Bundle?, isViewCreated: Boolean)

    fun Activity.restartApp() {
        // (this as? MainActivity)?.resetSession()
        val newIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        this.startActivity(newIntent)
        finish()
    }

/*    protected fun showProgressDialog() {
        activity?.
    }*/

    override fun onStart() {
        super.onStart()
        Timber.tag(TAG).d("onStart: ")
    }

    override fun onResume() {
        super.onResume()
        context?.let {
            if (it.isNetWorkAvailable().not()) {
                mainActivity?.showNoInternetDialog()
                return
            }
        }
        if (isReloadData.not()) return
        isReloadData = false
        loadData()
        Timber.tag(TAG).d("onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Timber.tag(TAG).d("onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Timber.tag(TAG).d("onStop: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag(TAG).d("onDestroyView: ")
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        Timber.tag(TAG).d("onDetach: ")
    }

    fun onClearViewModelInScopeActivity() {
        activity?.viewModelStore?.clear()
    }

    open fun loadData() {

    }

    protected fun showProgress() {
        (activity as? MainActivity)?.showProgress()
    }

    protected fun dismissProgress(throwable: Throwable? = null) {
        (activity as? MainActivity)?.dismissProgress()
    }

    override fun startActivity(intent: Intent?) {
        mainActivity?.startActivity(intent)
    }

    protected fun onBackFragment() {
        Timber.tag(TAG).d("onBackFragment: ")
        findNavController().popBackStack()
    }

    protected fun navigateToDestination(destination: Int, bundle: Bundle? = null) {
        activity?.let {
            Timber.tag(TAG).d("navigateToDestination: ")
            bundle?.let {
                findNavController().navigate(destination, it)
            } ?: findNavController().navigate(destination)
        }
    }

    open fun hideKeyBoard() = activity.hideKeyboard()

/*    protected fun moveToDashBoard() {
        if (!this.isResumed) return
        context?.let { ct ->
            Navigation.findNavController(
                ct as Activity,
                R.id.nav_host_fragment
            ).setGraph(R.navigation.nav_graph_dash_board)
        }
    }*/

    private fun hasPermissions(vararg permissions: String): Boolean =
        permissions.all { permission ->
            activity?.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }

    private fun getListPermissionDenied(): Array<String> {
        return listPermission.filter { activity?.checkSelfPermission(it) == PackageManager.PERMISSION_DENIED }.toTypedArray()
    }

    protected fun requestMultiPermission(): Boolean {
        return hasPermissions(*listPermission.toTypedArray())
    }

    fun <T> BaseResource<T>.handleResult(
        onSuccess: ((data: T?, message: String?) -> Unit)? = null,
        onHandleError: (ErrorMessage?) -> Boolean = { false },
        onRetry: (() -> Unit)? = null
    ) {
        when (this.status) {
             Status.SUCCESS -> {
                 dismissProgressRequest()
                 onSuccess?.invoke(this.data, this.message)
            }
            Status.ERROR -> {
                dismissProgressRequest()
                onSuccess?.invoke(this.data, this.message)
            }
            Status.LOADING -> {
                showProgressRequest()
                showErrorDialog(errorMessage = this.errorMessage, onRetry = onRetry, onHandleError = onHandleError)
            }
        }
    }
    private fun showProgressRequest() {
        context?.let {
            ProgressDialogApiRequest.showProgressDialog(it)
        }
    }

    private fun dismissProgressRequest() {
      // delay(500) {
            ProgressDialogApiRequest.dismissProgressDialog()
       // }
    }

    private fun showErrorDialog(
        errorMessage: ErrorMessage?,
        onRetry: (() -> Unit)? = null,
        onHandleError: ((ErrorMessage?) -> Boolean) = { false }
    ) {
/*        if (onHandleError(errorMessage)
            && (errorMessage?.code != HttpStatusCode.UNAUTHENTICATED)
        ) return
        val fm = activity?.supportFragmentManager ?: return

        val message = when (errorMessage?.code) {
            ErrorCode.NETWORK_NOT_AVAILABLE -> {
                getString(R.string.noInternetMess)
            }
            ErrorCode.SERVER_EXCEPTION -> {
                errorMessage.message ?: getString(R.string.wrongConnectToServer)
            }
            HttpStatusCode.UNAUTHENTICATED -> {
                getString(R.string.loginSessionHasExpiredLabel)
            }
            else -> {
                errorMessage?.message ?: getString(R.string.wrongLabel)
            }
        }

        if (errorMessage?.code == SESSION_EXPIRED_CODE
            || errorMessage?.code == HttpStatusCode.UNAUTHENTICATED
        ) {
            if (isExpied.not()) {
                isExpied = true
                context?.showAlertDialog(message, isCancelable = false) {
                    isExpied = false
                    (context as Activity).restartApp()
                }
            }
        } else if (onRetry != null) {
            showRetryDialog(fm, message = message, onRetry)
        } else {
            showCommonDialogFragment(fm, message = message)
        }*/
    }

    protected fun moveToDashBoard() {
        if (!this.isResumed) return
        context?.let { ct ->
            Navigation.findNavController(
                ct as Activity,
                R.id.navHostFragment
            ).setGraph(R.navigation.navigation)
        }
    }

}