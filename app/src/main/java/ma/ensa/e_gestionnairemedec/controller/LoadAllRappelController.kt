package ma.ensa.e_gestionnairemedec.controller

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ma.ensa.e_gestionnairemedec.classe.Rappel
import java.net.HttpURLConnection
import java.net.URL

class LoadAllRappelController {

    suspend fun loadAllRappels(): List<Rappel> {
        return withContext(Dispatchers.IO) {
            val url = URL("http://192.168.1.117:8000/ws/loadRappel.php")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            try {
                val responseCode = connection.responseCode
                Log.d("LoadAllRappelController", "Response Code: $responseCode")

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = inputStream.bufferedReader().use { it.readText() }

                    // Conversion de la réponse JSON en liste d'objets Rappel
                    val gson = Gson()
                    val listType = object : TypeToken<List<Rappel>>() {}.type
                    return@withContext gson.fromJson(response, listType)
                } else {
                    Log.e("LoadAllRappelController", "Erreur lors de la récupération des rappels")
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                Log.e("LoadAllRappelController", "Erreur lors de la récupération des rappels: ${e.message}")
                return@withContext emptyList()
            } finally {
                connection.disconnect()
            }
        }
    }
}
