package me.pglvee.map

interface OnMapListener {

    fun onCameraIdle(lat: Double, lng: Double) {}

    fun onInitComplete() {}
}