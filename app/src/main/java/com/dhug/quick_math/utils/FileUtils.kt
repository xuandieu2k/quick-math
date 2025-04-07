package com.dhug.quick_math.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.ImageCapture
import androidx.camera.video.MediaStoreOutputOptions
import androidx.core.content.FileProvider
import com.dhug.quick_math.R
import com.hjq.toast.ToastUtils
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

@Singleton
object FileUtils {

    const val STORAGE_VIDEO = "DashCam Recorded Videos"
    const val STORAGE_IMAGE = "DashCam Image"

    const val CAMERA_HEADER_NAME = "DashCam-take-photo-"
    const val VIDEO_HEADER_NAME = "DashCam-recording-"
    private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    fun createMediaStoreOutputOptions(
        fileName: String = VIDEO_HEADER_NAME, context: Context
    ): MediaStoreOutputOptions {
        val name = fileName + SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(
            System.currentTimeMillis()
        ) + ".mp4"
        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH, STORAGE_VIDEO
                )
            }
        }
        return MediaStoreOutputOptions.Builder(
            context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()
    }

    fun createPictureStoreOutput(
        fileName: String = CAMERA_HEADER_NAME, context: Context
    ): ImageCapture.OutputFileOptions {
        val name = fileName + "" + SimpleDateFormat(
            FILENAME_FORMAT, Locale.US
        ).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, STORAGE_IMAGE)
            }
        }
        return ImageCapture.OutputFileOptions.Builder(
            context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        ).build()
    }


    fun createLocalVideoOutputOptions(
        context: Context, fileName: String = VIDEO_HEADER_NAME
    ): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES) ?: context.cacheDir
        val name = fileName + SimpleDateFormat(
            FILENAME_FORMAT,
            Locale.getDefault()
        ).format(System.currentTimeMillis()) + ".mp4"
        val videosDir = File(dir, "")
        if (!videosDir.exists()) {
            videosDir.mkdirs()
        }
        return File(videosDir, name)
    }

    fun createLocalImageOutputOptions(
        context: Context, fileName: String = CAMERA_HEADER_NAME
    ): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: context.cacheDir
        val name = fileName + SimpleDateFormat(
            FILENAME_FORMAT,
            Locale.getDefault()
        ).format(System.currentTimeMillis()) + ".jpg"
        val imagesDir = File(dir, "")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        return File(imagesDir, name)
    }


    fun deleteFileFromUrl(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }

    fun deleteFileFromMediaStore(context: Context, fileId: Long, isImage: Boolean): Boolean {
        val uri = if (isImage) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

        val fileUri = ContentUris.withAppendedId(uri, fileId)
        return try {
            val rowsDeleted = context.contentResolver.delete(fileUri, null, null)
            rowsDeleted > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun shareFileFromStorage(context: Context, fileUri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = context.contentResolver.getType(fileUri) ?: "application/octet-stream"
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, context.getString(R.string.share_file_by))
        context.startActivity(chooser)
    }

    private fun shareFileFromMediaStore(context: Context, mediaId: Long, mediaType: String) {
        val contentUri = when (mediaType) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> throw IllegalArgumentException(context.getString(R.string.file_not_support))
        }
        val fileUri = ContentUris.withAppendedId(contentUri, mediaId)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = context.contentResolver.getType(fileUri) ?: "application/octet-stream"
            putExtra(Intent.EXTRA_STREAM, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, context.getString(R.string.share_file_by))
        context.startActivity(chooser)
    }

    fun shareMultipleFiles(context: Context, files: List<File?>, mediaUris: List<Uri?>) {
        val uris = mutableListOf<Uri>()

        // Thêm các tệp từ Internal/External Storage
        files.forEach { file ->
            file?.let {
                if (it.exists()) {
                    val uri = FileProvider.getUriForFile(
                        context, "${context.packageName}.fileprovider", it
                    )
                    uris.add(uri)
                }
            }
        }

        // Thêm các URI từ MediaStore
        mediaUris.forEach { uri ->
            uri?.let {
                uris.add(it)
            }
        }

        if (uris.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
                type = "*/*" // Đặt kiểu tệp là bất kỳ
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Cấp quyền tạm thời
            }

            val chooser = Intent.createChooser(intent, context.getString(R.string.share_file_by))
            context.startActivity(chooser)
        } else {
            ToastUtils.show(context.getString(R.string.not_have_any_file_is_share))
        }
    }

    fun saveImageToMediaStore(
        context: Context, fileName: String = CAMERA_HEADER_NAME, content: ByteArray
    ): Pair<Uri?, String> {
        val name = fileName + SimpleDateFormat(
            FILENAME_FORMAT, Locale.getDefault()
        ).format(System.currentTimeMillis()) + ".jpg"

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + STORAGE_IMAGE)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        val path = if (uri != null) {
            resolver.openOutputStream(uri).use { outputStream ->
                try {
                    outputStream?.write(content)
                    Timber.tag("MediaStoreSave").d( "Image saved to MediaStore at: $uri")
                } catch (e: IOException) {
                    Timber.tag("MediaStoreSave").e( "Error saving image: ${e.message} $e")
                }
            }
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/$STORAGE_IMAGE/$name"
        } else {
            Timber.tag("MediaStoreSave").e( "Failed to create MediaStore entry for image.")
            ""
        }

        return Pair(uri, path)
    }

    fun saveVideoToMediaStore(
        context: Context, fileName: String = VIDEO_HEADER_NAME, content: ByteArray
    ): Pair<Uri?, String> {
        val name = fileName + SimpleDateFormat(
            FILENAME_FORMAT, Locale.getDefault()
        ).format(System.currentTimeMillis()) + ".mp4"

        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MOVIES + "/" + STORAGE_VIDEO)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)

        val path = if (uri != null) {
            resolver.openOutputStream(uri).use { outputStream ->
                try {
                    outputStream?.write(content)
                    Timber.tag("MediaStoreSave").e( "Video saved to MediaStore at: $uri")
                } catch (e: IOException) {
                    Timber.tag("MediaStoreSave").e( "Error saving video: ${e.message}")
                }
            }
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)}/$STORAGE_VIDEO/$name"
        } else {
            Timber.tag("MediaStoreSave").e( "Failed to create MediaStore entry for video.")
            ""
        }

        return Pair(uri, path)
    }

    @SuppressLint("TimberArgCount")
    fun deleteFromMediaStoreWithPath(context: Context, filePath: String): Boolean {
        val contentResolver = context.contentResolver
        val uri: Uri? = getMediaStoreUriFromPath(context, filePath)

        return if (uri != null) {
            try {
                val rowsDeleted = contentResolver.delete(uri, null, null)
                if (rowsDeleted > 0) {
                    Timber.d("MediaStoreDelete", "File deleted successfully: $filePath")
                    true
                } else {
                    Timber.e("MediaStoreDelete", "Failed to delete file: $filePath")
                    false
                }
            } catch (e: Exception) {
                Timber.e(e, "MediaStoreDelete", "Error deleting file: ${e.message}")
                false
            }
        } else {
            Timber.e("MediaStoreDelete", "Failed to find URI for file: $filePath")
            false
        }
    }

    private fun getMediaStoreUriFromPath(context: Context, filePath: String): Uri? {
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = MediaStore.MediaColumns.DATA + " = ?"
        val selectionArgs = arrayOf(filePath)

        val uri: Uri = if (filePath.contains(Environment.DIRECTORY_PICTURES)) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else if (filePath.contains(Environment.DIRECTORY_MOVIES)) {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        } else {
            return null
        }

        context.contentResolver.query(uri, projection, selection, selectionArgs, null).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                return ContentUris.withAppendedId(uri, id)
            }
        }

        return null
    }




}