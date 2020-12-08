package me.pglvee.map

import android.graphics.Bitmap
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.TextureMapFragment
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions

private var markerOption: MarkerOptions? = null

fun TextureMapFragment.init(
    lat: Double,
    lng: Double,
    listener: OnMapListener? = null,
    markerView: View? = null
) {
    map.uiSettings.isZoomControlsEnabled = false
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 18f))
    if (listener != null) {
        map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChange(p0: CameraPosition?) {
            }

            override fun onCameraChangeFinish(p0: CameraPosition?) {
                p0?.target?.let {
                    listener.onCameraIdle(it.latitude, it.longitude)
                }
            }

        })
        listener.onInitComplete()
    }
    if (markerView != null) {
        val icon = BitmapDescriptorFactory.fromView(markerView)
        markerOption = MarkerOptions().position(LatLng(lat, lng)).icon(icon)
        map.addMarker(markerOption)
    }
}

fun TextureMapFragment.updateMarker(
    lat: Double? = null,
    lng: Double? = null,
    markerView: View? = null
) {
    if (markerOption == null) markerOption = MarkerOptions()
    if (lat != null && lng != null)
        markerOption = markerOption?.position(LatLng(lat, lng))
    if (markerView != null)
        markerOption = markerOption?.icon(BitmapDescriptorFactory.fromView(markerView))
    map.addMarker(markerOption)
}

fun TextureMapFragment.snapshot(bitmap: (Bitmap) -> Unit) {
    map.getMapScreenShot(object : AMap.OnMapScreenShotListener {
        override fun onMapScreenShot(p0: Bitmap) {
        }

        override fun onMapScreenShot(p0: Bitmap, p1: Int) {
            bitmap.invoke(p0)
        }
    })
}