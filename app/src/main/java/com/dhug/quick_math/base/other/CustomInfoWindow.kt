//package com.dhug.quick_math.base.other
//
//import org.osmdroid.views.overlay.Marker
//import org.osmdroid.views.MapView
//import android.view.View
//import androidx.core.view.isVisible
//import com.dhug.quick_math.R
//import com.dhug.quick_math.base.wiget.AppTextViewBold
//import org.osmdroid.views.overlay.infowindow.InfoWindow
//
////class CustomInfoWindow(private val marker: Marker, mapView: MapView) :
////    InfoWindow(R.layout.custom_marker_info, mapView) {
////
////    override fun getView(): View {
////        val view = super.getView()
////        val titleText = view.findViewById<AppTextViewBold>(R.id.tvTitleMarker)
////        titleText.text = marker.title
////
////        return view
////    }
////
////    override fun onOpen(item: Any?) {
////        view.isVisible = true
////    }
////
////    override fun onClose() {
////        view.isVisible = false
////    }
////}
