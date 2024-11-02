package ma.ensa.e_gestionnairemedec

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import ma.ensa.e_gestionnairemedec.R

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val helpTextView: TextView = findViewById(R.id.help_text)
        helpTextView.text = getHelpText()
    }

    private fun getHelpText(): String {
        return """
            Bienvenue dans notre application de gestion des rappels !

            Cette application vous aide à garder un œil attentif sur vos prises de médicaments et vos rendez-vous médicaux.

            Voici ce que vous pouvez faire avec cette application :
            - Créer des rappels pour vos médicaments.
            - Recevoir des notifications lorsque c'est l'heure de prendre votre médicament.
            - Consulter l'historique de vos rappels pour suivre vos prises.

            Il est crucial de respecter vos horaires de médication pour votre santé. 
            N'oubliez pas de consulter votre médecin si vous avez des questions ou des préoccupations concernant vos médicaments.
        """.trimIndent()
    }
}
