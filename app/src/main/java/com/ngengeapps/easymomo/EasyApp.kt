package com.ngengeapps.easymomo

import android.app.Application
import com.google.i18n.phonenumbers.PhoneNumberUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EasyApp:Application() {

    override fun onCreate() {
        super.onCreate()
        PhoneNumberUtil.init(this.applicationContext)
    }

}