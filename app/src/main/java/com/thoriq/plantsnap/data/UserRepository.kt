package com.thoriq.plantsnap.data

import com.thoriq.plantsnap.data.pref.UserModel
import com.thoriq.plantsnap.data.pref.UserPreference
import com.thoriq.plantsnap.data.remote.response.SuccessResponse
import com.thoriq.plantsnap.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference

) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun register(username: String, password: String): Response<SuccessResponse> {
        return apiService.register(username, password)
    }
    suspend fun login(username: String, password: String): Response<SuccessResponse> {
        return apiService.login(username, password)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService , userPreference)
            }.also { instance = it }
    }
}