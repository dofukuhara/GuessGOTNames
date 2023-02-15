package br.com.fdtechcorp.android.guessgotnames.lib.common.service

import android.widget.ImageView
import com.squareup.picasso.Picasso

interface ImageLoader {
    fun loadImage(imageUrl: String, width: Int, height: Int, imageView: ImageView)
}

internal class PicassoImageLoader : ImageLoader {
    override fun loadImage(imageUrl: String, width: Int, height: Int, imageView: ImageView) {
        Picasso.get().load(imageUrl).resize(width, height).into(imageView)
    }
}

