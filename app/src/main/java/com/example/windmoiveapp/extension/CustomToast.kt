package com.example.windmoiveapp.extension

import android.app.Activity
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.windmoiveapp.R


fun Toast.showCustomToast(message: String, activity: Activity, isShowToastError: Boolean = false) {
    val layout = activity.layoutInflater.inflate(
        R.layout.layout_toast_screen,
        activity.findViewById(R.id.toastContainer) as? (ViewGroup)
    )

    // set the text of the TextView of the message
    val textView = layout.findViewById<TextView>(R.id.tvToastText)
    textView.text = message
    if (isShowToastError) {
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fail_round, 0, 0, 0)
    }


    // use the application extension function
    this.apply {
        setGravity(Gravity.BOTTOM, 0, 40)
        duration = Toast.LENGTH_LONG
        view = layout
        show()
    }
}

fun Activity?.showCustomToast(message: String, isShowToastError: Boolean = false) =
    this?.let { Toast(this).showCustomToast(message, this, isShowToastError) }