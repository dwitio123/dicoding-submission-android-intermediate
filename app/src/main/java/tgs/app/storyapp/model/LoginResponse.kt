package tgs.app.storyapp.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null

)

data class LoginResult(

    @field:SerializedName("token")
	var token: String? = null
)
