package ma.ensa.e_gestionnairemedec.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ma.ensa.e_gestionnairemedec.controller.DeleteRappelController
import ma.ensa.e_gestionnairemedec.controller.LoadAllRappelController

class CleanupWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        return runBlocking {
            runCatching {
                cleanupTerminatedRappels()
            }.fold(
                onSuccess = { Result.success() },
                onFailure = { Result.failure() }
            )
        }
    }

    private suspend fun cleanupTerminatedRappels() {
        val controller = LoadAllRappelController()
        val rappels = controller.loadAllRappels()

        val terminatedRappels = rappels.filter { it.getEtat() == "terminé" || it.getEtat() == "annulé" }

        val deleteController = DeleteRappelController()

        terminatedRappels.forEach { rappel ->
            val deleted = deleteController.deleteRappel(rappel.getId())
            if (deleted) {

            }
        }
    }
}
