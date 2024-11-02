package ma.ensa.e_gestionnairemedec.classe

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Rappel(
    private var id: Int,
    private var utilisateurId: Int,
    private var titre: String,
    private var description: String,
    private var dateHeure: Date,
    private var etat: String,
    private var createdAt: Date,
    private var medicament: String
) {
    // Constructeur secondaire pour créer un rappel avec des valeurs par défaut
    constructor() : this(
        id = 0,
        utilisateurId = 0,
        titre = "",
        description = "",
        dateHeure = Date(),
        etat = "en_attente",
        createdAt = Date(),
        medicament = ""
    )

    // Getters
    fun getId() = id
    fun getUtilisateurId() = utilisateurId
    fun getTitre() = titre
    fun getDescription() = description
    fun getEtat() = etat
    fun getMedicament() = medicament

    fun getDateHeure(): Date = dateHeure

    // Retourner la date de création sous forme de chaîne
    fun getCreatedAt(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(createdAt)
    }

    // Formater `dateHeure` sous forme de chaîne pour l'affichage
    fun getFormattedDateHeure(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(dateHeure)
    }

    // Setters
    fun setId(id: Int) {
        this.id = id
    }

    fun setUtilisateurId(utilisateurId: Int) {
        this.utilisateurId = utilisateurId
    }

    fun setTitre(titre: String) {
        this.titre = titre
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setDateHeure(dateHeure: Date) {
        this.dateHeure = dateHeure
    }

    fun setEtat(etat: String) {
        this.etat = etat
    }

    fun setCreatedAt(createdAt: Date) {
        this.createdAt = createdAt
    }

    fun setMedicament(medicament: String) {
        this.medicament = medicament
    }
}
