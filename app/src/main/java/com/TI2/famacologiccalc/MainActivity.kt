package com.TI2.famacologiccalc

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.TI2.famacologiccalc.databinding.ActivityMainBinding
import com.TI2.famacologiccalc.sesion.ActualSession
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.viewmodels.PacienteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

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

        // Agregar listener para el FAB y mostrar el BottomSheetDialog
        binding.appBarMain.fab.setOnClickListener {
            mostrarRegistroPaciente()
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
        navController.navigate(R.id.nav_settings) // Asegúrate de que el ID del fragmento sea correcto
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

    // Función para mostrar el BottomSheetDialog con el formulario de registro de paciente
    private fun mostrarRegistroPaciente() {
        // Inflar el layout del registro de paciente
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_registro_paciente, null)

        // Crear el BottomSheetDialog
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        // Configurar los campos del BottomSheet
        val etNombrePaciente = bottomSheetView.findViewById<EditText>(R.id.etNombrePaciente)
        val etEdadPaciente = bottomSheetView.findViewById<EditText>(R.id.etEdadPaciente)
        val etPesoPaciente = bottomSheetView.findViewById<EditText>(R.id.etPesoPaciente)
        val etAlturaPaciente = bottomSheetView.findViewById<EditText>(R.id.etAlturaPaciente)

        // Configurar el botón de registro
        val btnRegistrarPaciente = bottomSheetView.findViewById<Button>(R.id.btnRegistrarPaciente)
        btnRegistrarPaciente.setOnClickListener {
            val nombre = etNombrePaciente.text.toString()
            val edad = etEdadPaciente.text.toString().toIntOrNull()
            val peso = etPesoPaciente.text.toString().toDoubleOrNull()
            val altura = etAlturaPaciente.text.toString().toDoubleOrNull()

            if (nombre.isNotEmpty() && edad != null && peso != null) {
                // Aquí podrías agregar la lógica para registrar el paciente en la base de datos
                val database = DatabaseInstance.getDatabase(this)
                val pacienteRepository = PacienteRepository(database.pacienteDao())
                val pacienteViewModel = PacienteViewModel(pacienteRepository)

                // Asociar el paciente con el usuario logueado
                val usuarioId = ActualSession.usuarioLogeado?.id ?: return@setOnClickListener

                // Registrar el paciente
                pacienteViewModel.registrarPaciente(nombre, edad, peso, altura, usuarioId)
                Toast.makeText(this, "Paciente registrado exitosamente", Toast.LENGTH_SHORT).show()

                bottomSheetDialog.dismiss() // Cerrar el BottomSheet después del registro
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        // Mostrar el BottomSheet
        bottomSheetDialog.show()
    }
}
