package com.ngengeapps.easymomo.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "account")
data class Account(

    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val name:String,
    val number:String
):Parcelable {
    fun trimAccount() = TrimmedAccount(name, number)
}

@Parcelize
data class TrimmedAccount(
    val name:String,
    val number:String
):Parcelable