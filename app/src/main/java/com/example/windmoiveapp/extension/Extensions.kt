package com.example.windmoiveapp.extension

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.example.windmoiveapp.R
import com.example.windmoiveapp.databinding.DialogAlertBinding
import com.example.windmoiveapp.ui.MainActivity
import com.google.android.material.tabs.TabLayout
import org.json.JSONObject
import java.math.BigDecimal
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/*fun TextView.setIsSelected(isIsSelected: Boolean) {
    val color = ContextCompat.getColor(
        this.context,
        if (isIsSelected) R.color.purple_200 else R.color.teal_200
    )
    this.setTextColor(color)
    val img = ContextCompat.getDrawable(this.context, R.drawable.under_line_tab_button)
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        null,
        if (isIsSelected) img else null
    )
}*/


fun String.highlightText(
    highLightText: String,
    @ColorInt colorZ: Int,
): SpannableString {
    val currentText = this
    val index = currentText.lowercase(Locale.ROOT).indexOf(highLightText.lowercase(Locale.ROOT))
    if (index < 0) return SpannableString(currentText)
    val backgroundColorSpan = ForegroundColorSpan(colorZ)

    val wordSpan = SpannableString(currentText)
    wordSpan.setSpan(
        backgroundColorSpan,
        index,
        index + highLightText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return wordSpan
}


fun TextView.highlightText(highLightText: String) {
    val currentText = this.text
    val index = currentText.indexOf(highLightText)
    if (index < 0) return
    val color = context?.let { ContextCompat.getColor(it, R.color.teal_700) }
    val backgroundColorSpan = color?.let { ForegroundColorSpan(it) }

    val wordSpan = SpannableString(currentText)
    wordSpan.setSpan(
        backgroundColorSpan,
        index,
        index + highLightText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    this.text = wordSpan
}

fun TextView.underlineText(underLineText: String) {
    val currentText = this.text
    val index = currentText.indexOf(underLineText)
    if (index < 0) return
    val wordSpan = SpannableString(currentText)
    wordSpan.setSpan(UnderlineSpan(), index, index + underLineText.length, 0)
    this.text = wordSpan
}

fun TextView.highLightTypeFaceText(highLightText: String) {
    val currentText = this.text
    val index = currentText.indexOf(highLightText)
    if (index < 0) return
    val wordSpan = SpannableString(currentText)
    wordSpan.setSpan(
        TypefaceSpan("din_pro_bold.otf"),
        index,
        index + highLightText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
    )
    this.text = wordSpan
}

/*fun TextView.setDrawableToText(text: String?, drawableID: Int) {
    this.text = SpannableStringBuilder()
        .append(text)
        .appendThreeSpace()
        .appendIcon(context, drawableID, context.resources.getDimensionPixelSize(R.dimen.dp8))
}*/

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun EditText.drawableRightClick(callback: () -> Unit) {
    this.setOnTouchListener(View.OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= v.right - this.compoundDrawables[2].bounds.width()
            ) {
                callback()
                return@OnTouchListener false
            }
        }
        false
    })
}

/*fun Context.getDeviceID() =
    EncryptUtil.decryptData(this, EncryptUtil.DEVICE_ID)*/

fun Context?.isPortTraitScreen() =
    this?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT

fun Activity?.rotateScreen() {
    val orientationSwitcherFlag = if (this.isPortTraitScreen())
        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    this?.requestedOrientation = orientationSwitcherFlag
}

fun Activity.setPortTraitOrientation() {
    this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}

fun EditText.validate(message: String): Boolean {
    val error = if (this.text.isNullOrEmpty()) {
        message
    } else {
        null
    }
    this.error = error
    return this.error == null
}

fun NavController.navigateWithAnim(
    idDestination: Int,
    bundle: Bundle? = null,
    popupToId: Int? = null
) {
    val anim = navOptions {
        anim {
            popEnter = R.anim.slide_in_left
            popExit = R.anim.slide_out_right
            enter = R.anim.slide_in_right
            exit = R.anim.slide_out_left
        }
        popupToId?.let {
            popUpTo(popupToId) {
                inclusive = true
            }
        }
    }
    this.navigate(idDestination, bundle, anim)
}

