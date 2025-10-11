package tm.kumush.shengci.ui

data class ShengCiUiState(
    val zheGeShengCi: String = "",
    val currentShengCiCount: Int = 1,
    val score: Int = 0,
    val isGuessedShengCiWrong: Boolean = false,
    val isGameOver: Boolean = false,
    val showExitDialog: Boolean = false
)