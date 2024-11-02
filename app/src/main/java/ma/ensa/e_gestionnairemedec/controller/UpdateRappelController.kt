package ma.ensa.e_gestionnairemedec.controller

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ma.ensa.e_gestionnairemedec.classe.Rappel
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class UpdateRappelController(private val context: Context) {

    // Fonction pour mettre à jour un rappel
    suspend fun updateRappel(rappel: Rappel): String {
        return withContext(Dispatchers.IO) {
            try {
                // Définir l'URL de l'API
                val url = URL("http://192.168.1.117:8000/controller/updateRappel.php")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json; utf-8")
                connection.doOutput = true

                // Construire l'objet JSON pour le rappel
                val jsonInput = JSONObject().apply {
                    put("id", rappel.getId())
                    put("utilisateur_id", rappel.getUtilisateurId())
                    put("titre", rappel.getTitre())
                    put("description", rappel.getDescription())
                    put("date_heure", rappel.getDateHeure())
                    put("etat", rappel.getEtat())
                    put("medicament", rappel.getMedicament())
                }

                // Envoyer la requête avec les données JSON
                OutputStreamWriter(connection.outputStream).apply {
                    write(jsonInput.toString())
                    flush()
                    close()
                }

                // Lire la réponse
                val responseCode = connection.responseCode
                val responseMessage = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                } else {
                    "Erreur lors de la mise à jour : Code $responseCode"
                }

                Log.d("UpdateRappelController", "Réponse de l'API: $responseMessage")
                responseMessage

            } catch (e: Exception) {
                Log.e("UpdateRappelController", "Erreur: ${e.message}", e)
                "Erreur lors de la mise à jour du rappel : ${e.message}"
            }
        }
    }
}
