package com.example.windmoiveapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.ActivityMainBinding
import com.example.windmoiveapp.extension.getAlertDialog
import com.example.windmoiveapp.extension.showAlertDialog
import com.example.windmoiveapp.ui.fragment.ProgressDialogFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
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


    override fun onBackPressed() {
        showDialogBackPress()
    }

    private fun getCurrentFragment(): Fragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    private fun showDialogBackPress() {
        showAlertDialog(mess = getString(R.string.textExitAppContent), isShowNegativeBtn = true) {
            super.onBackPressed()
        }
    }
}