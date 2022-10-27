package com.example.windmoiveapp.extension

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.fragment.CommonDialogFragment

const val DIALOG_TAG = "DIALOG"
fun showCommonDialogFragment(
    fm: FragmentManager, tag: String = DIALOG_TAG,
    title: String? = null, message: String,
    isCancelable: Boolean = true, @StringRes cancelBtnId: Int? = null,
    onCancel: (() -> Unit)? = null, onDismiss: (() -> Unit)? = null,
    isDismissOnOK: Boolean = true,
    @StringRes okBtnId: Int = R.string.okeLabel, onOk: (() -> Unit)? = null
) {
    if (fm.findFragmentByTag(tag) != null) return
    CommonDialogFragment(
        title, message, cancelBtnId, onCancel, isCancelable, onDismiss, isDismissOnOK, okBtnId,
        onOk
    ).show(fm, tag)
}


/*
fun Context.showDialogCommonTwoOption(
    title: String,
    content: String,
    stringNegative: String,
    stringPositive: String,
    textColor: Int? = null,
    callBackNegative: (() -> Unit)? = null,
    callBackPositive: (() -> Unit)? = null
) {
    val dialog = Dialog(this)
    val binding = DialogConfirmDataBinding.inflate((this as Activity).layoutInflater)
    binding.apply {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        textColor?.let {
            tvMessage.setTextColor(ContextCompat.getColor(root.context, textColor))
        }
        tvTitleDialog.visibility = if(title.isEmpty()) View.GONE  else View.VISIBLE
        tvTitleDialog.text = title

        tvMessage.text = content
        tvMessage.visibility = if(content.isEmpty()) View.GONE  else View.VISIBLE

        btnNegative.text = stringNegative
        btnPositive.text = stringPositive
        btnNegative.setOnClickListener {
            dialog.dismiss()
            callBackNegative?.invoke()
        }
        btnPositive.setOnClickListener {
            dialog.dismiss()
            callBackPositive?.invoke()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(root)
    }
    dialog.show()
}*/
