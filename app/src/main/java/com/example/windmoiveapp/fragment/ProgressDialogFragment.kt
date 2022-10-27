package com.example.windmoiveapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.windmoiveapp.databinding.FragmentProgressDialogBinding

const val DIALOG_TAG_PROGRESS = "DIALOG_TAG_PROGRESS"
class ProgressDialogFragment : DialogFragment() {
    private var binding: FragmentProgressDialogBinding? = null
    private var isShown: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setupDialog()
        binding = FragmentProgressDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun setupDialog() {
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        isCancelable = false
    }

    fun show(manager: FragmentManager) {
        if (isShown) return
        isShown = true
        if (manager.findFragmentByTag(DIALOG_TAG_PROGRESS) != null) return
        show(manager, DIALOG_TAG_PROGRESS)
    }

    override fun dismiss() {
        if (activity?.supportFragmentManager?.isStateSaved == true) return
        isShown = false
        super.dismiss()
    }
}
