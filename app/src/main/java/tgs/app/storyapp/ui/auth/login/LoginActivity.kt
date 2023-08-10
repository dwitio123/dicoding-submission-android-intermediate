package tgs.app.storyapp.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import tgs.app.storyapp.viewModelFactory.ViewModelFactory
import tgs.app.storyapp.databinding.ActivityLoginBinding
import tgs.app.storyapp.model.LoginResult
import tgs.app.storyapp.preference.UserPreference
import tgs.app.storyapp.ui.auth.register.RegisterActivity
import tgs.app.storyapp.ui.story.StoryActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginResult: LoginResult
    private lateinit var mUserPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mUserPreference = UserPreference(this)

        showExistingPreference()

        loginViewModel = obtainLoginViewModel(this@LoginActivity)
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val pass = binding.editPass.text.toString()
            if (email.isEmpty()) {
                binding.editEmail.error = "Please fill email"
            } else if (pass.isEmpty()) {
                binding.editPass.error = "Please fill password"
            } else {
                loginViewModel.loginUser(email, pass)
                loginViewModel.loginResponse.observe(this) { loginUser ->
                    if (loginUser.error == false) {
                        saveUser(loginUser.loginResult?.token.toString())
                        startActivity(Intent(this, StoryActivity::class.java))
                        finish()
                    }
                }
            }
        }

        binding.btnNewAccount.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext,
                    RegisterActivity::class.java
                )
            )
        }

        playAnimation()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainLoginViewModel(activity: AppCompatActivity): LoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }

    private fun showExistingPreference() {
        loginResult = mUserPreference.getUser()
        checkForm(loginResult)
    }

    private fun checkForm(loginResult: LoginResult) {
        when {
            loginResult.token.toString().isNotEmpty() -> {
                startActivity(Intent(this, StoryActivity::class.java))
                finish()
            }
        }
    }

    private fun saveUser(token: String) {
        val userPreference = UserPreference(this)

        loginResult.token = token

        userPreference.setUser(loginResult)
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.editEmail, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.editPass, View.ALPHA, 1f).setDuration(500)
        val btnSignIn = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 1f).setDuration(500)
        val btnNewAccount = ObjectAnimator.ofFloat(binding.btnNewAccount, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, email, pass, btnSignIn, btnNewAccount)
            startDelay = 500
        }.start()
    }
}