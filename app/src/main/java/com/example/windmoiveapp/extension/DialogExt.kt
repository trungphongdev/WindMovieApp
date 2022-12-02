package com.example.windmoiveapp.extension

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.StringRes
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.DialogCmtBinding
import com.example.windmoiveapp.ui.fragment.CommonDialogFragment
import timber.log.Timber

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

class ProgressDialogApiRequest {
    companion object {
        private var countApi = 0
        private var singleTonDialog: Dialog? = null

        fun showProgressDialog(context: Context) {
            countApi++
            singleTonDialog ?: Dialog(context).apply {
                setContentView(R.layout.fragment_progress_dialog)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                setCancelable(false)
                singleTonDialog = this
            }
            if (singleTonDialog?.isShowing == true) return
            (context as? Activity)?.apply {
                runOnUiThread {
                    singleTonDialog?.show()
                }
            }
        }

        fun dismissProgressDialog() {
            countApi--
            if (countApi <= 0) {
                if (singleTonDialog?.isShowing == true) {
                    try {
                        singleTonDialog?.dismiss()
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }
                singleTonDialog = null
            }
        }
    }
}

fun Context.showDialogComment(
    title: String? = null,
    position: Int? = null,
    callBack: ((Int) -> Unit)? = null
) {
    val dialog = Dialog(this)
    val binding = DialogCmtBinding.inflate((this as Activity).layoutInflater)
    binding.apply {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(false)
        title.let {
            tvTitleDialog.text = it
        }
        position?.let {
            (rdgCmt.getChildAt(it) as? RadioButton)?.isChecked = true
        }
        //stringAction?.let { tvAction.text = it }
        rdgCmt.setOnCheckedChangeListener { _, checkedId ->
            val view = rdgCmt.findViewById<View>(checkedId)
            val index = rdgCmt.indexOfChild(view)
            callBack?.invoke(index)
            dialog.dismiss()
        }
        dialog.setContentView(root)
    }
    dialog.show()
}


