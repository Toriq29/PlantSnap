package com.thoriq.plantsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class SuccessResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
