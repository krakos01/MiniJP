package com.krakos.minijp.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.krakos.minijp.MiniJpApplication
import com.krakos.minijp.data.DataRepository
import com.krakos.minijp.data.FileUtils
import com.krakos.minijp.model.Items
import com.krakos.minijp.model.Word
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MiniJpUiState {
    data class SuccessHomeScreen(val words: Items): MiniJpUiState
    data class SuccessDetailsScreen(val word: Word): MiniJpUiState
    object Error: MiniJpUiState
    object Loading: MiniJpUiState
}

class MiniJpViewModel(private val dataRepository: DataRepository): ViewModel() {

    // SuccessHomeScreen is default - app don't load words immediately after launching
    var miniJpUiState:
            MiniJpUiState by mutableStateOf(MiniJpUiState.SuccessHomeScreen(Items(listOf())))
        private set


    fun getDetails(item: Word) {
        viewModelScope.launch {
            miniJpUiState = MiniJpUiState.Loading
            try {
                miniJpUiState = MiniJpUiState.SuccessDetailsScreen(item)
            } catch (e: NullPointerException) {
                miniJpUiState = MiniJpUiState.Error
                Log.e("ui_state_error", e.toString())
            }
        }
    }

    fun getWords(context: Context, query: String) {
        viewModelScope.launch {
            miniJpUiState = MiniJpUiState.Loading
            try {
                miniJpUiState = MiniJpUiState.SuccessHomeScreen(dataRepository.allWordsData(query))
                FileUtils.appendSearch(context, query)
            } catch (e: IOException) {
                miniJpUiState = MiniJpUiState.Error
                Log.e("ui_state_error", e.toString())
            } catch (e: HttpException) {
                miniJpUiState = MiniJpUiState.Error
                Log.e("ui_state_error", e.toString())
            }
        }
    }

    /**
     * Tries getting data using last used query, if there is no last query then nothing happens.
     */
    fun retrySearch(context: Context) {
        val lastQuery: String? = FileUtils.getLastSearch(context)
        if (!lastQuery.isNullOrEmpty()) {
            getWords(context, lastQuery)
        }
    }


    fun isSearchFileEmpty(context: Context) = FileUtils.getSearches(context).isEmpty()


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as MiniJpApplication
                val dataRepository = application.container.dataRepository
                MiniJpViewModel(dataRepository = dataRepository)
            }
        }
    }
}