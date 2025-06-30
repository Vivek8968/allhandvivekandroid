package com.hyperlocal.marketplace

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HyperlocalMarketplaceApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}