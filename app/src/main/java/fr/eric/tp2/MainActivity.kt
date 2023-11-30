package fr.eric.tp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.WindowInfoTracker
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import fr.eric.tp2.ui.nav.AdaptiveScreenMenu
import fr.eric.tp2.ui.state.DevicePosture
import fr.eric.tp2.ui.state.TypesNavigation
import fr.eric.tp2.ui.state.getDevicePostureFlow
import fr.eric.tp2.ui.theme.TP2Theme
import fr.eric.tp2.workers.UpdateWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TP2Theme {
                val windowSize = calculateWindowSizeClass(this).widthSizeClass
                val devicePosture = getDevicePosture().collectAsState().value
                val navigationType = getNavigationType(windowSize, devicePosture)
                val navController = rememberNavController()

                AdaptiveScreenMenu(navigationType, navController)

            }
        }
    }

    override fun onResume() {
        super.onResume()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicRequest = PeriodicWorkRequest.Builder(
            UpdateWorker::class.java,
            15,
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .addTag("UpdateWorker")
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "UpdateWorker",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
    }

    private fun getDevicePosture(): StateFlow<DevicePosture> {
        return WindowInfoTracker.getOrCreate(this)
            .getDevicePostureFlow(this, lifecycle)
            .stateIn(
                scope = lifecycleScope,
                started = SharingStarted.Eagerly,
                initialValue = DevicePosture.NormalPosture
            )
    }

    private fun getNavigationType(
        windowSize: WindowWidthSizeClass,
        devicePosture: DevicePosture
    ): TypesNavigation {
        val navigationType: TypesNavigation

        when (windowSize) {
            WindowWidthSizeClass.Medium -> {
                navigationType = TypesNavigation.NAVIGATION_RAIL
            }

            WindowWidthSizeClass.Expanded -> {
                navigationType = if (devicePosture is DevicePosture.BookPosture) {
                    TypesNavigation.NAVIGATION_RAIL
                } else {
                    TypesNavigation.PERMANENT_NAVIGATION_DRAWER
                }
            }

            else -> {
                navigationType = TypesNavigation.BOTTOM_NAVIGATION
            }
        }
        return navigationType
    }

}


@Preview(showBackground = true, widthDp = 600 )
@Composable
fun CompactScreenPreview() {
    TP2Theme {
    val navController = rememberNavController()
    AdaptiveScreenMenu(TypesNavigation.BOTTOM_NAVIGATION, navController)
    }
}

@Preview(showBackground = true, widthDp = 800 )
@Composable
fun MediumScreenPreview() {
    TP2Theme {
    val navController = rememberNavController()
    AdaptiveScreenMenu(TypesNavigation.NAVIGATION_RAIL, navController)
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun ExpandedScreenPreview() {
    TP2Theme {
    val navController = rememberNavController()
    AdaptiveScreenMenu(TypesNavigation.PERMANENT_NAVIGATION_DRAWER, navController)
    }
}

