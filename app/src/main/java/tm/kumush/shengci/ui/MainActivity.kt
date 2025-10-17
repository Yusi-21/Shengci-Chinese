package tm.kumush.shengci.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tm.kumush.shengci.R
import tm.kumush.shengci.data.ShengCiData
import tm.kumush.shengci.data.units
import tm.kumush.shengci.ui.theme.ShengciTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ShengciTheme {
                Surface(modifier = Modifier
                    .fillMaxSize()
                ) {
                    ShengciApp()
                }
            }
        }
    }
}

@Composable
fun MainApp(
    onUnitClick: (Int) -> Unit
) {

    Scaffold(
        topBar = {
            MainTopAppBar()
        }
    ) { it ->
        LazyColumn(contentPadding = it) {
            itemsIndexed(units) {index, unit ->
                MainItem(
                    unit = unit,
                    modifier = Modifier.padding(8.dp),
                    onUnitClick = {
                        onUnitClick(index)
                    }
                )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .padding(8.dp)
                        .clip(CircleShape)
                        .border(
                                width = 1.dp,
                                color = Color.Green,
                                shape = RoundedCornerShape(50.dp)
                            ),
                    painter = painterResource(R.drawable.icon_panda_main),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = stringResource(R.string.hanyujiaocheng),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun MainTitle(
    @StringRes shengCiTitle: Int,
) {
    Column(modifier = Modifier,
        verticalArrangement = Arrangement.Center) {
        Text(
            stringResource(shengCiTitle),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(12.dp),

        )
    }
}

@Composable
fun MainItemButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.NavigateNext,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun MainItem(
    unit: ShengCiData,
    modifier: Modifier = Modifier,
    onUnitClick: () -> Unit
) {

    val color by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.inversePrimary
    )

    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                ))
                .background(color = color)
        ) {
            MainTitle(unit.title)
            Spacer(Modifier.weight(1f))
            MainItemButton(onClick = onUnitClick)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainLightThemePreview() {
    ShengciTheme(darkTheme = false) {
        MainApp(onUnitClick = {})
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainDarkThemePreview() {
    ShengciTheme(darkTheme = true) {
        MainApp(onUnitClick = {})
    }
}