fun NavController.navigateBottomWithAnim(
    idDestination: Int,
    bundle: Bundle? = null,
    popupToId: Int? = null
) {
    val anim = navOptions {
        anim {
            popEnter = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
            popExit = androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
            enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
            exit = androidx.navigation.ui.R.anim.nav_default_enter_anim
        }
        popupToId?.let {
            popUpTo(popupToId) {
                inclusive = true
            }
        }
    }
    this.navigate(idDestination, bundle, anim)
}

fun NavController.navigateBackWithAnim(
    idDestination: Int,
    bundle: Bundle? = null,
    popupToId: Int? = null
) {
    val anim = navOptions {
        anim {
            popEnter = R.anim.slide_in_right
            popExit = R.anim.slide_out_left
            enter = R.anim.slide_in_left
            exit = R.anim.slide_out_right
        }
        popupToId?.let {
            popUpTo(popupToId) {
                inclusive = true
            }
        }
    }
    this.navigate(idDestination, bundle, anim)
}

fun Activity?.hideKeyboard() {
    val currentFocusedView = this?.currentFocus
    if (currentFocusedView != null) {
        val inputManager: InputMethodManager =
            this?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Activity.getNavigationBarHeight(): Int {
    val resourceId = this.resources.getIdentifier(
        "navigation_bar_height",
        "dimen",
        "android"
    )
    return if (resourceId > 0) this.resources.getDimensionPixelSize(resourceId) else 0
}

fun Context.showKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

fun TextView.setSizeText(textSize: Int) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(textSize))
}

fun Guideline.setPercentForGuideline(value: Float) {
    val lp =
        this.layoutParams as ConstraintLayout.LayoutParams
    lp.guidePercent = value
    this.layoutParams = lp
}

fun Calendar.clearTime() {
    this[Calendar.MINUTE] = 0
    this[Calendar.HOUR_OF_DAY] = 0
    this[Calendar.SECOND] = 0
    this[Calendar.MILLISECOND] = 0
}


fun TextView.setClickableTextView(isClick: Boolean, context: Context) {
    this.isClickable = isClick
    this.background.setColorFilter(
        if (isClick) ContextCompat.getColor(
            context, R.color.purple_200
        )
        else ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_IN
    )
}

fun <T> List<T>.safeSublist(fromIndex: Int, toIndex: Int): List<T> =
    this.subList(fromIndex.coerceAtLeast(0), toIndex.coerceAtMost(this.size))

fun TabLayout.getOnTabSelected(onTabSelected: (Int) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            onTabSelected.invoke(tab.position)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {}
        override fun onTabReselected(tab: TabLayout.Tab?) {}
    })
}

