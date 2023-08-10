package tgs.app.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import tgs.app.storyapp.data.StoryRepository
import tgs.app.storyapp.di.Injection.provideRepository
import tgs.app.storyapp.model.ListStoryItem

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun story(token: String) : LiveData<PagingData<ListStoryItem>> =
        storyRepository.getAllStories(token).cachedIn(viewModelScope)
}

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(provideRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
