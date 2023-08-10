package tgs.app.storyapp.ui.detailStory

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import tgs.app.storyapp.viewModelFactory.ViewModelFactory
import tgs.app.storyapp.databinding.ActivityDetailStoryBinding
import tgs.app.storyapp.model.LoginResult
import tgs.app.storyapp.model.Story
import tgs.app.storyapp.preference.UserPreference

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var detailStoryViewModel: DetailStoryViewModel
    private lateinit var loginResult: LoginResult
    private lateinit var mUserPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        mUserPreference = UserPreference(this)
        loginResult = mUserPreference.getUser()

        detailStoryViewModel = obtainViewModel(this@DetailStoryActivity)
        detailStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        val id = intent.extras?.getString(DETAIL_STORY)
        detailStoryViewModel.detailStory(loginResult.token.toString(), id.toString())
        detailStoryViewModel.detailStoryItem.observe(this) {
            setItemsData(it)
        }

        supportActionBar?.title = "Detail Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setItemsData(it: Story?) {
        binding.apply {
            tvDetailName.text = it?.name
            tvDetailDescription.text = it?.description
            Glide.with(applicationContext).load(it?.photoUrl).into(ivDetailPhoto)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailStoryViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailStoryViewModel::class.java]
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}