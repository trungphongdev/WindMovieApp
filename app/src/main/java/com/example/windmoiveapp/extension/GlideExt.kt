package com.example.windmoiveapp.extension

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.windmoiveapp.R

fun ImageView.loadRoundedImage(
    image: Int?, @DimenRes radiusId: Int = R.dimen.dp4,
    @DrawableRes defaultDrawableId: Int? = null
) {
    Glide.with(this)
        .load(image)
        .placeholder(defaultDrawableId ?: R.drawable.ic_user)
        .error(defaultDrawableId ?: R.drawable.ic_baseline_error_24)
        .transform(RoundedCorners(context.resources.getDimensionPixelSize(radiusId)))
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadCircleImage(
    urlImage: String?, @DimenRes radiusId: Int = R.dimen.dp4,
    @DrawableRes defaultDrawableId: Int? = null
) {
    Glide.with(this)
        .load(urlImage)
        .placeholder(defaultDrawableId ?: R.drawable.ic_user)
        .error(defaultDrawableId ?: R.drawable.ic_baseline_error_24)
        .circleCrop()
        .into(this)
}

fun ImageView.loadCircleImage(
    uri: Uri?, @DimenRes radiusId: Int = R.dimen.dp4,
    @DrawableRes defaultDrawableId: Int? = null
) {
    Glide.with(this)
        .load(uri)
        .placeholder(defaultDrawableId ?: R.drawable.logohome)
        .error(defaultDrawableId ?: com.google.android.material.R.drawable.mtrl_ic_error)
        .into(this)
}


fun ImageView.loadImage(
    urlImage: String,
    @DrawableRes defaultDrawableId: Int = R.drawable.logohome,
    @DimenRes radiusId: Int = R.dimen.dp4
) {
    Glide.with(this)
        .load(urlImage)
        .placeholder(defaultDrawableId)
        .transform(RoundedCorners(context.resources.getDimensionPixelSize(radiusId)))
        .transition(DrawableTransitionOptions.withCrossFade())
        .error(defaultDrawableId)
        .into(this)
}