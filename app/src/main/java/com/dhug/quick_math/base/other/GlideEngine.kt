//package com.dhug.quick_math.base.other
//
//import android.content.Context
//import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.bitmap.CenterCrop
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners
//import com.dhug.quick_math.R
//import com.cooldev.selector.engine.ImageEngine
//import com.cooldev.selector.utils.ActivityCompatHelper
//
//class GlideEngine private constructor() : ImageEngine {
//    /**
//     * Loading pictures
//     *
//     * @param context context
//     * @param url Resource url
//     * @param imageView image hosting control
//     */
//    override fun loadImage(context: Context, url: String, imageView: ImageView) {
//        if (!ActivityCompatHelper.assertValidRequest(context)) {
//            return
//        }
//        Glide.with(context)
//            .load(url)
//            .into(imageView)
//    }
//
//    override fun loadImage(
//        context: Context,
//        imageView: ImageView,
//        url: String,
//        maxWidth: Int,
//        maxHeight: Int
//    ) {
//        if (!ActivityCompatHelper.assertValidRequest(context)) {
//            return
//        }
//        Glide.with(context)
//            .load(url)
//            .override(maxWidth, maxHeight)
//            .into(imageView)
//    }
//
//    /**
//     * Load the album directory cover
//     *
//     * @param context context
//     * @param url Image path
//     * @param imageView hosts image ImageView
//     */
//    override fun loadAlbumCover(context: Context, url: String, imageView: ImageView) {
//        if (!ActivityCompatHelper.assertValidRequest(context)) {
//            return
//        }
//        Glide.with(context)
//            .asBitmap()
//            .load(url)
//            .override(180, 180)
//            .sizeMultiplier(0.5f)
//            .transform(CenterCrop(), RoundedCorners(8))
//            .placeholder(R.drawable.image_default)
//            .into(imageView)
//    }
//
//    /**
//     * Loading picture list
//     *
//     * @param context context
//     * @param url Image path
//     * @param imageView hosts image ImageView
//     */
//    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
//        if (!ActivityCompatHelper.assertValidRequest(context)) {
//            return
//        }
//        Glide.with(context)
//            .load(url)
//            .override(200, 200)
//            .centerCrop()
//            .placeholder(R.drawable.image_default)
//            .into(imageView)
//    }
//
//    override fun pauseRequests(context: Context) {
//        if (!ActivityCompatHelper.assertValidRequest(context)) {
//            return
//        }
//        Glide.with(context).pauseRequests()
//    }
//
//    override fun resumeRequests(context: Context) {
//        if (!ActivityCompatHelper.assertValidRequest(context)) {
//            return
//        }
//        Glide.with(context).resumeRequests()
//    }
//
//    private object InstanceHolder {
//        val instance = GlideEngine()
//    }
//
//    companion object {
//        fun createGlideEngine(): GlideEngine {
//            return InstanceHolder.instance
//        }
//    }
//}