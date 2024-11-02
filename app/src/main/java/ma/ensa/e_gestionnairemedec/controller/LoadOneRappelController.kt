package ma.ensa.e_gestionnairemedec.controller

import android.util.Log
import com.google.gson.Gson
import ma.ensa.e_gestionnairemedec.classe.Rappel
import java.net.HttpURLConnection
import java.net.URL

class LoadOneRappelController {

     fun loadRappelById(id: Int): Rappel? {
        return try {
            val url = URL("http://192.168.1.117:8000/ws/loadoneRappel.php?id=$id")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")

            val responseCode = connection.responseCode
            Log.d("LoadOneRappelController", "Response Code: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream.bufferedReader().use { it.readText() }
                parseAndFormatRappel(inputStream)
            } else {
                Log.e("LoadOneRappelController", "Erreur de réponse: $responseCode")
                null
            }
        } catch (e: Exception) {
            Log.e("LoadOneRappelController", "Erreur lors du chargement du rappel: ${e.message}")
            null
        }
    }

    private fun parseAndFormatRappel(json: String): Rappel? {
        // Utilisation de Gson pour parser le JSON
        val gson = Gson()
        return try {
            gson.fromJson(json, Rappel::class.java)
        } catch (e: Exception) {
            Log.e("LoadOneRappelController", "Erreur lors du parsing JSON: ${e.message}")
            null
        }
    }



}
