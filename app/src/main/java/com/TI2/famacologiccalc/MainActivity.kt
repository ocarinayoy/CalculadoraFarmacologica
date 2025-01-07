package com.TI2.famacologiccalc

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.TI2.famacologiccalc.databinding.ActivityMainBinding
import com.TI2.famacologiccalc.sesion.ActualSession

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura el Toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        // Configuración del Navigation Drawer y ActionBar
        val drawerLayout = binding.drawerLayout
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_weighted_dosage,
                R.id.nav_surface_dosage,
                R.id.nav_clark,
                R.id.nav_young,
                R.id.nav_fried,
                R.id.nav_infusion_rate,
                R.id.nav_renal_clearance
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // Llamar a updateNavHeader para inicializar el header con los datos de sesión
        updateNavHeader()

        // Agregar listener para detectar cuando el fragmento cambia
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Si el destino es login o registro, ocultamos el Toolbar
            if (destination.id == R.id.nav_login || destination.id == R.id.nav_register) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show() // Mostrar el Toolbar en otros fragmentos
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Acción cuando se selecciona el ítem de configuración
                navigateToSettingsFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Función para navegar al fragmento de configuración
    fun navigateToSettingsFragment() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.

        nav_settings) // Asegúrate de que el ID del fragmento sea correcto
    }

    // Función fuera de onCreate para ser accesible globalmente
    fun updateNavHeader() {
        // Obtén la vista del header del NavigationView
        val headerView = binding.navView.getHeaderView(0)

        // Encuentra los TextViews del header
        val userNameTextView = headerView.findViewById<TextView>(R.id.etUsername)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.etEmail)

        // Actualiza el texto con los datos del usuario logueado
        userNameTextView.text = ActualSession.usuarioLogeado?.nombre ?: "Nombre no disponible"
        userEmailTextView.text = ActualSession.usuarioLogeado?.email ?: "Correo no disponible"
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setFabVisibility(visible: Boolean) {
        // Controla la visibilidad del FAB desde cualquier fragmento
        if (visible) {
            binding.appBarMain.fab.show()
        } else {
            binding.appBarMain.fab.hide()
        }
    }
}
