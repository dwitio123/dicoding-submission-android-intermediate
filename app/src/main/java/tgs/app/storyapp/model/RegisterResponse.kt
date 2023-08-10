package tgs.app.storyapp.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("message")
	var message: String? = null
)
