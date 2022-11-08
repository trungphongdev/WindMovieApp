package com.example.windmoiveapp.ui.fragment

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.FragmentHomeBinding
import com.example.windmoiveapp.util.PERMISSION_REQUEST_CODE


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {

    }

    private fun requestPermission() {
        if (requestMultiPermission()) {
            activity?.requestPermissions(listPermission.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            when (requestCode) {
                PERMISSION_REQUEST_CODE -> {
                    Toast.makeText(context ?: return, getString(R.string.permissionGrantedLabel), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context ?: return, getString(R.string.permissionDeniedLabel), Toast.LENGTH_SHORT).show()
        }
    }
}
