package com.volozhinsky.mapslife

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MapsLifeApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAP_KIT_APY_KEY)
    }

    companion object{
        const val MAP_KIT_APY_KEY = "29ee71e9-ad68-4adb-9cc8-7d734255cde6"
    }
}