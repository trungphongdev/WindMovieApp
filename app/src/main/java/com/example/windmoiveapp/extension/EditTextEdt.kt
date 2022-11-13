package com.example.windmoiveapp.extension

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.example.windmoiveapp.util.PASSWORD_CHAR_STYLE_DOT
import com.example.windmoiveapp.util.PASSWORD_HIDE_7_CHAR
import java.util.regex.Pattern

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

fun EditText.isValidString() =
    this.text.isNotEmpty() && Pattern.compile("[a-zA-Z0-9]+").matcher(this.text.toString())
        .matches()

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

fun EditText.showPass(show: Boolean = true, hideCharStyle: Char? = PASSWORD_CHAR_STYLE_DOT) {
    val currentSelection = selectionStart to selectionEnd
    transformationMethod = when {
        show -> HideReturnsTransformationMethod.getInstance()
        hideCharStyle == null -> PasswordTransformationMethod.getInstance()
        else -> CustomPasswordTransformationMethod(hideCharStyle)
    }
    setSelection(currentSelection.first, currentSelection.second)
}

const val PASSWORD_HIDE = "00000"
fun TextView.showPass(show: Boolean = true, data: String? = null, applyColor: Boolean? = false) {
    this.changePassMode(show)
    if (show) {
        this.text = if (!data.isNullOrEmpty()) data else "0"
    } else this.text = PASSWORD_HIDE
    /*if (applyColor == true) {
        this.setColorText(data)
    }
    if (!show) {
        setTextColor(ContextCompat.getColor(context, R.color.colorRegular))
    }*/
}

fun TextView.showPass7Char(
    show: Boolean = true, data: String? = null,
    applyColor: Boolean? = false, isBankAccNo: Boolean = false
) {
    this.changePassMode(show)
    if (show) {
        this.text = if (!data.isNullOrEmpty()) data else if (isBankAccNo) "-" else "0"
    } else this.text = PASSWORD_HIDE_7_CHAR

}

fun TextView.changePassMode(show: Boolean = true, hideCharStyle: Char? = PASSWORD_CHAR_STYLE_DOT) {
    transformationMethod = when {
        show -> HideReturnsTransformationMethod.getInstance()
        hideCharStyle == null -> PasswordTransformationMethod.getInstance()
        else -> CustomPasswordTransformationMethod(hideCharStyle)
    }
}
class CustomPasswordTransformationMethod(private val charStyle: Char = PASSWORD_CHAR_STYLE_DOT) :
    PasswordTransformationMethod() {

    override fun getTransformation(source: CharSequence, view: View?): CharSequence {
        return PasswordCharSequence(source, charStyle)
    }
}

class PasswordCharSequence(
    private val charSequence: CharSequence,
    private val charStyle: Char
) : CharSequence {

    override val length: Int
        get() = charSequence.length

    override fun get(index: Int): Char {
        return charStyle
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return charSequence.subSequence(startIndex, endIndex)
    }
}

