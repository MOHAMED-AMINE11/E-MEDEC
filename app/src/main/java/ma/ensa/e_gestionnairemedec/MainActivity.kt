package ma.ensa.e_gestionnairemedec

import SwipeToDeleteCallback
import SwipeToUpdateCallback
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.navigation.NavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import ma.ensa.e_gestionnairemedec.adapter.RappelAdapter
import ma.ensa.e_gestionnairemedec.controller.LoadAllRappelController
import ma.ensa.e_gestionnairemedec.classe.Rappel
import ma.ensa.e_gestionnairemedec.worker.CleanupWorker // Importez CleanupWorker
import ma.ensa.e_gestionnairemedec.worker.NotificationWorker
import androidx.work.* // N'oubliez pas d'importer WorkManager et les classes nécessaires
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var navigationView: NavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var rappelAdapter: RappelAdapter
    private lateinit var welcomeMessage: TextView
    private var rappels: List<Rappel> = listOf()

    companion object {
        private const val NOTIFICATION_PERMISSION_CODE = 1001 // Code pour la permission de notification
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Demander la permission de notification
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_CODE)
        }

        // Créer le canal de notification
        createNotificationChannel()

        // Initialiser la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialiser la vue du message de bienvenue
        welcomeMessage = findViewById(R.id.welcome_message)

        // Initialiser NavigationView et RecyclerView
        navigationView = findViewById(R.id.nav_view)
        recyclerView = findViewById(R.id.rappel_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialiser les éléments de l'en-tête de NavigationView
        val headerView = navigationView.getHeaderView(0)
        val userImage: ImageView = headerView.findViewById(R.id.user_image)
        val userName: TextView = headerView.findViewById(R.id.user_name)
        val userAge: TextView = headerView.findViewById(R.id.user_age)

        // Définir des données utilisateur factices
        userName.text = "Mohamed Amine El Maskyne"
        userAge.text = "20 ans"
        userImage.setImageResource(R.drawable.elmaskyne)

        // Charger et afficher le message de bienvenue
        showWelcomeMessage()

        Glide.with(this)
            .load(R.drawable.elmaskyne)
            .circleCrop()
            .into(userImage)

        // Gérer les clics de menu de navigation
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_mes_rappels -> {
                    loadRappels()
                    true
                }
                R.id.nav_parametres -> {
                    showSettings()
                    true
                }
                R.id.nav_aide -> {
                    showHelp()
                    true
                }
                R.id.nav_deconnexion -> {
                    showLogout()
                    true
                }
                else -> false
            }
        }

        // Initialiser le FloatingActionButton pour ajouter un rappel
        val fabAddRappel: FloatingActionButton = findViewById(R.id.fab_add_rappel)
        fabAddRappel.setOnClickListener {
            // Afficher le dialog pour ajouter un rappel
            val dialog = AddRappelDialogFragment()
            dialog.show(supportFragmentManager, "AddRappelDialog")
        }

        // Planifier le CleanupWorker à un intervalle de 24 heures
        scheduleCleanupWorker()
    }

    private fun showWelcomeMessage() {
        lifecycleScope.launch {
            val controller = LoadAllRappelController()
            rappels = controller.loadAllRappels()
            val countPendingRappels = rappels.count { it.getEtat() == "en_attente" }
            welcomeMessage.text = "Bienvenue ! Vous avez $countPendingRappels rappel(s) en attente."
        }
    }

    private fun loadRappels() {
        lifecycleScope.launch {
            try {
                val controller = LoadAllRappelController()
                rappels = controller.loadAllRappels()
                rappelAdapter = RappelAdapter(rappels)
                recyclerView.adapter = rappelAdapter
                recyclerView.visibility = View.VISIBLE
                welcomeMessage.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Rappels chargés avec succès", Toast.LENGTH_SHORT).show()

                // Planifier des notifications pour chaque rappel
                for (rappel in rappels) {
                    if (rappel.getEtat() == "en_attente") {
                        val delayInMillis = rappel.getDateHeure().time - System.currentTimeMillis()
                        if (delayInMillis > 0) { // Vérifiez si le délai est positif
                            scheduleNotification(rappel.getTitre(), rappel.getDescription(), delayInMillis)
                        }
                    }
                }

                // Attacher l'ItemTouchHelper pour le balayage de suppression
                val itemTouchHelperDelete = ItemTouchHelper(SwipeToDeleteCallback(rappelAdapter) {
                    loadRappels() // Recharger les rappels après une suppression
                })
                itemTouchHelperDelete.attachToRecyclerView(recyclerView)

                // Attacher l'ItemTouchHelper pour le balayage de mise à jour
                val itemTouchHelperUpdate = ItemTouchHelper(SwipeToUpdateCallback(rappelAdapter) {
                    loadRappels() // Passer ici la fonction pour recharger les rappels
                })
                itemTouchHelperUpdate.attachToRecyclerView(recyclerView)

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@MainActivity, "Erreur lors du chargement des rappels.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSettings() {
        // Logique pour afficher les paramètres
        Toast.makeText(this, "Paramètres", Toast.LENGTH_SHORT).show()
    }

    private fun showHelp() {
        // Logique pour afficher l'aide
        Toast.makeText(this, "Aide", Toast.LENGTH_SHORT).show()
    }

    private fun showLogout() {
        // Logique pour gérer la déconnexion
        Toast.makeText(this, "Déconnexion", Toast.LENGTH_SHORT).show()
    }

    private fun scheduleNotification(title: String, description: String, delayInMillis: Long) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true) // Exemple de contrainte
            .build()

        val notificationWork: WorkRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInputData(workDataOf("title" to title, "description" to description))
            .setConstraints(constraints)
            .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS) // Délai avant l'exécution
            .build()

        WorkManager.getInstance(applicationContext).enqueue(notificationWork)
    }

    private fun scheduleCleanupWorker() {
        val cleanupWorkRequest = PeriodicWorkRequestBuilder<CleanupWorker>(24, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "cleanup_work",
            ExistingPeriodicWorkPolicy.REPLACE,
            cleanupWorkRequest
        )
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Rappel Notifications"
            val descriptionText = "Canal pour les notifications de rappels"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("RAPPEL_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        // Configure le SearchView pour filtrer les rappels
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                // Vérifie si le rappelAdapter est initialisé avant de filtrer
                if (::rappelAdapter.isInitialized) {
                    rappelAdapter.filter.filter(newText)
                }
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // La permission a été accordée
            } else {
                // La permission a été refusée
                Toast.makeText(this, "Permission de notification refusée", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
