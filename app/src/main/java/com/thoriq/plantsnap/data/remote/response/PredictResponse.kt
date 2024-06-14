package com.thoriq.plantsnap.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictResponse(

	@field:SerializedName("label")
	val label: String? = null
)
