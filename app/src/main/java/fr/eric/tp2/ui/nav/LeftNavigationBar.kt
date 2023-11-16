package fr.eric.tp2.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.eric.tp2.R
import fr.eric.tp2.ui.state.TypesNavigation
import fr.eric.tp2.ui.theme.md_theme_light_outlineVariant
import fr.eric.tp2.ui.theme.md_theme_light_primary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveScreenMenu(
    navigationType: TypesNavigation,
    navController: NavHostController
) {
    if (navigationType == TypesNavigation.PERMANENT_NAVIGATION_DRAWER) {

        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet {
                    LeftBarNavDrawer(navController = navController)
                }
            }
        ) {
            ScreenMenu(navController = navController, navigationType = navigationType)
        }
    } else {
        ScreenMenu(navController = navController, navigationType = navigationType)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeftBarNavDrawer(
    navController: NavHostController,
    modifier: Modifier =  Modifier
){
    val screens = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Gestion,
        BottomNavScreen.Panier,
        BottomNavScreen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(
        modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .background(color = md_theme_light_outlineVariant)
            .padding(all = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = typography.bodyMedium,
            color = md_theme_light_primary,
            modifier = modifier.padding(bottom = 16.dp)
        )

        screens.forEach {
            screen ->
            NavigationDrawerItem (
                selected = currentDestination?.hierarchy?.any {it.route == screen.route
                } == true,
                colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
                onClick = {
                    navController.navigate(screen.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true

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
                        text = stringResource(id = screen.title),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            )
        }

    }
}