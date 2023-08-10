package tgs.app.storyapp.model

import com.google.gson.annotations.SerializedName

data class DetailStoryResponse(

	@field:SerializedName("story")
	val story: Story? = null
)

data class Story(

	@field:SerializedName("photoUrl")
	val photoUrl: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null

)
