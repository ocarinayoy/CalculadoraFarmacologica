package com.TI2.famacologiccalc

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.TI2.famacologiccalc.databinding.ActivityMainBinding
import com.TI2.famacologiccalc.sesion.ActualSession
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.viewmodels.PacienteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var isFabExpanded = false // Estado de los botones secundarios

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura el navcontroller
        navController = findNavController(R.id.nav_host_fragment_content_main)

        // Configuración del Toolbar
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

        // Listener para detectar cambios en el fragmento actual
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_login || destination.id == R.id.nav_register) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }

        // Configurar los FABs
        binding.appBarMain.fab.setOnClickListener {
            toggleFabMenu()
        }

        binding.appBarMain.fabRegisterPatient.setOnClickListener {
            mostrarRegistroPaciente() // Llamar al método existente
        }

        binding.appBarMain.fabConsultPatient.setOnClickListener {
            Toast.makeText(this, "Consultar paciente actual", Toast.LENGTH_SHORT).show()
        }

        // Agregar un callback para manejar el botón de retroceso
        onBackPressedDispatcher.addCallback(this) {
            handleBackPress() // Llama al método que maneja la lógica del retroceso
        }
    }

    // Método para alternar el menú del FAB
    private fun toggleFabMenu() {
        isFabExpanded = !isFabExpanded

        // Animación del FAB principal (giro)
        val rotationAngle = if (isFabExpanded) 90f else 0f // Rota 135 grados si está expandido, 0 si no
        binding.appBarMain.fab.animate()
            .rotation(rotationAngle)  // Rota el FAB principal
            .setDuration(300)
            .start()

        if (isFabExpanded) {
            binding.appBarMain.fabRegisterPatient.isVisible = true
            binding.appBarMain.fabConsultPatient.isVisible = true
            animateFab(binding.appBarMain.fabRegisterPatient, -25f)
            animateFab(binding.appBarMain.fabConsultPatient, -50f)
        } else {
            animateFab(binding.appBarMain.fabRegisterPatient, 0f, false)
            animateFab(binding.appBarMain.fabConsultPatient, 0f, false)
            binding.appBarMain.fabRegisterPatient.isVisible = false
            binding.appBarMain.fabConsultPatient.isVisible = false
        }
    }

    // Método para animar los botones secundarios
    private fun animateFab(fab: FloatingActionButton, translationY: Float, show: Boolean = true) {
        fab.animate()
            .translationY(translationY)
            .alpha(if (show) 1f else 0f)
            .setDuration(300)
            .start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                navigateToSettingsFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun navigateToSettingsFragment() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.nav_settings)
    }

    fun updateNavHeader() {
        val headerView = binding.navView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.etUsername)
        val userEmailTextView = headerView.findViewById<TextView>(R.id.etEspecialidad)
        userNameTextView.text = ActualSession.usuarioLogeado?.nombre ?: "Nombre no disponible"
        userEmailTextView.text = ActualSession.usuarioLogeado?.especialidad ?: "Sin especialidad"
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setFabVisibility(visible: Boolean) {
        if (visible) {
            binding.appBarMain.fab.show()
        } else {
            binding.appBarMain.fab.hide()
        }
    }

    private fun mostrarRegistroPaciente() {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_registro_paciente, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val etNombrePaciente = bottomSheetView.findViewById<EditText>(R.id.etNombrePaciente)
        val etEdadPaciente = bottomSheetView.findViewById<EditText>(R.id.etEdadPaciente)
        val etPesoPaciente = bottomSheetView.findViewById<EditText>(R.id.etPesoPaciente)
        val etAlturaPaciente = bottomSheetView.findViewById<EditText>(R.id.etAlturaPaciente)
        val btnRegistrarPaciente = bottomSheetView.findViewById<Button>(R.id.btnRegistrarPaciente)

        btnRegistrarPaciente.setOnClickListener {
            val nombre = etNombrePaciente.text.toString()
            val edad = etEdadPaciente.text.toString().toIntOrNull()
            val peso = etPesoPaciente.text.toString().toDoubleOrNull()
            val altura = etAlturaPaciente.text.toString().toDoubleOrNull()

            if (nombre.isNotEmpty() && edad != null && peso != null) {
                val database = DatabaseInstance.getDatabase(this)
                val pacienteRepository = PacienteRepository(database.pacienteDao())
                val pacienteViewModel = PacienteViewModel(pacienteRepository)
                val usuarioId = ActualSession.usuarioLogeado?.id ?: return@setOnClickListener

                pacienteViewModel.registrarPaciente(nombre, edad, peso, altura, usuarioId)
                Toast.makeText(this, "Paciente registrado exitosamente", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        bottomSheetDialog.show()
    }

    // Metodo que maneja el retroceso
// Método que maneja el retroceso
    private fun handleBackPress() {
        when (navController.currentDestination?.id) {
            R.id.nav_home -> {
                // Si estamos en el fragmento de home, no hacer nada
                // o mostrar un mensaje indicando que el usuario debe cerrar sesión
                Toast.makeText(this, "Por favor, cierre sesión para salir", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_register -> {
                // Si estamos en el fragmento de registro, navegamos al fragmento de login
                navController.navigate(R.id.nav_login)
            }
            R.id.nav_login -> {
                // Si estamos en el fragmento de login, cerramos la aplicación
                onBackPressedDispatcher.onBackPressed()
            }
            else -> {
                // Si estamos en cualquier otro fragmento y el usuario está logueado, regresamos al home
                if (ActualSession.isLoggedIn) {
                    navController.navigate(R.id.nav_home)
                } else {
                    // Si no está logueado, retrocedemos normalmente
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
    }

}
