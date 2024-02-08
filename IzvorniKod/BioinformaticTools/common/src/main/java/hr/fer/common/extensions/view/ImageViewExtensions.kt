package hr.fer.common.extensions.view

import android.util.Log
import android.widget.ResourceCursorAdapter
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("android:imageUrl")
fun AppCompatImageView.setImageUri(uri: String?) {
    Glide
        .with(this)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:src")
fun AppCompatImageView.setImageSrc(src: Int) {
    if (src == 0) return
    Glide
        .with(this)
        .load(ResourcesCompat.getDrawable(this.resources, src, null))
        .into(this)
}