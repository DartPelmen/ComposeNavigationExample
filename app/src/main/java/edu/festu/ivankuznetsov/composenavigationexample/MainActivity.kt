package edu.festu.ivankuznetsov.composenavigationexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.festu.ivankuznetsov.composenavigationexample.ui.theme.ComposeNavigationExampleTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeNavigationExampleTheme {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    // NavContainer(navController = navController)
                    Scaffold(bottomBar = { NavBottomBar(navController = navController) }) {
                        NavContainer(navController = navController, padding = it)
                    }
                }
            }
        }
    }
}


val items = listOf(
    Screens.Greeting,
    Screens.Home,
    Screens.Detail
)

@Composable
fun NavBottomBar(navController: NavHostController, modifier: Modifier = Modifier) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any {
                it.route == screen.route
            } == true
            BottomNavigationItem(

                modifier = Modifier.background(if (selected) Color.Cyan else Color.Magenta),
                icon = {
                    Icon(
                        when (screen) {
                            Screens.Home -> Icons.Filled.Home
                            Screens.Detail -> Icons.Filled.Edit
                            Screens.Greeting -> Icons.Filled.Face
                        },
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        when (screen) {
                            Screens.Home -> "Дом"
                            Screens.Detail -> "Подробно"
                            Screens.Greeting -> "Привет"
                        }
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun NavContainer(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        modifier = Modifier.padding(paddingValues = padding),
        navController = navController,
        startDestination = Screens.Greeting.route
    )
    {
        composable(route = Screens.Greeting.route) {
            Greeting("screen")
        }
        composable(route = Screens.Home.route) {
            Home("screen")
        }
        composable(route = Screens.Detail.route) {
            Details("screen")
        }
    }
}

sealed class Screens(val route: String) {
    data object Greeting : Screens("greeting")
    data object Home : Screens("home")
    data object Detail : Screens("detail")
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )

}

@Composable
fun Home(name: String, modifier: Modifier = Modifier) {

    Text(
        text = "Home $name!",
        modifier = modifier
    )

}

@Composable
fun Details(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Details $name!",
        modifier = modifier
    )

}
