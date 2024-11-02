package ma.ensa.e_gestionnairemedec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ma.ensa.e_gestionnairemedec.controller.AddRappelController
import ma.ensa.e_gestionnairemedec.classe.Rappel
import ma.ensa.e_gestionnairemedec.utils.NotificationUtils // Assurez-vous d'importer votre classe utilitaire de notification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddRappelDialogFragment : DialogFragment() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var editTextDateTime: EditText
    private lateinit var editTextMedicament: EditText
    private lateinit var spinnerEtat: Spinner
    private lateinit var buttonAddRappel: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_add_rappel, container, false)

        // Initialiser les vues
        editTextTitle = view.findViewById(R.id.edit_title)
        editTextDescription = view.findViewById(R.id.edit_description)
        editTextDateTime = view.findViewById(R.id.edit_date_time)
        editTextMedicament = view.findViewById(R.id.edit_medicament)
        spinnerEtat = view.findViewById(R.id.spinner_etat)
        buttonAddRappel = view.findViewById(R.id.button_add_rappel)

        // Initialiser le Spinner avec l'adaptateur
        val etatArrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_etat,
            android.R.layout.simple_spinner_item
        )
        etatArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEtat.adapter = etatArrayAdapter

        buttonAddRappel.setOnClickListener {
            saveRappel()
        }

        return view
    }

    private fun saveRappel() {
        val titre = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val dateHeureString = editTextDateTime.text.toString()
        val medicament = editTextMedicament.text.toString()
        val etat = spinnerEtat.selectedItem.toString() // État sélectionné du Spinner

        // Conversion de la chaîne de date en objet Date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateHeure: Date = dateFormat.parse(dateHeureString) ?: Date() // Utilise la date actuelle si le format est invalide

        // Créez un nouvel objet Rappel avec les données fournies
        val rappel = Rappel(
            id = 0, // L'ID sera géré par la base de données
            utilisateurId = 1, // Remplacez par l'ID utilisateur approprié
            titre = titre,
            description = description,
            dateHeure = dateHeure,
            etat = etat,
            createdAt = Date(), // Date actuelle pour la création
            medicament = medicament
        )

        // Utilisez le AddRappelController pour sauvegarder le rappel
        lifecycleScope.launch {
            val controller = AddRappelController()
            val success = controller.addRappel(rappel)
            if (success) {
                // Planifier la notification
                NotificationUtils.scheduleNotification(requireContext(), rappel)

                // Afficher un message Toast pour indiquer que le rappel a été ajouté
                Toast.makeText(requireContext(), "Rappel ajouté avec succès!", Toast.LENGTH_SHORT).show()
                dismiss() // Ferme le Dialog
            } else {
                // Afficher un message Toast pour indiquer qu'il y a eu une erreur lors de l'ajout
                Toast.makeText(requireContext(), "Erreur lors de l'ajout du rappel.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
