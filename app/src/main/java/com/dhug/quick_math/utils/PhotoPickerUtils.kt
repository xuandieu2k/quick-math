//package com.dhug.quick_math.utils
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import androidx.activity.result.ActivityResultLauncher
//import androidx.core.content.ContextCompat
//import com.dhug.quick_math.base.other.GlideEngine
//import com.cooldev.selector.basic.PictureSelectionModel
//import com.cooldev.selector.basic.PictureSelector
//import com.cooldev.selector.config.SelectMimeType
//import com.cooldev.selector.config.SelectModeConfig
//import com.cooldev.selector.entity.LocalMedia
//import com.cooldev.selector.interfaces.OnResultCallbackListener
//import com.cooldev.selector.language.LanguageConfig
//import com.cooldev.selector.style.BottomNavBarStyle
//import com.cooldev.selector.style.PictureSelectorStyle
//import com.cooldev.selector.style.SelectMainStyle
//import com.cooldev.selector.style.TitleBarStyle
//
//
//object PhotoPickerUtils {
//    private var selectorStyle: PictureSelectorStyle? = null
//
//    fun showImagePickerChooseAvatar(
//        activity: Activity, intent: ActivityResultLauncher<Intent>
//    ) {
//        PictureSelector.create(activity).openSystemGallery(SelectMimeType.ofImage())
//            .setSelectionMode(SelectModeConfig.SINGLE).forSystemResultActivity(intent)
//    }
//
//    fun showImagePickerUploadMedia(
//        activity: Activity,
//        intent: ActivityResultLauncher<Intent>,
//        selectedMediaList: MutableList<LocalMedia>
//    ) {
//        styleSelector(activity)
//        val selectionModel: PictureSelectionModel =
//            PictureSelector.create(activity).openGallery(SelectMimeType.ofAll())
//                .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//                .isUseSystemVideoPlayer(true).setSelectionMode(SelectModeConfig.MULTIPLE)
//                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE).isDisplayCamera(true)
//                .isWithSelectVideoImage(true).isPreviewFullScreenMode(true)
//                .isPreviewZoomEffect(true).isPreviewImage(true).isPreviewVideo(true)
//                .isPreviewAudio(true).setMaxSelectNum(4).setMaxVideoSelectNum(1)
//                .setSelectMaxFileSize(10240).isGif(true)
//                .setSelectedData(selectedMediaList)
//        selectionModel.forResult(intent)
//    }
//
//    fun showImagePickerUploadImage(
//        activity: Activity,
//        intent: ActivityResultLauncher<Intent>,
//        selectedMediaList: MutableList<LocalMedia>
//    ) {
//        styleSelector(activity)
//        val selectionModel: PictureSelectionModel =
//            PictureSelector.create(activity).openGallery(SelectMimeType.ofImage())
//                .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//                .isUseSystemVideoPlayer(true).setSelectionMode(SelectModeConfig.MULTIPLE)
//                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE).isDisplayCamera(true)
//                .isWithSelectVideoImage(true).isPreviewFullScreenMode(true)
//                .isPreviewZoomEffect(true).isPreviewImage(true).isPreviewVideo(true)
//                .isPreviewAudio(true).setMaxSelectNum(10)
//                .setSelectMaxFileSize(50*1024).isGif(true)
//                .setSelectedData(selectedMediaList)
//        selectionModel.forResult(intent)
//    }
//
//    fun showImagePickerUploadPosterInEvent(
//        activity: Activity,
//        intent: ActivityResultLauncher<Intent>,
//        selectedMediaList: MutableList<LocalMedia>
//    ) {
//        styleSelector(activity)
//        val selectionModel: PictureSelectionModel =
//            PictureSelector.create(activity).openGallery(SelectMimeType.ofImage())
//                .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//                .isUseSystemVideoPlayer(true).setSelectionMode(SelectModeConfig.SINGLE)
//                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE).isDisplayCamera(true)
//                .isWithSelectVideoImage(true).isPreviewFullScreenMode(true)
//                .isPreviewZoomEffect(true).isPreviewImage(true).isPreviewVideo(true)
//                .isPreviewAudio(true).setMaxSelectNum(1).isGif(true)
//                .setSelectedData(selectedMediaList)
//        selectionModel.forResult(intent)
//    }
//
//
//    fun showImagePickerUploadComment(
//        activity: Activity,
//        intent: ActivityResultLauncher<Intent>,
//        selectedMediaList: MutableList<LocalMedia>
//    ) {
//        styleSelector(activity)
//        val selectionModel: PictureSelectionModel =
//            PictureSelector.create(activity).openGallery(SelectMimeType.ofAll())
//                .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//                .isUseSystemVideoPlayer(true).setSelectionMode(SelectModeConfig.SINGLE)
//                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE).isDisplayCamera(true)
//                .isWithSelectVideoImage(true).isPreviewFullScreenMode(true)
//                .isPreviewZoomEffect(true).isPreviewImage(true).isPreviewVideo(true)
//                .isPreviewAudio(true).setMaxSelectNum(1).setMaxVideoSelectNum(1).isGif(true)
//                .setSelectedData(selectedMediaList)
//        selectionModel.forResult(intent)
//    }
//
//    fun showImagePickerChat(
//        activity: Activity, intent: ActivityResultLauncher<Intent>
//    ) {
//        styleSelector(activity)
//        val selectionModel: PictureSelectionModel =
//            PictureSelector.create(activity).openGallery(SelectMimeType.ofAll())
//                .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//                .isUseSystemVideoPlayer(true).setSelectionMode(SelectModeConfig.MULTIPLE)
//                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE).isDisplayCamera(true)
//                .isWithSelectVideoImage(true).isPreviewFullScreenMode(true)
//                .isPreviewZoomEffect(true).isPreviewImage(true).isPreviewVideo(true)
//                .isPreviewAudio(true).setMaxSelectNum(2).setMaxVideoSelectNum(1)
//                .setSelectMaxDurationSecond(120)
//                .setSelectMaxFileSize(10240).isGif(true)
//        selectionModel.forResult(intent)
//    }
//
//    fun showImagePickerChooseAvatarNotGif(
//        activity: Activity, intent: ActivityResultLauncher<Intent>
//    ) {
//        styleSelector(activity)
//        PictureSelector.create(activity).openGallery(SelectMimeType.ofImage())
//            .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//            .setLanguage(LanguageConfig.SYSTEM_LANGUAGE)
//            .isDisplayCamera(false).setSelectionMode(SelectModeConfig.SINGLE).isGif(false)
//            .forResult(intent)
//    }
//
//
//    fun showImagePickerNewsFeed(
//        activity: Activity, intent: ActivityResultLauncher<Intent>, listData: MutableList<LocalMedia>
//    ) {
//        styleSelector(activity)
//        val selectionModel: PictureSelectionModel =
//            PictureSelector.create(activity).openGallery(SelectMimeType.ofAll())
//                .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//                .isUseSystemVideoPlayer(true).setSelectionMode(SelectModeConfig.MULTIPLE)
//                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE).isDisplayCamera(true)
//                .isWithSelectVideoImage(true).isPreviewFullScreenMode(true)
//                .isPreviewZoomEffect(true).isPreviewImage(true).isPreviewVideo(true)
//                .isPreviewAudio(true).setMaxSelectNum(30).setMaxVideoSelectNum(5)
//                .setSelectedData(listData).isGif(true)
//        selectionModel.forResult(intent)
//    }
//
//    fun showImagePickerOneMedia(
//        activity: Activity, intent: ActivityResultLauncher<Intent>, listData: MutableList<LocalMedia>
//    ) {
//        styleSelector(activity)
//        val selectionModel: PictureSelectionModel =
//            PictureSelector.create(activity).openGallery(SelectMimeType.ofAll())
//                .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//                .isUseSystemVideoPlayer(true).setSelectionMode(SelectModeConfig.MULTIPLE)
//                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE).isDisplayCamera(true)
//                .isWithSelectVideoImage(true).isPreviewFullScreenMode(true)
//                .isPreviewZoomEffect(true).isPreviewImage(true).isPreviewVideo(true)
//                .isPreviewAudio(true).setMaxSelectNum(1).setMaxVideoSelectNum(1)
//                .setSelectedData(listData).isGif(true)
//        selectionModel.forResult(intent)
//    }
//
//    fun showImagePickerInDialog(
//        context: Context,
//        onResultCallbackListener: OnResultCallbackListener<LocalMedia>,
//        listData: MutableList<LocalMedia>
//    ) {
//        styleSelector(context)
//        val selectionModel: PictureSelectionModel =
//            PictureSelector.create(context).openGallery(SelectMimeType.ofAll())
//                .setSelectorUIStyle(selectorStyle).setImageEngine(GlideEngine.createGlideEngine())
//                .isUseSystemVideoPlayer(true).setSelectionMode(SelectModeConfig.MULTIPLE)
//                .setLanguage(LanguageConfig.SYSTEM_LANGUAGE).isDisplayCamera(true)
//                .isWithSelectVideoImage(true).isPreviewFullScreenMode(true)
//                .isPreviewZoomEffect(true).isPreviewImage(true).isPreviewVideo(true)
//                .isPreviewAudio(true).setMaxSelectNum(1).setMaxVideoSelectNum(1)
//                .setSelectedData(listData).isGif(true)
//        selectionModel.forResult(onResultCallbackListener)
//    }
//
//    private fun styleSelector(context: Context) {
//        selectorStyle = PictureSelectorStyle()
//
//        val whiteTitleBarStyle = TitleBarStyle()
//
//        whiteTitleBarStyle.titleBackgroundColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_white)
//        whiteTitleBarStyle.titleDrawableRightResource =
//            com.dhug.quick_math.R.drawable.ic_arrow_right
//        whiteTitleBarStyle.titleLeftBackResource =
//            com.dhug.quick_math.R.drawable.ic_arrow_left
//        whiteTitleBarStyle.titleLeftBackResource
//        whiteTitleBarStyle.titleTextColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_black)
//        whiteTitleBarStyle.titleCancelTextColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_53575e)
//        whiteTitleBarStyle.isDisplayTitleBarLine = true
//
//        val whiteBottomNavBarStyle = BottomNavBarStyle()
//        whiteBottomNavBarStyle.bottomNarBarBackgroundColor = Color.parseColor("#EEEEEE")
//        whiteBottomNavBarStyle.bottomPreviewSelectTextColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_53575e)
//
//        whiteBottomNavBarStyle.bottomPreviewNormalTextColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_9b)
//        whiteBottomNavBarStyle.bottomPreviewSelectTextColor = ContextCompat.getColor(
//            context, com.dhug.quick_math.R.color.green_primary
//        )
//        whiteBottomNavBarStyle.isCompleteCountTips = false
//        whiteBottomNavBarStyle.bottomEditorTextColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_53575e)
//        whiteBottomNavBarStyle.bottomOriginalTextColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_53575e)
//
//        val selectMainStyle = SelectMainStyle()
//        selectMainStyle.statusBarColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_white)
//        selectMainStyle.isDarkStatusBarBlack = true
//        selectMainStyle.selectNormalTextColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_9b)
//        selectMainStyle.selectTextColor = ContextCompat.getColor(
//            context, com.dhug.quick_math.R.color.green_primary
//        )
//        selectMainStyle.previewSelectBackground =
//            com.cooldev.selector.R.drawable.ps_checkbox_selector
//        selectMainStyle.selectBackground = com.cooldev.selector.R.drawable.ps_checkbox_selector
//        selectMainStyle.setSelectText(com.cooldev.selector.R.string.ps_done_front_num)
//        selectMainStyle.mainListBackgroundColor =
//            ContextCompat.getColor(context, com.cooldev.selector.R.color.ps_color_white)
//        selectorStyle?.let {
//            it.titleBarStyle = whiteTitleBarStyle
//            it.bottomBarStyle = whiteBottomNavBarStyle
//            it.selectMainStyle = selectMainStyle
//        }
//    }
//
//
//}