package tm.kumush.shengci.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import tm.kumush.shengci.data.StringShengCiPair

class ShengCiViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ShengCiUiState())
    val uiState: StateFlow<ShengCiUiState> = _uiState.asStateFlow()

    private var currentUnitWords: List<StringShengCiPair> = emptyList()
    private var remainingWords: MutableList<StringShengCiPair> = mutableListOf()
    private var currentWord: StringShengCiPair? = null

    var userGuess by mutableStateOf("")
        private set

    fun initializeWithUnit(unitWords: List<StringShengCiPair>) {
        currentUnitWords = if (unitWords.size > 10) {
            unitWords.shuffled().take(10)
        } else {
            unitWords.shuffled()
        }
        resetGame()
    }

    fun resetGame() {
        remainingWords.clear()
        remainingWords.addAll(currentUnitWords.shuffled())
        userGuess = ""
        _uiState.value = ShengCiUiState(
            score = 0,
            currentShengCiCount = 1,
            isGameOver = false,
            isGuessedShengCiWrong = false
        )
        nextWord()
    }

    private fun nextWord() {
        if (remainingWords.isNotEmpty()) {
            currentWord = remainingWords.removeAt(0)
            _uiState.update { currentState ->
                currentState.copy(
                    zheGeShengCi = currentWord?.chinese ?: "",
                    currentShengCiCount = currentUnitWords.size - remainingWords.size,
                    isGuessedShengCiWrong = false
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGameOver = true)
            }
        }
    }

    fun checkUserGuess() {
        val normalizedGuess = userGuess
            .replace(Regex("[\\s\\p{Z}]"), "")
            .lowercase()

        val normalizedAnswer = currentWord?.english
            ?.replace(Regex("[\\s\\p{Z}]"), "")
            ?.lowercase()

        val isCorrect = normalizedGuess == normalizedAnswer

        if (isCorrect) {
            _uiState.update { currentState ->
                currentState.copy(
                    score = currentState.score + 10,
                    isGuessedShengCiWrong = false
                )
            }
            userGuess = ""
            nextWord()
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedShengCiWrong = true)
            }
        }
    }

    fun skipWord() {
        userGuess = ""
        nextWord()
    }

    fun updateUserGuess(guess: String) {
        userGuess = guess
    }

    fun showExitDialog() {
        _uiState.update { currentState ->
            currentState.copy(showExitDialog = true)
        }
    }

    fun hideExitDialog() {
        _uiState.update { currentState ->
            currentState.copy(showExitDialog = false)
        }
    }

    fun exitGame(onExit: () -> Unit) {
        hideExitDialog()
        onExit()
    }
}
