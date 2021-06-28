package com.ngengeapps.easymomo.database

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideGson():Gson = Gson()

    @Provides
    @Singleton
    fun providesPreferences(@ApplicationContext context: Context):SharedPreferences = context.getSharedPreferences("my_account",MODE_PRIVATE)
}