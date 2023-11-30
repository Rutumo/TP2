package fr.eric.tp2.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import fr.eric.tp2.MainActivity
import fr.eric.tp2.Model.User
import fr.eric.tp2.Model.UserDao
import fr.eric.tp2.R
import fr.eric.tp2.repository.UserRepository
import javax.inject.Inject

class UpdateWorker @Inject constructor(
    context: Context,
    parameters: WorkerParameters,
    private val userRepository: UserRepository
) : SensorEventListener, CoroutineWorker(context, parameters) {

    /*private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager*/

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastUpdateTime: Long = 0
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f

    private val SHAKE_THRESHOLD = 800


    override suspend fun doWork(): Result {
        Log.d("success",
            "doWork: Success function called")

        insertAutoDB()
        createForegroundInfo()
        sensorManager.unregisterListener(this)
        return Result.success()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val currentTime = System.currentTimeMillis()
            val timeDifference = currentTime - lastUpdateTime

            if (timeDifference > 100) { // Minimum time interval between shake events
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val deltaX = x - lastX
                val deltaY = y - lastY
                val deltaZ = z - lastZ

                val acceleration = Math.sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble())
                    .toFloat()

                if (acceleration > SHAKE_THRESHOLD) {
                    Log.d("ShakeDetector", "Device is shaking!")

                    lastX = x
                    lastY = y
                    lastZ = z
                    lastUpdateTime = currentTime
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        when (sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                when (accuracy) {
                    SensorManager.SENSOR_STATUS_UNRELIABLE -> {
                        Log.d("SensorAccuracy", "Accelerometer accuracy is unreliable")
                    }
                    SensorManager.SENSOR_STATUS_ACCURACY_LOW -> {
                        Log.d("SensorAccuracy", "Low accuracy in accelerometer data")
                    }
                    SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                        Log.d("SensorAccuracy", "Medium accuracy in accelerometer data")
                    }
                    SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                        Log.d("SensorAccuracy", "High accuracy in accelerometer data")
                    }
                }
            }
            else -> {
                Log.d("SensorAccuracy", "Accuracy changed for an unsupported sensor type")
            }
        }
    }

    private suspend fun insertAutoDB() {
        try {
            userRepository.getNewUser()
        } catch (e: Exception) {
            e.printStackTrace()
            e.localizedMessage
            Result.failure()
        }
    }


    private fun createForegroundInfo(): ForegroundInfo {

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        var notificationBuilder: NotificationCompat.Builder

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
            notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        }
        else {
            notificationBuilder = NotificationCompat.Builder(applicationContext)
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.baseline_security_update_24)
            .setContentTitle("Adding new user")
            .setContentText("Subscribe on the channel")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setForegroundServiceBehavior(FOREGROUND_SERVICE_IMMEDIATE)
            .setOngoing(true)
            .build()

        return ForegroundInfo(
            NOTIFICATION_ID,
            notification,
            ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(){
        val channelName = "Channel Name"
        val channelDescription = "Channel Description"
        val channelImportance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
            description = channelDescription
        }

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
    }


}