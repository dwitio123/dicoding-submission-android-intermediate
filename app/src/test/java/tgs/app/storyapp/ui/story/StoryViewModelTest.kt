package tgs.app.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import tgs.app.storyapp.adapter.StoryAdapter
import tgs.app.storyapp.data.StoryRepository
import tgs.app.storyapp.model.ListStoryItem
import tgs.app.storyapp.utils.DataDummy
import tgs.app.storyapp.utils.getOrAwaitValue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var storyViewModel: StoryViewModel

    @Mock
    private lateinit var storyRepository: StoryRepository
    private val dummyToken = DataDummy.generateDummyToken()
    private val dummyStory = DataDummy.generateDummyStoryEntity()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        storyViewModel = StoryViewModel(storyRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when Get ListStory Should Not Null and Return Data`() = runTest {
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = PagingData.from(dummyStory)
        `when`(storyRepository.getAllStories(dummyToken)).thenReturn(expectedStory)
        val actualStory: PagingData<ListStoryItem> = storyViewModel.story(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

    @Test
    fun `when Get ListStory Empty Should Return Zero Data`() = runTest {
        val expectedStory = MutableLiveData<PagingData<ListStoryItem>>()
        expectedStory.value = PagingData.from(emptyList())
        `when`(storyRepository.getAllStories(dummyToken)).thenReturn(expectedStory)
        val actualStory: PagingData<ListStoryItem> = storyViewModel.story(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)
        assertEquals(0, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}