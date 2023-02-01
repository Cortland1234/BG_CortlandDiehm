package edu.arizona.cast.cortlanddiehm.bloodglucosemonitor_cortlanddiehm

import android.app.Application

class GlucoseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlucoseRepository.initialize(this)
    }
}