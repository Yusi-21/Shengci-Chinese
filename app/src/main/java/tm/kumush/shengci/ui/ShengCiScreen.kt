package tm.kumush.shengci.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tm.kumush.shengci.R
import tm.kumush.shengci.data.ShengCiUnits
import tm.kumush.shengci.data.shengCi_AllUnits
import tm.kumush.shengci.ui.theme.ShengciTheme

@Composable
fun ShengciApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route) {
            MainApp(
                onUnitClick = { unitIndex ->
                    navController.navigate("${Screen.ShengCiScreen.route}/$unitIndex")
                }
            )
        }
        composable(
            route = "${Screen.ShengCiScreen.route}/{unitId}",
            arguments = listOf(navArgument("unitId") {type = NavType.IntType})
            ) {backStackEntry ->
            val unitId = backStackEntry.arguments?.getInt("unitId") ?: 0
            val currentUnit = shengCi_AllUnits.getOrNull(unitId) ?: shengCi_AllUnits[0]

            ShengCiScreen(
                unit = currentUnit,
                unitId = unitId,
                onBackClick = {
                    navController.popBackStack()
                },
                onFuxiClicked = { unitId ->
                    navController.navigate("${Screen.FuxiScreen.route}/$unitId")
                }
            )
        }
        composable(
            route = "${Screen.FuxiScreen.route}/{unitId}",
            arguments = listOf(navArgument("unitId") { type = NavType.IntType})
            ) {backStackEntry ->
            val unitId = backStackEntry.arguments?.getInt("unitId") ?: 0
            val currentUnit = shengCi_AllUnits.getOrNull(unitId) ?: shengCi_AllUnits[0]

            FuxiScreen(
                unit = currentUnit,
                onBackClick = {
                    navController.popBackStack()
                },
                onExitToShengCi = {
                    navController.popBackStack()
                },
                onExitToMain = {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.MainScreen.route) { inclusive = true } // check it
                    }
                }
            )
        }
    }
}

@Composable
fun ShengCiScreen(
    unit: ShengCiUnits,
    unitId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onFuxiClicked: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            ShengCiTopAppBar(
                titleRes = unit.title,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier) {
                Button(
                    onClick = { onFuxiClicked(unitId) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(25.dp))
                        .border(
                            width = 2.dp,
                            color = Color.Blue,
                            shape = RoundedCornerShape(25.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.inversePrimary,
                    ),


                ) {
                    Text(modifier = modifier,
                        text = stringResource(R.string.fuxi),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(unit.words) {pair ->
                ShengCiFanYi(
                    chineseWord = pair.chinese,
                    englishWord = pair.english,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ShengCiFanYi(
    @StringRes chineseWord: Int,
    @StringRes englishWord: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 5.dp, bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary,
            ),
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
            Text(
                text = stringResource(chineseWord),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp,
            )
        }

        Spacer(modifier = Modifier.width(3.dp))

        Text(
            text = " · ",
            style = MaterialTheme.typography.headlineLarge
            )

        Spacer(modifier = Modifier.width(3.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inversePrimary,
            ),
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = stringResource(englishWord),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp),
                fontSize = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShengCiTopAppBar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.headlineLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = modifier
    )
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShengCiLightScreenPreview() {
    ShengciTheme(darkTheme = false) {
        ShengCiScreen(
            unit = shengCi_AllUnits[0],
            unitId = 0,
            onBackClick = {},
            onFuxiClicked = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShengCiDarkScreenPreview() {
    ShengciTheme(darkTheme = true) {
        ShengCiScreen(
            unit = shengCi_AllUnits[0],
            onBackClick = {},
            unitId = 0,
            onFuxiClicked = {})
    }
}