package com.ngengeapps.easymomo.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext:Context):AccountDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            AccountDatabase::class.java,
            "account_database"
        )
            .fallbackToDestructiveMigration()
            .build()

    }

    @Provides
    fun provideDao(appDatabase:AccountDatabase):AccountDao = appDatabase.accountDao

}