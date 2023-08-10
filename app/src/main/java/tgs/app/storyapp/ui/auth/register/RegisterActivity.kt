package tgs.app.storyapp.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import tgs.app.storyapp.viewModelFactory.ViewModelFactory
import tgs.app.storyapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = obtainRegisterViewModel(this@RegisterActivity)
        registerViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnSignUp.setOnClickListener {
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            val pass = binding.editPass.text.toString()

            if (name.isEmpty()) {
                binding.editName.error = "Please fill name"
            } else if (email.isEmpty()) {
                binding.editEmail.error = "Please fill email"
            } else if (pass.isEmpty()) {
                binding.editPass.error = "Please fill password"
            } else {
                registerViewModel.registerUser(name, email, pass)
                registerViewModel.registerResponse.observe(this) { registerUser ->
                    if (registerUser.message == "User created") {
                        finish()
                    }
                }
            }
        }

        playAnimation()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun obtainRegisterViewModel(activity: AppCompatActivity): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[RegisterViewModel::class.java]
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.editName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.editEmail, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.editPass, View.ALPHA, 1f).setDuration(500)
        val btnSignUp = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, name, email, pass, btnSignUp)
            startDelay = 500
        }.start()
    }
}