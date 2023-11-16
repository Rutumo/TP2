package fr.eric.tp2.ui.nav

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomBarNav(navController: NavHostController){
    val screens = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Gestion,
        BottomNavScreen.Panier,
        BottomNavScreen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach{
                screen ->
            if (currentDestination != null) {
                AddItemBottom(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItemBottom(
    screen: BottomNavScreen,
    currentDestination: NavDestination,
    navController: NavHostController
){
    NavigationBarItem(
        alwaysShowLabel = true,
        selected = currentDestination?.hierarchy?.any {it.route == screen.route
        } == true,
        //unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id){
                    saveState = true
                }
                launchSingleTop = true
                //restoreState = true

            }
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Icone de Navigaion"
            )
        },
        label = {
            Text(
                text = stringResource(id = screen.title)
            )
        }
    )
}