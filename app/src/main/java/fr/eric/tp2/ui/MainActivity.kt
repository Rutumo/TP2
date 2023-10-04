package fr.eric.tp2.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.eric.tp2.ui.nav.ScreenMenu
import fr.eric.tp2.ui.theme.TP2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TP2Theme {
                ScreenMenu()
            }
        }
    }
}
