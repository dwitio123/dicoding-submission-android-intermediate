package tgs.app.storyapp.utils

import tgs.app.storyapp.model.ListStoryItem

object DataDummy {
    fun generateDummyToken(): String {
        return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUtSWTlTY0czSjFNSHVfcTAiLCJpYXQiOjE2ODcwMDMxNDV9.2txwcDI1obgrS6KJV7BXZs_hALhtSWZIfJcsRvNQr7Q"
    }

    fun generateDummyStoryEntity(): List<ListStoryItem> {
        val storyList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1687475220455_gU30NWaZ.jpg",
                "TGS",
                "id $i",
                107.7812007,
                -6.9337875,
            )
            storyList.add(story)
        }
        return storyList
    }
}