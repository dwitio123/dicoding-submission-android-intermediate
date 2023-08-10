package tgs.app.storyapp.ui.maps

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
import tgs.app.storyapp.model.ListStoryItem
import tgs.app.storyapp.model.StoryResponse

class MapsViewModel(val application: Application) : ViewModel() {

    private val _listStoryItem = MutableLiveData<List<ListStoryItem>>()
    val listStoryItem: LiveData<List<ListStoryItem>> = _listStoryItem

        fun mapsUser(token: String) {
        val authToken = ("Bearer $token")
        val client = ApiConfig.getApiService().getMaps(authToken)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful) {
                    _listStoryItem.value = response.body()?.listStory
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = errorBody?.let {
                        JSONObject(it).getString("message")
                    }
                    Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Toast.makeText(application, t.message.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }
}