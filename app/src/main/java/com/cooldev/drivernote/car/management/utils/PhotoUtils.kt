package com.dhug.example.utils

import android.annotation.SuppressLint
import javax.inject.Singleton

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.media.ExifInterface
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dhug.example.R
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.File
import java.io.FileOutputStream

/**
 * @Author: NGUYEN XUAN DIEU
 * @Date: 22 / 10 / 2024
 */
@Singleton
object PhotoUtils {

    fun getLinkPhoto(photo: String?): String {
        return String.format("%s%s", "", photo)
    }

    fun loadPhotoImageAvatar(url: String, view: ImageView) {
        if (url.contains("/")) {
            Glide.with(view.context.applicationContext).asBitmap().load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL).apply(
                    RequestOptions().placeholder(R.drawable.image_default).centerCrop()
                        .error(R.drawable.image_default)
                ).transform(MultiTransformation(CircleCrop())).into(view)
        } else {
            Glide.with(view.context.applicationContext).asBitmap().load(
                String.format(
                    "%s%s", "", url
                )
            ).diskCacheStrategy(DiskCacheStrategy.ALL).apply(
                RequestOptions().placeholder(R.drawable.image_default).centerCrop().transform(
                    MultiTransformation(CircleCrop())
                ).error(R.drawable.image_default)
            ).into(view)
        }
    }

    fun loadPhotoImageNormal(url: String, view: ImageView) {
        if (url.contains("/")) {
            Glide.with(view.context.applicationContext).asBitmap().load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL).apply(
                    RequestOptions().placeholder(R.drawable.image_default)
                        .error(R.drawable.image_default)
                ).into(view)
        } else {
            Glide.with(view.context.applicationContext).asBitmap().load(
                String.format(
                    "%s%s", "", url
                )
            ).diskCacheStrategy(DiskCacheStrategy.ALL).apply(
                RequestOptions().placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default)
            ).into(view)
        }
    }

    fun loadPhotoRound(url: String, view: ImageView, enableBlur: Boolean = false) {
        if (enableBlur) {
            if (url.contains("/")) {
                Glide.with(view.context.applicationContext).asBitmap().load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).apply(
                        RequestOptions().placeholder(R.drawable.image_default)
                            .error(R.drawable.image_default)
                    ).centerCrop().transform(
                        MultiTransformation(
                            RoundedCorners(
                                view.context.resources.getDimension(com.cooldev.base.R.dimen.dp_8)
                                    .toInt()
                            )
                        ),
                        BlurTransformation(12)
                    ).into(view)
            } else {
                Glide.with(view.context.applicationContext).asBitmap().load(
                    String.format(
                        "%s%s", "", url
                    )
                ).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().transform(
                    MultiTransformation(
                        RoundedCorners(
                            view.context.resources.getDimension(com.cooldev.base.R.dimen.dp_8)
                                .toInt()
                        )
                    )
                ).apply(
                    RequestOptions().centerCrop().transform(
                        MultiTransformation(
                            RoundedCorners(
                                view.context.resources.getDimension(com.cooldev.base.R.dimen.dp_8)
                                    .toInt()
                            )
                        ),
                        BlurTransformation(12)
                    ).placeholder(R.drawable.image_default).error(R.drawable.image_default)
                ).into(view)
            }
            return
        }
        if (url.contains("/")) {
            Glide.with(view.context.applicationContext).asBitmap().load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL).apply(
                    RequestOptions().placeholder(R.drawable.image_default)
                        .error(R.drawable.image_default)
                ).centerCrop().transform(
                    MultiTransformation(
                        RoundedCorners(
                            view.context.resources.getDimension(com.cooldev.base.R.dimen.dp_8)
                                .toInt()
                        )
                    )
                ).into(view)
        } else {
            Glide.with(view.context.applicationContext).asBitmap().load(
                String.format(
                    "%s%s", "", url
                )
            ).diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().transform(
                MultiTransformation(
                    RoundedCorners(
                        view.context.resources.getDimension(com.cooldev.base.R.dimen.dp_8).toInt()
                    )
                )
            ).apply(
                RequestOptions().centerCrop().transform(
                    MultiTransformation(
                        RoundedCorners(
                            view.context.resources.getDimension(com.cooldev.base.R.dimen.dp_8)
                                .toInt()
                        )
                    )
                ).placeholder(R.drawable.image_default).error(R.drawable.image_default)
            ).into(view)
        }
    }


    fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    fun fixImageRotation(bitmap: Bitmap, imagePath: String): Bitmap {
        val exif = ExifInterface(imagePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        // Xoay ảnh nếu cần thiết
        return if (rotationDegrees != 0) {
            val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
            Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )
        } else {
            bitmap // Không cần xoay
        }
    }


    fun bitmapFromVector(context: Context, vectorResId: Int): Bitmap {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
            ?: throw IllegalArgumentException("Resource not found!")

        vectorDrawable.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )

        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    fun getResourceFilePath(context: Context, rawResId: Int, fileName: String): String {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) {
            context.resources.openRawResource(rawResId).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }
        return file.absolutePath
    }

}