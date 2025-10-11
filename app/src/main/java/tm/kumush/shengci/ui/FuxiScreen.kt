package tm.kumush.shengci.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tm.kumush.shengci.R
import tm.kumush.shengci.data.ShengCiUnits
import tm.kumush.shengci.data.StringShengCiPair
import tm.kumush.shengci.data.shengCi_AllUnits
import tm.kumush.shengci.ui.theme.ShengciTheme
import tm.kumush.shengci.ui.theme.shapes

@Composable
fun FuxiScreen(
    unit: ShengCiUnits,
    onBackClick: () -> Unit,
    onExitToMain: () -> Unit,
    onExitToShengCi: () -> Unit,
    shengCiViewModel: ShengCiViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val stringWords = remember(unit) {
        unit.words.map { pair ->
            StringShengCiPair(
                chinese = context.getString(pair.chinese),
                english = context.getString(pair.english)
            )
        }
    }

    LaunchedEffect(unit) {
        shengCiViewModel.initializeWithUnit(stringWords)
    }

    val shengCiUiState by shengCiViewModel.uiState.collectAsState()

    BackHandler(enabled = true) {
        shengCiViewModel.showExitDialog()
    }

    LaunchedEffect(shengCiUiState.isGuessedShengCiWrong) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Scaffold(
        topBar = {
            FuxiTopAppBar(
                onBackClick = {
                    shengCiViewModel.showExitDialog()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.fanyi_yixia),
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            FuxiLayout(
                onUserGuessChanged = { shengCiViewModel.updateUserGuess(it) },
                shengCiCount = shengCiUiState.currentShengCiCount,
                userGuess = shengCiViewModel.userGuess,
                onKeyboardDone = {
                    shengCiViewModel.checkUserGuess()
//                    focusManager.clearFocus()
                },
                zheGeShengCi = shengCiUiState.zheGeShengCi,
                isGuessWrong = shengCiUiState.isGuessedShengCiWrong,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(dimensionResource(R.dimen.padding_medium))
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            width = 2.dp,
                            color = Color.Green,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.surface,
                    ),
                    onClick = {
                        shengCiViewModel.checkUserGuess()
                        focusManager.clearFocus()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.submit),
                        style = typography.titleLarge,
                        color = colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            width = 2.dp,
                            color = Color.Red,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.background,
                    ),
                    onClick = {
                        shengCiViewModel.skipWord()
                        focusManager.clearFocus()

                    }
                ) {
                    Text(
                        text = stringResource(R.string.skip),
                        style = typography.titleLarge,
                        color = colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            FuxiStatus(
                score = shengCiUiState.score,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .align(Alignment.CenterHorizontally)
                )

        }
    }
    if (shengCiUiState.showExitDialog) {
        ExitConfirmationDialog(
            onDismiss = {shengCiViewModel.hideExitDialog()},
            onConfirm = {
                shengCiViewModel.exitGame {
                    onExitToShengCi()
                }
            }
        )
    }

    if (shengCiUiState.isGameOver) {
        FinalFuxiScoreDialog(
            score = shengCiUiState.score,
            guan = onExitToMain,
            fuxiZaiYiBian = { shengCiViewModel.resetGame() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuxiTopAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.fuxi),
                style = typography.headlineLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = colorScheme.onSurface
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun FuxiLayout(
    shengCiCount : Int,
    zheGeShengCi: String,
    userGuess: String,
    isGuessWrong: Boolean,
    onKeyboardDone: () -> Unit,
    onUserGuessChanged: (String) -> Unit,
    modifier : Modifier = Modifier
) {
    val totalWords = 10

    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.inversePrimary,
        ),
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                modifier = Modifier
                    .clip(shapes.medium)
                    .background(colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(Alignment.End),
                text = stringResource(R.string.shengCi_count, shengCiCount, totalWords),
                style = typography.titleMedium,
                color = colorScheme.onPrimary
            )

            Text(
                text = zheGeShengCi,
                style = typography.displayMedium
            )

            OutlinedTextField(
                value = userGuess,
                singleLine = true,
                shape = shapes.large,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface
                ),
                onValueChange =onUserGuessChanged,
                label = {
                    if (isGuessWrong) {
                        Text(stringResource(R.string.budui))
                    } else {
                        Text(stringResource(R.string.tianxie))
                    }
                },
                isError = isGuessWrong,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onKeyboardDone() }
                )
            )
        }
    }
}

@Composable
fun FuxiStatus(
    score: Int,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.inversePrimary),
        modifier = modifier) {
        Text(
            text = stringResource(R.string.score, score),
            style = typography.headlineMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun FinalFuxiScoreDialog(
    score: Int,
    guan: () -> Unit,
    fuxiZaiYiBian: () -> Unit,
    modifier: Modifier = Modifier
) {

    AlertDialog(
        onDismissRequest = {},
        containerColor = colorScheme.onPrimary,
        titleContentColor = colorScheme.onBackground,
        textContentColor = colorScheme.onBackground,
        title = {
            Text(
                text = stringResource(R.string.zhuheni),
                style = typography.headlineMedium
            )
                },
        text = {
            Text(
                text = stringResource(R.string.nidechengji, score),
                style = typography.titleLarge
            )
               },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = guan
            ) {
                Text(
                    text = stringResource(R.string.guan),
                    color = Color.Red,
                    style = typography.titleMedium
                    )
            }
        },
        confirmButton = {
            TextButton(
                onClick = fuxiZaiYiBian
            ) { Text(
                text = stringResource(R.string.zaiyibian),
                color = Color.Green,
                style = typography.titleMedium
                )
            }
        }
    )
}

@Composable
fun ExitConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colorScheme.inversePrimary,
        titleContentColor = colorScheme.onBackground,
        textContentColor = colorScheme.onBackground,
        title = {
            Text(
                text = stringResource(R.string.tuichu),
                style = typography.headlineMedium
            )
        },
        text = {
            Text(
                text = stringResource(R.string.tuichi_mesg),
                style = typography.titleLarge
            )
        },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Green
                )
            ) {
                Text(
                    text = stringResource(R.string.bu),
                    style = typography.titleMedium
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.Red
                )
            ) {
                Text(
                    text = stringResource(R.string.dui_correct),
                    style = typography.titleMedium
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FuxiScreenLightThemePreview() {
    ShengciTheme(darkTheme = false) {
        FuxiScreen(
            onBackClick = {},
            unit = shengCi_AllUnits[0],
            onExitToMain = {},
            onExitToShengCi = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FuxiScreenDarkThemePreview() {
    ShengciTheme(darkTheme = true) {
        FuxiScreen(
            onBackClick = {},
            unit = shengCi_AllUnits[0],
            onExitToMain = {},
            onExitToShengCi = {}
        )
    }
}