fun Spinner.onItemSelected(callback: (Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            callback.invoke(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

fun TextView.setFontFamilySelectedText(isCheck: Boolean) {
    this.typeface =
        if (isCheck) ResourcesCompat.getFont(
            this.context,
            R.font.roboto_regular
        )
        else ResourcesCompat.getFont(this.context, R.font.roboto_regular)
}

fun TextView.setTextColorSelectedText(isCheck: Boolean) {
    val color = ContextCompat.getColor(
        this.context,
        if (isCheck) android.R.color.white else R.color.black
    )
    this.setTextColor(color)
}

fun TextView.switchInputType(isShowPassword: Boolean? = null) {
    val typeTextPassword = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD
    this.inputType = if (isShowPassword == true) InputType.TYPE_CLASS_TEXT else typeTextPassword
}

fun TextView.setFontFamilyTabLayout(isActivated: Boolean) {
    this.typeface = if (isActivated) ResourcesCompat.getFont(
        context,
        R.font.roboto_medium
    )
    else ResourcesCompat.getFont(context, R.font.roboto_medium)
}

fun TabLayout.setFontForTab() {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.view?.children?.forEach {
                (it as? TextView)?.setFontFamilyTabLayout(it.isActivated)
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            tab?.view?.children?.forEach {
                (it as? TextView)?.setFontFamilyTabLayout(it.isActivated)
            }
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

    })
}

fun Activity.restartApp() {
    val newIntent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    this.startActivity(newIntent)
    finish()
}

fun Activity.logOut() {
    val newIntent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    this.startActivity(newIntent)
    finish()
}

/*fun Spinner.setupData(data: List<*>, offset: Int? = null) {
    adapter = ArrayAdapter(context, R.layout., data)
    post { setPadding(0, 0, 0, 0) }
    offset?.let {
        dropDownVerticalOffset = it
    }
}*/

/*fun BottomSheetDialog.setHeightBottomSheetDialog(context: Context, heightPercent: Int = 80) {
    this.setOnShowListener { dialogInterface ->
        val bottomSheetDialog = dialogInterface as BottomSheetDialog
        context.setupHeight(bottomSheetDialog, heightPercent)
    }
}*/

/*fun Context.setupHeight(bottomSheetDialog: BottomSheetDialog, heightPercent: Int) {
    val bottomSheet = bottomSheetDialog.findViewById() as FrameLayout?
    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet as View)
    val layoutParams = bottomSheet.layoutParams

    val windowHeight: Int = this.getWindowHeight() * heightPercent / 100
    if (layoutParams != null) {
        layoutParams.height =
            if (heightPercent < 100) windowHeight else WindowManager.LayoutParams.MATCH_PARENT
    }
    bottomSheet.layoutParams = layoutParams
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
}*/

fun Context.getWindowHeight(): Int {
    val displayMetrics = DisplayMetrics()
    (this as? Activity)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

fun Context.getWindowWidth(): Int {
    val displayMetrics = DisplayMetrics()
    (this as? Activity)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

fun TextView.setTextHasDataOrNull(data: String?) {
    text = if (data.isNullOrEmpty()) {
        context.getString(R.string.emptyLabel)
    } else data
}

fun getTextHasDataOrNull(context: Context, data: String?): String {
    return data ?: context.getString(R.string.emptyLabel)
}

fun TextView.setLeftDrawable(drawableID: Int) {
    setCompoundDrawablesWithIntrinsicBounds(
        ContextCompat.getDrawable(context, drawableID),
        null,
        null,
        null
    )
}

fun TextView.setDrawableRightByArrow(isExpand: Boolean) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(
        null, null,
        ContextCompat.getDrawable(
            context,
            if (isExpand) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
        ), null
    )
}

fun TextView.setRightDrawable(drawableId: Int) {
    this.tag = drawableId
    val drawable = context?.let { ContextCompat.getDrawable(it, drawableId) }
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        null,
        drawable,
        null
    )
}

fun TextView.setTopDrawable(drawableId: Int) {
    val drawable = context?.let { ContextCompat.getDrawable(it, drawableId) }
    this.setCompoundDrawablesWithIntrinsicBounds(
        null,
        drawable,
        null,
        null
    )
}

fun String.formatTextDownTheLine(): String {
    return replace(
        "\\n",
        System.getProperty("line.separator") ?: "\\n"
    ).replace("\\", "")
}


fun TextView.textViewChange(callback: () -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            callback.invoke()
        }

        override fun afterTextChanged(s: Editable?) {}
    })
}


fun EditText.disableSpace() {
    this.filters = arrayOf(object : InputFilter {
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            if (end == 1) {
                if (Character.isWhitespace(source?.get(0)!!)) {
                    return ""
                }
            }
            return null
        }
    })
}


fun getList6MonthAgoHistory(): ArrayList<Long> {
    val listMonth = ArrayList<Long>()
    Calendar.getInstance().let { calendar ->
        val currentCalendarYear = calendar.get(Calendar.YEAR)
        val currentCalendar = calendar.get(Calendar.MONTH)
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 6)
        while (calendar.get(Calendar.MONTH) < currentCalendar || calendar.get(Calendar.YEAR) < currentCalendarYear) {
            calendar.add(Calendar.MONTH, 1)
            listMonth.add(calendar.timeInMillis)
        }
    }
    return ArrayList(listMonth.reversed())
}

fun String.reformatDownLine(): String {
    return replace("\\n", "\n")
}

fun String.reFormatDisclaimerCFDString(): String {
    return replace("\\r", "")
}

fun String.reformatDownLineDouble(): String {
    return replace("\n", "\n\n")
}


