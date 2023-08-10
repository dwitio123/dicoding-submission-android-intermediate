package tgs.app.storyapp.ui.auth.login

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
import tgs.app.storyapp.model.LoginResponse

class LoginViewModel(val application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    fun loginUser(email: String?, password: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postLogin(email.toString(), password.toString())
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _loginResponse.value = response.body()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = errorBody?.let {
                        JSONObject(it).getString("message")
                    }
                    Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(application, t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}