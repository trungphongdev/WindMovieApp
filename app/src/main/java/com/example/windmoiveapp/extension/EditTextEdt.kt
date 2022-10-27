package com.example.windmoiveapp.extension

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.example.windmoiveapp.R
import org.checkerframework.checker.units.qual.s

const val TIME_SEARCH_DEFAULT  = 500L
fun EditText.onSearch(onSearch: (String) -> Unit) {
    val handler = Handler(Looper.getMainLooper())
    val runnable = Runnable {
        onSearch(this.text.toString().trim())
    }
    this.watchText {
        handler.removeCallbacks(runnable)
        if (it.isNotBlank()) {
            handler.postDelayed(runnable, TIME_SEARCH_DEFAULT)
        }
    }
}


fun EditText.watchText(onTextChanged: (CharSequence) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            onTextChanged.invoke(s)
        }

        override fun afterTextChanged(editable: Editable?) {}
    })
}
fun EditText.afterTextChange(onTextChanged: (CharSequence) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }
        override fun afterTextChanged(editable: Editable?) {
            onTextChanged.invoke(editable.toString())
        }
    })
}

fun EditText.isValidEmail() =
    this.text.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()

fun EditText.hideKeyBoardOnTouch(context: Context) {
    this.setOnFocusChangeListener { view, focus ->
        if (!focus) {
            val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        } else run {
            this.post { this.setSelection(this.text.length) }
        }
    }
}

const val PASSWORD_CHAR_STYLE_DOT: Char = '.'
fun EditText.showPass(show: Boolean = true, hideCharStyle: Char? = PASSWORD_CHAR_STYLE_DOT) {
    val currentSelection = selectionStart to selectionEnd
/*    transformationMethod = when {
        show -> HideReturnsTransformationMethod.getInstance()
        hideCharStyle == null -> PasswordTransformationMethod.getInstance()
        else -> CustomPasswordTransformationMethod(hideCharStyle)
    }*/
    setSelection(currentSelection.first, currentSelection.second)
}




