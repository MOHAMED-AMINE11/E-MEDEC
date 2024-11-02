package ma.ensa.e_gestionnairemedec.controller

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class DeleteRappelController {

    suspend fun deleteRappel(id: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val url = URL("http://192.168.1.117:8000/controller/deleteRappel.php?id=$id")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            try {
                val responseCode = connection.responseCode
                Log.d("DeleteRappelController", "Response Code: $responseCode")

                return@withContext responseCode == HttpURLConnection.HTTP_OK
            } catch (e: Exception) {
                Log.e("DeleteRappelController", "Erreur lors de la suppression du rappel: ${e.message}")
                return@withContext false
            } finally {
                connection.disconnect()
            }
        }
    }
}
