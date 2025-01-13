package com.example.hw1_obstacleracinggame.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hw1_obstacleracinggame.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var scoresLocations: List<Pair<String, LatLng>> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val israelBounds = LatLngBounds(
            LatLng(29.453379, 34.267499),
            LatLng(33.335631, 35.896244)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(israelBounds, 100))

        addAllLocations()
    }

    private fun addAllLocations() {
        if (::googleMap.isInitialized && scoresLocations.isNotEmpty()) {
            for ((title, location) in scoresLocations) {
                googleMap.addMarker(MarkerOptions().position(location).title(title))
            }
        }
    }

    fun setMarkers(locations: List<Pair<String, LatLng>>) {
        scoresLocations = locations
        if (::googleMap.isInitialized) {
            googleMap.clear()
            addAllLocations()
        }
    }

    fun focusOnLocation(location: LatLng) {
        if (::googleMap.isInitialized) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }
    }
}

