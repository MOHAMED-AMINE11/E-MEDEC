// SplashActivity.kt
package ma.ensa.e_gestionnairemedec

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Trouver l'ImageView du logo et appliquer l'animation fade-in
        val imageViewLogo: ImageView = findViewById(R.id.imageViewLogo)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        imageViewLogo.startAnimation(fadeInAnimation)

        // Charger l'image en cercle dans imageViewLogo avec Glide
        Glide.with(this)
            .load(R.drawable.icon)  // remplacez par l'URL ou ressource de l'image
            .circleCrop()
            .into(imageViewLogo)

        // Délai de 2 secondes avant de passer à MainActivity
        Handler().postDelayed({
            // Masquer la barre de progression et le logo
            findViewById<View>(R.id.progressBar).visibility = View.GONE
            imageViewLogo.visibility = View.GONE

            // Passer à MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Termine SplashActivity pour éviter que l'utilisateur revienne en arrière
        }, 2000) // 2000 millisecondes = 2 secondes
    }
}
