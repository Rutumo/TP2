package fr.eric.tp2.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.eric.tp2.ui.screens.HomeScreen
import fr.eric.tp2.ui.screens.PanierScreen
import fr.eric.tp2.ui.screens.ProfileScreen

@Composable
fun BottomNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Home.route
    ){
        composable(
            route = BottomNavScreen.Home.route
        ){
            HomeScreen()
        }

        composable(
            route = BottomNavScreen.Panier.route
        ){
            PanierScreen()
        }

        composable(
            route = BottomNavScreen.Profile.route
        ){
            ProfileScreen()
        }
    }
}