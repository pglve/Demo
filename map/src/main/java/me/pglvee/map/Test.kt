/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.map

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
//import com.mapbox.mapboxsdk.plugins.china.shift.ChinaBoundsChecker



//Determining whether coordinates are in China
private fun t1(activity: AppCompatActivity, location: Location, lat: Double, lng: Double) {
    // Pass in an Android-system Location object
//    val locationIsInChina = ChinaBoundsChecker.locationIsInChina(activity, location)

    // Pass in a Mapbox LatLng object
    //    val latLngIsInChina = ChinaBoundsChecker.latLngIsInChina(activity, LatLng(lat, lng))

    // Pass in a Mapbox Point object
    //    val pointIsInChina = ChinaBoundsChecker.pointIsInChina(activity, Point.fromLngLat(lng, lat))
}