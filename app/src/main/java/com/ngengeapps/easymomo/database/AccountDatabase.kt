package com.ngengeapps.easymomo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ngengeapps.easymomo.models.Account

@Database(version = 1,exportSchema = false,entities = [Account::class])
abstract class AccountDatabase: RoomDatabase() {

    abstract val accountDao: AccountDao

}