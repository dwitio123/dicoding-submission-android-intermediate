package tgs.app.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import tgs.app.storyapp.adapter.LoadingStateAdapter
import tgs.app.storyapp.R
import tgs.app.storyapp.adapter.StoryAdapter
import tgs.app.storyapp.databinding.ActivityMainBinding
import tgs.app.storyapp.model.ListStoryItem
import tgs.app.storyapp.model.LoginResult
import tgs.app.storyapp.preference.UserPreference
import tgs.app.storyapp.ui.addStory.AddStoryActivity
import tgs.app.storyapp.ui.auth.login.LoginActivity
import tgs.app.storyapp.ui.maps.MapsActivity

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private lateinit var loginResult: LoginResult
    private val storyViewModel: StoryViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        mUserPreference = UserPreference(this)
        loginResult = mUserPreference.getUser()

        storyViewModel.story(loginResult.token.toString()).observe(this) {
            lifecycleScope.launch {
                setItemsData(it)
            }
        }

        binding.btnAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        supportActionBar?.title = getString(R.string.story)
    }

    private suspend fun setItemsData(listStoryItem: PagingData<ListStoryItem>) {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.submitData(lifecycle, listStoryItem)
        adapter.loadStateFlow.collect {
            if (it.prepend is LoadState.NotLoading && it.prepend.endOfPaginationReached) {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        storyViewModel.story(loginResult.token.toString()).observe(this) {
            lifecycleScope.launch {
                setItemsData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                mUserPreference.deleteUser()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}