package ma.ensa.e_gestionnairemedec.utils

import android.app.NotificationManager
import android.content.Context
import androidx.work.*
import ma.ensa.e_gestionnairemedec.classe.Rappel
import ma.ensa.e_gestionnairemedec.worker.NotificationWorker
import java.util.Date
import java.util.concurrent.TimeUnit

object NotificationUtils {

    fun scheduleNotification(context: Context, rappel: Rappel) {
        val data = Data.Builder()
            .putString("title", rappel.getTitre())
            .putString("description", rappel.getDescription())
            .build()

        val delayInMillis = calculateDelay(rappel.getDateHeure()) // Méthode pour calculer le délai en millisecondes

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    private fun calculateDelay(dateHeure: Date): Long {
        val now = System.currentTimeMillis()
        return if (dateHeure.time > now) {
            dateHeure.time - now // Délai en millisecondes
        } else {
            0 // Pas de délai si la date est déjà passée
        }
    }
}
