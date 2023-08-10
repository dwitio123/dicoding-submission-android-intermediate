package tgs.app.storyapp.ui.detailStory

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tgs.app.storyapp.api.ApiConfig
import tgs.app.storyapp.model.DetailStoryResponse
import tgs.app.storyapp.model.Story

class DetailStoryViewModel(val application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailStory = MutableLiveData<Story>()
    val detailStoryItem: LiveData<Story> = _detailStory

    fun detailStory(token: String, id: String) {
        _isLoading.value = true
        val authToken = ("Bearer $token")
        val client = ApiConfig.getApiService().getDetailStory(authToken, id)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailStory.value = response.body()?.story
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = errorBody?.let {
                        JSONObject(it).getString("message")
                    }
                    Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Toast.makeText(application, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}