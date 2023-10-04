package fr.eric.tp2.ui.nav

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import fr.eric.tp2.R

sealed class BottomNavScreen(
    val route: String,
    val title: Int,
    val icon: ImageVector
) {
    object Home : BottomNavScreen(
        route = "home",
        title = R.string.home,
        icon = Icons.Default.Home
    )

    object Panier : BottomNavScreen(
        route = "panier",
        title = R.string.basket,
        icon = Icons.Default.ShoppingCart
    )

    object Profile : BottomNavScreen(
        route = "profile",
        title = R.string.profile,
        icon = Icons.Default.Person
    )

}
