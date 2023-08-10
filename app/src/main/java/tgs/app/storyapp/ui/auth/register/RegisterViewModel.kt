package tgs.app.storyapp.ui.auth.register

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
import tgs.app.storyapp.model.RegisterResponse

class RegisterViewModel(val application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse

    fun registerUser(name: String?, email: String?, password: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postRegister(name.toString(), email.toString(), password.toString())
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _registerResponse.value = response.body()
                    Toast.makeText(application, response.body()?.message, Toast.LENGTH_SHORT).show()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = errorBody?.let {
                        JSONObject(it).getString("message")
                    }
                    Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(application, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}