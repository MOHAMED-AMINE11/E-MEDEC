import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ma.ensa.e_gestionnairemedec.adapter.RappelAdapter
import ma.ensa.e_gestionnairemedec.controller.DeleteRappelController
import ma.ensa.e_gestionnairemedec.classe.Rappel

class SwipeToDeleteCallback(
    private val adapter: RappelAdapter,
    private val reloadRappels: () -> Unit // Fonction pour recharger les rappels
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val paint = Paint().apply {
        color = Color.RED // Couleur rouge pour l'arrière-plan de la suppression
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false // Pas besoin de gérer le mouvement ici
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val rappel = adapter.getRappelAtPosition(position) // Obtenez le rappel à la position

        // Afficher un dialog de confirmation
        AlertDialog.Builder(viewHolder.itemView.context)
            .setTitle("Confirmer la suppression")
            .setMessage("Voulez-vous vraiment supprimer ce rappel ?")
            .setPositiveButton("Oui") { _, _ ->
                // Utiliser un CoroutineScope pour appeler la fonction suspendue
                CoroutineScope(Dispatchers.Main).launch {
                    val controller = DeleteRappelController() // Initialiser le contrôleur
                    val response = controller.deleteRappel(rappel.getId()) // Supprimez le rappel en utilisant l'ID

                    // Vérifiez si la suppression a réussi et affichez un message

                    // Rechargez les rappels après la suppression
                    reloadRappels()
                }
            }
            .setNegativeButton("Non") { dialog, _ ->
                dialog.dismiss()
                adapter.notifyItemChanged(position) // Rétablir l'élément si l'utilisateur refuse
            }
            .show()
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        if (dX < 0) { // Balayage à gauche
            c.drawRect(
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat(),
                paint
            )
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
