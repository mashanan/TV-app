package com.example.tv_app.list

import com.example.tv_app.data.model.Show
import com.example.tv_app.ui.common.UiState
import com.example.tv_app.ui.list.ShowListViewModel
import com.example.tv_app.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShowListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sampleShows = listOf(
        Show(id = 1, name = "Show One"),
        Show(id = 2, name = "Show Two")
    )

    @Test
    fun `uiState becomes Success with shows when repository succeeds`() = runTest {
        val viewModel = ShowListViewModel(repository = FakeShowRepository(shows = sampleShows))

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(sampleShows, (state as UiState.Success).data)
    }

    @Test
    fun `uiState becomes Error when repository throws`() = runTest {
        val viewModel = ShowListViewModel(
            repository = FakeShowRepository(error = IllegalStateException("network down"))
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("network down", (state as UiState.Error).message)
    }
}
