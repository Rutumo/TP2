package fr.eric.tp2.ui.state

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun WindowInfoTracker.getDevicePostureFlow(
    context: Context,
    lifecycle: Lifecycle
): Flow<DevicePosture> {
    return windowLayoutInfo(context)
        .flowWithLifecycle(lifecycle)
        .map { layoutInfo ->
            val foldingFeature =
                layoutInfo.displayFeatures
                    .filterIsInstance<FoldingFeature>()
                    .firstOrNull()
            when {
                isTableTopPosture(foldingFeature) ->
                    DevicePosture.TableTopPosture(foldingFeature.bounds)

                isBookPosture(foldingFeature) ->
                    DevicePosture.BookPosture(foldingFeature.bounds)

                isSeparating(foldingFeature) ->
                    DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

                else -> DevicePosture.NormalPosture
            }
        }

}