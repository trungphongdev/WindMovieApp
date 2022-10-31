package com.example.windmoiveapp.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.windmoiveapp.ui.MainActivity
import com.example.windmoiveapp.extension.hideKeyboard
import timber.log.Timber

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private val TAG by lazy { this::class.java.name }

    private var _binding: VB? = null

    protected val binding get() = _binding!!

    private var isViewCreated: Boolean = false

/*    @get: LayoutRes
    abstract val layoutId: Int*/

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.tag(TAG).d("onAttach: ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(TAG).d("onAttach: ")
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

    override fun onStart() {
        super.onStart()
        Timber.tag(TAG).d("onStart: ")
    }

    override fun onResume() {
        super.onResume()
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

}