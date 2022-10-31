package com.example.windmoiveapp.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.DialogAlertBinding

class CommonDialogFragment(
    private val title: String? = null,
    private val message: String,
    @StringRes private val cancelBtnId: Int? = null,
    private val onCancel: (() -> Unit)? = null,
    private val isCancelableZ: Boolean = true,
    // onDismiss = Both click ok, click cancel, touch outside to cancel
    private val onDismiss: (() -> Unit)? = null,
    private val isDismissOnOK: Boolean = true,
    @StringRes private val okBtnId: Int = R.string.okeLabel,
    private val onOk: (() -> Unit)? = null,
) : DialogFragment() {


    private var binding: DialogAlertBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setupDialog()
        binding = DialogAlertBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupDialog() {
        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
        }
        isCancelable = isCancelableZ
    }

    private fun setupView() {
        binding?.apply {
/*            tvTitleAlert.isVisible = title.isNullOrBlank().not()
            tvTitleAlert.text = title
            tvMessageAlert.text = message

            btnCancelDialog.isVisible = cancelBtnId != null
            cancelBtnId?.let { btnCancelDialog.setText(it) }
            btnCancelDialog.setOnClickListener {
                dismiss()
                onCancel?.invoke()
            }

            btnOkDialog.setText(okBtnId)
            btnOkDialog.setOnClickListener {
                if (isDismissOnOK) dismiss()
                onOk?.invoke()
            }*/
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction()
            .add(this, tag)
            .commitAllowingStateLoss()
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

}

