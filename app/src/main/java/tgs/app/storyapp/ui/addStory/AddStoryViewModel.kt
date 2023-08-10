package tgs.app.storyapp.ui.addStory

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tgs.app.storyapp.api.ApiConfig
import tgs.app.storyapp.model.AddStoryResponse

class AddStoryViewModel(val application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _addStoryResponse = MutableLiveData<AddStoryResponse>()
    val addStoryResponse: LiveData<AddStoryResponse> = _addStoryResponse

    fun addStory(token: String, photo: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val authToken = ("Bearer $token")
        val client = ApiConfig.getApiService().postStory(authToken, photo, description)
        client.enqueue(object : Callback<AddStoryResponse> {
            override fun onResponse(
                call: Call<AddStoryResponse>,
                response: Response<AddStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _addStoryResponse.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = errorBody?.let {
                        JSONObject(it).getString("message")
                    }
                    Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                Toast.makeText(application, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}