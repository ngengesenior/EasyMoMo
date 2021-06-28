package com.ngengeapps.easymomo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.i18n.phonenumbers.PhoneNumberUtil

fun Context.openSettings() {

    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package",packageName,null)
    intent.data = uri
    startActivity(intent)
}

fun convertToLocalNumber(number:String):String {
    val phoneNumberUtil = PhoneNumberUtil.getInstance()
    var camerPhone = phoneNumberUtil.parse(number,"CM").nationalNumber.toString()
     // Some people might save numbers like Whatsapp without leading 6. I will check the length and append 6
    if (camerPhone.length == 8) {
        camerPhone = "6$camerPhone"
    }
    return camerPhone

}
