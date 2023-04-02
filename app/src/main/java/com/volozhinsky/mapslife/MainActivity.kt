package com.volozhinsky.mapslife

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(
                Manifest.permission.ACCESS_FINE_LOCATION,
                false
            ) -> {
                moveToLastLocation()
            }
            permissions.getOrDefault(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                false
            ) -> {
                moveToLastLocation()
            }
            else -> {

            }
        }
    }
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var mapview: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_main)
        initMaps()
        initViews()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapview?.onStart()
    }

    override fun onStop() {
        mapview?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun initMaps() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        MapKitFactory.initialize(this)
        mapview = findViewById<MapView>(R.id.mapview)
    }

    private fun initViews() {
        findViewById<Button>(R.id.btMoveToCurrent).setOnClickListener {
            launchRequestpermission()
        }
        mapview?.map?.mapObjects?.addPlacemark(mainViewModel.teachMeSkillsPoint)
    }

    private fun launchRequestpermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun moveToLastLocation() {
        fusedLocationClient?.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })?.addOnSuccessListener { location: Location? ->
            location?.let { showLocation(it) }
        }
    }

    private fun showLocation(location: Location) {
        Toast.makeText(
            this,
            "x= ${location.latitude} , y= ${location.longitude}",
            Toast.LENGTH_SHORT
        ).show()
        val currentPositionMapPoint = Point(location.latitude, location.longitude)
        mainViewModel.currentPositionMapObject?.let {
            mapview?.map?.mapObjects?.remove(it)
        }
        mainViewModel.currentPositionMapObject =
            mapview?.map?.mapObjects?.addPlacemark(currentPositionMapPoint)
        mapview?.map?.move(
            CameraPosition(currentPositionMapPoint, 14.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
    }
}