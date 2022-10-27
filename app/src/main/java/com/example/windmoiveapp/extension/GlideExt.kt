package com.example.windmoiveapp.extension

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
