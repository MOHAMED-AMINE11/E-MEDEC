package ma.ensa.e_gestionnairemedec.worker

import android.app.NotificationManager
import android.content.Context
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import ma.ensa.e_gestionnairemedec.MainActivity
import ma.ensa.e_gestionnairemedec.R

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // Récupérer les données de l'entrée
        val title = inputData.getString("title") ?: "Rappel"
        val description = inputData.getString("description") ?: "Vous avez un rappel !"

        // Créer l'intent pour ouvrir l'activité
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Construire la notification
        val builder = NotificationCompat.Builder(applicationContext, "RAPPEL_CHANNEL")
            .setSmallIcon(R.drawable.elmaskyne) // Remplacez par votre icône de notification
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Afficher la notification
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify((System.currentTimeMillis() % 10000).toInt(), builder.build())

        return Result.success()
    }
}
