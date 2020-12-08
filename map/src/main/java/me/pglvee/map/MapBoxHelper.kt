package me.pglvee.map

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.*
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager

private var mapBox: MapboxMap? = null

fun SupportMapFragment.init(
    lat: Double,
    lng: Double,
    listener: OnMapListener? = null,
    markerView: View? = null
) {
    getMapAsync { map: MapboxMap ->
        mapBox = map
        map.setStyle(Style.MAPBOX_STREETS) {
            // Map is set up and the style has loaded. Now you can add data or make other map adjustments
            val uiSettings: UiSettings = map.uiSettings
            uiSettings.isCompassEnabled = false
            uiSettings.isRotateGesturesEnabled = false
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15.0))
            if (listener != null) {
                map.addOnCameraIdleListener {
                    map.cameraPosition.target?.let {
                        listener.onCameraIdle(it.latitude, it.longitude)
                    }
                }
                listener.onInitComplete()
            }
            if (markerView != null) {
                val mapView = requireView() as MapView
                val markerViewManager = MarkerViewManager(mapView, map)
                val marker = MarkerView(LatLng(lat, lng), markerView)
                markerViewManager.addMarker(marker)
                lifecycle.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_DESTROY)
                            markerViewManager.onDestroy()
                    }
                })
            }
        }
    }
}

fun SupportMapFragment.snapshot(bitmap: (Bitmap) -> Unit) {
    mapBox?.snapshot { bitmap.invoke(it) }
}