package com.example.windmoiveapp.extension

import android.content.Context
import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.example.windmoiveapp.R


fun SpannableStringBuilder.appendSpace(): SpannableStringBuilder = append(" ")
fun SpannableStringBuilder.appendThreeSpace(): SpannableStringBuilder = append("   ")

fun SpannableStringBuilder.append(texts: List<CharSequence>): SpannableStringBuilder {
    texts.forEach {
        append(it)
    }
    return this
}

fun SpannableStringBuilder.appendIcon(
    context: Context, @DrawableRes iconId: Int, @Px size: Int
): SpannableStringBuilder {
    this.append(" ")
    this.setSpan(
        context.icon(iconId, size), length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}

/*fun SpannableStringBuilder.withClickLinkSpanBlue(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.colorHighLight),
        pressTextColor = ContextCompat.getColor(context, R.color.colorPressHighlight),
        click = { click() }
    )
}*/

/*
fun SpannableStringBuilder.withClickLinkSpanBlueLight(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.colorHighLight),
        //todo: need update color night when press
        pressTextColor = ContextCompat.getColor(context, R.color.colorPressHighlight),
        otherStyles = context.styleSpans { typeface(Typeface.BOLD) },
        click = { click() }
    )
}
*/

/*fun SpannableStringBuilder.withClickLinkDigitalToken(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.colorHighLight),
        pressTextColor = ContextCompat.getColor(context, R.color.colorPressHighlight),
        otherStyles = context.styleSpans {
            underline()
            sansSerifMedium()
        },
        click = { click() }
    )
}*/
/*
fun SpannableStringBuilder.withClickLinkSpanWhite(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.colorRegular),
        pressTextColor = ContextCompat.getColor(context, R.color.colorSubText),
        otherStyles = context.styleSpans { underline() },
        click = { click() }
    )
}*/

/*fun SpannableStringBuilder.withClickLinkNameGroup(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.colorRegular),
        click = { click() },
    )
}*/

/*
fun SpannableStringBuilder.withLinkAlertsSetting(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.colorRegular),
        click = { click() },
        otherStyles = context.styleSpans { size(R.dimen.sp20) }
    )
}
*/

//fun SpannableStringBuilder.withClickLinkSpanColorOrange(
//    context: Context, text: CharSequence, click: () -> Unit
//): SpannableStringBuilder {
//    return withClickStateSpan(
//        text = text,
//        textColor = ContextCompat.getColor(context, R.color.red),
//        pressTextColor = ContextCompat.getColor(context, R.color.redDark),
//        otherStyles = context.styleSpans { typeface(Typeface.BOLD) },
//        click = { click() }
//    )
//}

/*
* Only use with LinkTouchMovementMethod
* */


fun SpannableStringBuilder.withStyleSpan(
    text: CharSequence,
    style: StyleSpans
): SpannableStringBuilder {
    val from = length
    append(text)
    style.forEach {
        setSpan(it, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return this
}

fun Context.styleSpans(options: StyleSpans.() -> Unit) = StyleSpans(this).apply(options)

class StyleSpans(private val context: Context) : Iterable<Any> {
    private val spans = mutableListOf<Any>()

    override fun iterator() = spans.iterator()

    fun size(@DimenRes id: Int) =
        spans.add(AbsoluteSizeSpan(context.resources.getDimensionPixelSize(id)))

    fun color(@ColorRes id: Int) =
        spans.add(ForegroundColorSpan(ContextCompat.getColor(context, id)))

    fun typeface(style: Int) = spans.add(StyleSpan(style))

    fun fontFamily(family: String) = spans.add(TypefaceSpan(family))
    fun sansSerifMedium() = fontFamily("sans-serif-medium")
    fun sansSerifRegular() = fontFamily("sans-serif-regular")

    fun textAppearance(@StyleRes appearanceId: Int) =
        spans.add(TextAppearanceSpan(context, appearanceId))

    fun backgroundColor(@ColorRes id: Int) =
        spans.add(BackgroundColorSpan(ContextCompat.getColor(context, id)))

    fun underline() = spans.add(UnderlineSpan())

    fun custom(span: ParcelableSpan) = spans.add(span)

    fun icon(@DrawableRes id: Int, size: Int): ImageSpan? {
        val drawable = ContextCompat.getDrawable(context, id)?.apply {
            setBounds(0, 0, size, size)
        }
        return ImageSpan(drawable ?: return null)
    }
}

fun Context.icon(@DrawableRes id: Int, size: Int): ImageSpan? {
    val drawable = ContextCompat.getDrawable(this, id)?.apply {
        setBounds(0, 0, size, size)
    }
    return ImageSpan(drawable ?: return null, ImageSpan.ALIGN_BASELINE)
}
