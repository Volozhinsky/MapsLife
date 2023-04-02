package com.volozhinsky.mapslife

import com.yandex.mapkit.geometry.Point
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.map.PlacemarkMapObject

class MainViewModel: ViewModel() {
    var currentPositionMapObject:PlacemarkMapObject? = null
    val teachMeSkillsPoint = Point(53.925274, 27.508354)
}