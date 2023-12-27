package mobi.lab.throwabletest.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import mobi.lab.throwabletest.MainCoroutineRule
import mobi.lab.throwabletest.common.rx.TestSchedulerProvider
import mobi.lab.throwabletest.demo.main.MainViewModel
import mobi.lab.throwabletest.domain.entities.dogfacts.DogFactsResponse
import mobi.lab.throwabletest.domain.usecases.auth.LogoutUseCase
import mobi.lab.throwabletest.domain.usecases.dogfacts.DogFactsUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var logoutUseCase: LogoutUseCase

    @MockK
    private lateinit var dogFactsUseCase: DogFactsUseCase

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MainViewModel(logoutUseCase, dogFactsUseCase, TestSchedulerProvider)
    }

    @Test
    fun `check getDogFacts onSuccess`() = runTest {
        val dogFactsResponse = mockk<DogFactsResponse>()
        coEvery { dogFactsUseCase.execute() } returns dogFactsResponse

        viewModel.getDogFacts()
        assertThat(viewModel.state.value).isEqualTo(MainViewModel.State.Loading)
        advanceUntilIdle()
        coVerify { dogFactsUseCase.execute() }
        advanceUntilIdle()
        assertThat(viewModel.state.value).isEqualTo(MainViewModel.State.Default)
    }

    @Test
    fun `check getDogFacts onFail`() = runTest {
        coEvery { dogFactsUseCase.execute() } throws mockk<Throwable>()

        viewModel.getDogFacts()
        assertThat(viewModel.state.value).isEqualTo(MainViewModel.State.Loading)
        advanceUntilIdle()
        coVerify { dogFactsUseCase.execute() }
        advanceUntilIdle()
        assertThat(viewModel.state.value).isInstanceOf(MainViewModel.State.Error::class.java)
    }
}
