package ma.ensa.e_gestionnairemedec.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ma.ensa.e_gestionnairemedec.R
import ma.ensa.e_gestionnairemedec.classe.Rappel

class RappelAdapter(private var rappels: List<Rappel>) : RecyclerView.Adapter<RappelAdapter.RappelViewHolder>(), Filterable {

    private var rappelsFiltres: List<Rappel> = rappels

    inner class RappelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titreRappel: TextView = itemView.findViewById(R.id.titre_rappel)
        val etatRappel: TextView = itemView.findViewById(R.id.etat_rappel)
        val userImage: ImageView = itemView.findViewById(R.id.user_image) // ImageView pour l'image circulaire

        fun bind(rappel: Rappel) {
            titreRappel.text = rappel.getTitre()
            etatRappel.text = rappel.getEtat()

            // Appliquer la couleur du texte en fonction de l'état
            when (rappel.getEtat()) {
                "en_attente" -> etatRappel.setTextColor(Color.BLUE)
                "terminé" -> etatRappel.setTextColor(Color.GREEN)
                "annulé" -> etatRappel.setTextColor(Color.RED)
                else -> etatRappel.setTextColor(Color.BLACK)
            }

            // Charger l'image avec Glide
            Glide.with(itemView.context)
                .load(R.drawable.ic_rappels) // Remplacez par l'URL ou la ressource appropriée
                .circleCrop()
                .into(userImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RappelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rappel, parent, false)
        return RappelViewHolder(view)
    }

    override fun onBindViewHolder(holder: RappelViewHolder, position: Int) {
        val rappel = rappelsFiltres[position]
        holder.bind(rappel)
    }

    override fun getItemCount(): Int {
        return rappelsFiltres.size
    }

    // Nouvelle méthode pour obtenir un rappel à une position spécifique
    fun getRappelAtPosition(position: Int): Rappel {
        return rappelsFiltres[position]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    rappels
                } else {
                    val query = constraint.toString().lowercase()
                    // Filtrage selon le titre, la date et l'état
                    rappels.filter {
                        it.getTitre().lowercase().contains(query) ||
                                it.getFormattedDateHeure().contains(query) || // Utiliser la date formatée
                                it.getEtat().lowercase().contains(query)
                    }
                }
                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                rappelsFiltres = results?.values as? List<Rappel> ?: listOf()
                notifyDataSetChanged()
            }
        }
    }
}
