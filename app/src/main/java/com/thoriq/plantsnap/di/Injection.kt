package com.thoriq.plantsnap.di

import android.content.Context
import com.thoriq.plantsnap.data.UserRepository
import com.thoriq.plantsnap.data.pref.UserPreference
import com.thoriq.plantsnap.data.pref.dataStore
import com.thoriq.plantsnap.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService, pref)
    }
}