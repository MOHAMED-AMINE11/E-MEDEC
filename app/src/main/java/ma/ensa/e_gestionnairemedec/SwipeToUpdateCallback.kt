import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ma.ensa.e_gestionnairemedec.R
import ma.ensa.e_gestionnairemedec.adapter.RappelAdapter
import ma.ensa.e_gestionnairemedec.controller.UpdateRappelController
import ma.ensa.e_gestionnairemedec.classe.Rappel
import java.text.SimpleDateFormat
import java.util.*

class SwipeToUpdateCallback(
    private val adapter: RappelAdapter,
    private val reloadRappels: () -> Unit // Passer une fonction pour recharger les rappels
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    private val paint = Paint().apply {
        color = Color.GREEN // Couleur verte pour le balayage à droite
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val rappel = adapter.getRappelAtPosition(position)
        val context = viewHolder.itemView.context // Obtenir le contexte ici

        showUpdateDialog(rappel, position, context)
    }

    private fun showUpdateDialog(rappel: Rappel, position: Int, context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_update_rappel, null)

        // Initialiser les champs d'édition avec les valeurs existantes
        val titreEditText = view.findViewById<EditText>(R.id.titre_edit_text)
        val descriptionEditText = view.findViewById<EditText>(R.id.description_edit_text)
        val medicamentEditText = view.findViewById<EditText>(R.id.medicament_edit_text)
        val etatEditText = view.findViewById<EditText>(R.id.etat_edit_text)

        titreEditText.setText(rappel.getTitre())
        descriptionEditText.setText(rappel.getDescription())
        medicamentEditText.setText(rappel.getMedicament())
        etatEditText.setText(rappel.getEtat())

        AlertDialog.Builder(context)
            .setTitle("Mettre à jour le rappel")
            .setView(view)
            .setPositiveButton("Sauvegarder") { _, _ ->
                // Récupérer les valeurs mises à jour
                val updatedTitre = titreEditText.text.toString()
                val updatedDescription = descriptionEditText.text.toString()
                val updatedMedicament = medicamentEditText.text.toString()
                val updatedEtat = etatEditText.text.toString()

                // Validation des champs
                if (updatedTitre.isEmpty() || updatedDescription.isEmpty() || updatedMedicament.isEmpty() || updatedEtat.isEmpty() ) {
                    Toast.makeText(context, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show()
                } else {
                    // Convertir la chaîne de date en Date

                    // Mettre à jour l'objet rappel
                    rappel.setTitre(updatedTitre)
                    rappel.setDescription(updatedDescription)
                    rappel.setMedicament(updatedMedicament)
                    rappel.setEtat(updatedEtat)

                    // Utiliser un CoroutineScope pour appeler la fonction suspendue
                    CoroutineScope(Dispatchers.Main).launch {
                        val controller = UpdateRappelController(context)
                        val response = controller.updateRappel(rappel) // Appeler la fonction pour mettre à jour
                        // Gérer la réponse (afficher un message ou mettre à jour l'UI)
                        if (response.contains("Erreur")) {
                            Toast.makeText(context, "Échec de la mise à jour", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Mise à jour réussie", Toast.LENGTH_SHORT).show()
                        }
                        // Rechargez les rappels après la mise à jour
                        reloadRappels()
                    }
                }
            }
            .setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
                adapter.notifyItemChanged(position) // Rétablir l'élément si l'utilisateur refuse
            }
            .show()
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
        if (dX > 0) { // Balayage à droite
            c.drawRect(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.left + dX,
                itemView.bottom.toFloat(),
                paint
            )
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