fun NavController.checkInsFragmentBackStack(@IdRes destinationId: Int): Boolean {
    return try {
        getBackStackEntry(destinationId)
        true
    } catch (e: Exception) {
        false
    }
}


fun EditText.drawableRight(@DrawableRes right: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, right, 0);
}

fun EditText.initPopupSuggest(
    context: Context,
    callback: (Int) -> Unit
): ListPopupWindow {
    var listPopupWindow = ListPopupWindow(context).apply {
        anchorView = this@initPopupSuggest
        setOnItemClickListener { parent, view, position, id ->
            dismiss()
            callback.invoke(position)
        }
    }
    return listPopupWindow
}


fun String.toLowerCaseFirst(): String {
    try {
        var firstChar = this.substring(0, 1)
        return firstChar.toString().toUpperCase().plus(this.substring(1).toLowerCase())
    } catch (e: Exception) {

    }
    return ""
}


fun String.isPDFUrlExist() = this.contains(".pdf", ignoreCase = true)
        && !this.contains(/*GOOGLE_DOC_URL*/"", ignoreCase = true)

fun String.validateCharNumber(): String {
    val ret = StringBuilder()
    val matches: Matcher = Pattern.compile("[a-zA-Z0-9]+").matcher(this)
    while (matches.find()) {
        ret.append(matches.group())
    }
    return ret.toString()
}

fun EditText.setSwitchPasswordDialog(
    @DrawableRes iconIsShow: Int = R.drawable.ic_show_eye,
    @DrawableRes iconHide: Int = R.drawable.ic_hide_eye,
    isShowPassWord: Boolean = false
) {
    var isShow = isShowPassWord

    fun setDrawable() {
        drawableRight(
            if (isShow) iconIsShow else iconHide
        )
        showPass(isShow)
    }
    setDrawable()
    drawableRightClick {
        isShow = isShow.not()
        setDrawable()
    }
}

fun TextView.setTextDescription(content: String) {
    text = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

fun ImageView.isShowPassword(
    edtPassword: EditText,
    @DrawableRes iconIsShow: Int = R.drawable.ic_show_eye,
    @DrawableRes iconHide: Int = R.drawable.ic_hide_eye,
    isShowPassWord: Boolean = false
) {
    var isShow = isShowPassWord
    fun setDrawable() {
        setImageResource(
            if (isShow) iconIsShow else iconHide
        )
        edtPassword.showPass(isShow)
    }
    setDrawable()
    setOnClickListener {
        isShow = isShow.not()
        setDrawable()
    }
}

fun Context.getAlertDialog(
    mess: String,
    buttonText: String? = null,
    isShowNegativeBtn: Boolean = false,
    title: String? = getString(R.string.emptyLabel),
    buttonNegativeText: String? = null,
    isCancelable: Boolean = true,
    cancelListener: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    callBack: (() -> Unit?)? = null
): Dialog {
    val dialog = Dialog(this)
    val binding = DialogAlertBinding.inflate((this as Activity).layoutInflater)
    binding.apply {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(isCancelable)
        dialog.setCancelable(isCancelable)
        dialog.setOnDismissListener {
            onDismiss?.invoke()
        }
        title?.let {
            tvTitleAlert.text = it
        }
        tvTitleAlert.isVisible = title.isNullOrEmpty().not()
        tvMessageAlert.text = mess
        buttonText?.let {
            btnOkDialog.text = it
        }
        buttonNegativeText?.let {
            btnCancelDialog.text = it
        }
        btnOkDialog.setOnClickListener {
            callBack?.invoke()
            dialog.dismiss()
        }
        if (isShowNegativeBtn) {
            btnCancelDialog.setOnClickListener {
                dialog.dismiss()
                cancelListener?.invoke()
            }
            btnCancelDialog.visibility = View.VISIBLE
        }
        dialog.setContentView(binding.root)
    }
    return dialog
}

fun Double.convertDoubleToBigDecimal(): BigDecimal? {
    return try {
        BigDecimal(this)
    } catch (e: NumberFormatException) {
        null
    }
}

fun JSONObject.getStringValue(key: String): String? {
    return try {
        this.getString(key)
    } catch (e: Exception) {
        null
    }
}
