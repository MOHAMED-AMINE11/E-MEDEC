package ma.ensa.e_gestionnairemedec.controller

import android.util.Log
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ma.ensa.e_gestionnairemedec.classe.Rappel
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter
import java.util.concurrent.TimeUnit
import androidx.work.Constraints
import ma.ensa.e_gestionnairemedec.worker.NotificationWorker // Assurez-vous d'importer votre NotificationWorker

class AddRappelController {

    suspend fun addRappel(rappel: Rappel): Boolean {
        return withContext(Dispatchers.IO) {
            val url = URL("http://192.168.1.117:8000/controller/addRappel.php")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.doOutput = true

            // Préparer les données à envoyer
            val postData = "utilisateur_id=${rappel.getUtilisateurId()}&" +
                    "titre=${rappel.getTitre()}&" +
                    "description=${rappel.getDescription()}&" +
                    "date_heure=${rappel.getDateHeure()}&" +
                    "etat=${rappel.getEtat()}&" +
                    "medicament=${rappel.getMedicament()}"

            try {
                // Envoyer les données
                OutputStreamWriter(connection.outputStream).use { writer ->
                    writer.write(postData)
                }

                // Lire la réponse
                val responseCode = connection.responseCode
                Log.d("AddRappelController", "Response Code: $responseCode")

                // Lire la réponse du serveur pour le débogage
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { reader ->
                        val response = reader.readText()
                        Log.d("AddRappelController", "Response: $response")

                        // Planifier la notification après l'ajout réussi
                        scheduleNotification(rappel)
                    }
                    return@withContext true
                } else {
                    // En cas d'erreur, lire le message d'erreur
                    connection.errorStream?.bufferedReader()?.use { reader ->
                        val errorResponse = reader.readText()
                        Log.e("AddRappelController", "Erreur lors de l'ajout du rappel: $errorResponse")
                    }
                    Log.e("AddRappelController", "Erreur: Code de réponse $responseCode")
                    return@withContext false
                }
            } catch (e: Exception) {
                Log.e("AddRappelController", "Erreur lors de l'ajout du rappel: ${e.message}")
                return@withContext false
            } finally {
                connection.disconnect()
            }
        }
    }

    private fun scheduleNotification(rappel: Rappel) {
        // Calculer le délai avant la notification (ex : 30 minutes avant la date de prise)
        val reminderTimeMillis = rappel.getDateHeure().time // Assurez-vous que getDateHeure() renvoie un objet Date ou équivalent
        val currentTimeMillis = System.currentTimeMillis()
        val delayInMinutes = TimeUnit.MILLISECONDS.toMinutes(reminderTimeMillis - currentTimeMillis) - 5 // 30 minutes avant

        if (delayInMinutes > 0) {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true) // Exemple de contrainte
                .build()

            val notificationWork: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(workDataOf("title" to rappel.getTitre(), "description" to rappel.getDescription()))
                .setConstraints(constraints)
                .setInitialDelay(delayInMinutes, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance().enqueue(notificationWork)
            Log.d("AddRappelController", "Notification planifiée pour le rappel : ${rappel.getTitre()}")
        } else {
            Log.d("AddRappelController", "Le délai pour la notification est invalide ou trop court.")
        }
    }
}
