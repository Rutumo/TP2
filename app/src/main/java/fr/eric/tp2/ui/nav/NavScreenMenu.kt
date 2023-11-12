package fr.eric.tp2.ui.nav

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.eric.tp2.ui.state.TypesNavigation

@Composable
fun ScreenMenu(navController: NavHostController, navigationType: TypesNavigation) {
    Row(modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(visible = navigationType == TypesNavigation.NAVIGATION_RAIL) {
            LeftBarNavRail(navController)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            Box(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxSize()
            ) {
                BottomNavGraph(navController = navController)
            }

            AnimatedVisibility(visible = navigationType == TypesNavigation.BOTTOM_NAVIGATION) {
                BottomBarNav(navController)
            }
        }
    }
}

@Composable
fun LeftBarNavRail(navController: NavHostController){
    val screens = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Panier,
        BottomNavScreen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationRail {
        screens.forEach{
                screen ->
            if (currentDestination != null) {
                AddItemLeft(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun AddItemLeft(
    screen: BottomNavScreen,
    currentDestination: NavDestination,
    navController: NavHostController
){
    NavigationRailItem (
        //alwaysShowLabel = true,
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
