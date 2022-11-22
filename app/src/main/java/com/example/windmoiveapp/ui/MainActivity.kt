package com.example.windmoiveapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.windmoiveapp.R
import com.example.windmoiveapp.database.BuildDaoDatabase
import com.example.windmoiveapp.databinding.ActivityMainBinding
import com.example.windmoiveapp.extension.getAlertDialog
import com.example.windmoiveapp.extension.showAlertDialog
import com.example.windmoiveapp.model.NotificationModel
import com.example.windmoiveapp.model.convertToNotificationModel
import com.example.windmoiveapp.ui.fragment.BaseFragment
import com.example.windmoiveapp.ui.fragment.ProgressDialogFragment
import com.example.windmoiveapp.util.AppApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    var mOnBackPressListener: (() -> Unit)? = null
    private val noInternetDialog by lazy {
        getAlertDialog(getString(R.string.noInternetMess))
    }
    private val progressDialog: ProgressDialogFragment by lazy {
        ProgressDialogFragment()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showNoInternetDialog() {
        if (!noInternetDialog.isShowing) {
            noInternetDialog.show()
        }
    }

    fun showProgress() {
        if (!progressDialog.isAdded && !progressDialog.isVisible) {
            progressDialog.show(supportFragmentManager)
        }
    }

    fun dismissProgress() {
        runOnUiThread {
            progressDialog.dismiss()
        }
    }

    override fun startActivity(intent: Intent?) {
        intent?.resolveActivity(packageManager)?.let {
            super.startActivity(intent)
        }
    }


/*    override fun onBackPressed() {
        showDialogBackPress()
    }*/

    private fun getCurrentFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

     fun showDialogBackPress() {
         showAlertDialog(mess = getString(R.string.textExitAppContent), isShowNegativeBtn = true) {
             super.onBackPressed()
             this.finish()
         }
     }

    override fun onBackPressed() {
        if (isDisableBackButton()) return
        if (mOnBackPressListener == null) {
            //old logic
            //handleOldBackPressed()
        } else {
            mOnBackPressListener?.invoke()
        }
    }

    private fun isDisableBackButton(): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as? NavHostFragment?

        navHostFragment?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->

                if (fragment is BaseFragment<*> && fragment.disableBackPressed()) {
                    return true
                }
            }
        }
        return false
    }

/*    private fun handleOldBackPressed() {
        val currentFrg = getCurrentFragment()

        if (currentFrg is PoemsTokenScreen
            || currentFrg is PoemsDigitalTokenIntroScreen
            || currentFrg is SetupProcessTokenScreen
        ) {
            if (mIsFromSettingScreen) {
                super.onBackPressed()
            } else {
                Navigation.findNavController(this, R.id.nav_host_fragment)
                    .setGraph(R.navigation.nav_graph_home)
            }
        } else if (currentFrg is DashBoardScreen
            || currentFrg is StartPageScreen
        ) {
            showDialogBackPress()
        } else {
            (currentFrg as? ResetPasswordScreen)?.let {
                it.backToLoginScreen()
                return
            }

            (currentFrg as? AuthOtpScreen)?.let {
                val isBackToLoginScreen = it.checkBackToLoginScreen()
                if (!isBackToLoginScreen) {
                    super.onBackPressed()
                }
                return
            }

            (currentFrg as? StartAccountSetupDetailScreen)?.let {
                it.handleBackPressed()
                return
            }

            (currentFrg as? ResetPwCodeScreen)?.let {
                it.backToLoginScreen()
                return
            }
            val isBackPressedEnable =
                KeyBackPressUtils.onBackPressed?.setOnBackPressedListener() ?: true
            if (isBackPressedEnable) {
                super.onBackPressed()
            }
        }
    }*/
}