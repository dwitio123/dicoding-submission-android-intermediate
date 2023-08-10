package tgs.app.storyapp.di

import tgs.app.storyapp.api.ApiConfig
import tgs.app.storyapp.data.StoryRepository

object Injection {
    fun provideRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)
    }
}