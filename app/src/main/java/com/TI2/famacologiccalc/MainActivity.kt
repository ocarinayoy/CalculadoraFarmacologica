package com.TI2.famacologiccalc

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.TI2.famacologiccalc.adapters.PacienteAdapter
import com.TI2.famacologiccalc.databinding.ActivityMainBinding
import com.TI2.famacologiccalc.sesion.ActualSession
import com.TI2.famacologiccalc.database.DatabaseInstance
import com.TI2.famacologiccalc.database.repositories.PacienteRepository
import com.TI2.famacologiccalc.database.session.ActualPatient
import com.TI2.famacologiccalc.ui.ViewModelFactory
import com.TI2.famacologiccalc.viewmodels.PacienteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

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
            val pacienteId = ActualSession.usuarioLogeado?.id // Asumimos que el usuario tiene un ID
            if (pacienteId != null) {
                val database = DatabaseInstance.getDatabase(this)
                val pacienteRepository = PacienteRepository(database.pacienteDao())
                val pacienteViewModel = ViewModelProvider(this, ViewModelFactory(null,pacienteRepository)).get(PacienteViewModel::class.java)

                // Consultamos el paciente asociado al usuario
                pacienteViewModel.obtenerPacientesPorUsuario(pacienteId).observe(this) { paciente ->
                    paciente?.let {
                        mostrarConsultaPaciente()
                    } ?: run {
                        Toast.makeText(
                            this,
                            "No se encontró un paciente asociado",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
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

    private fun navigateToSettingsFragment() {
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
        val etFechaRegistro = bottomSheetView.findViewById<EditText>(R.id.etFechaRegistro) // Campo de fecha
        val spEstatusPaciente = bottomSheetView.findViewById<Spinner>(R.id.spEstatusPaciente)
        val btnRegistrarPaciente = bottomSheetView.findViewById<Button>(R.id.btnRegistrarPaciente)

        // Configuración del DatePickerDialog para seleccionar la fecha
        etFechaRegistro.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = android.app.DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    etFechaRegistro.setText(selectedDate) // Mostrar la fecha seleccionada en el EditText
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Definir las opciones para el spinner de estatus
        val estatusOptions = listOf("Alta", "Baja", "En tratamiento")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estatusOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spEstatusPaciente.adapter = adapter

        btnRegistrarPaciente.setOnClickListener {
            val nombre = etNombrePaciente.text.toString()
            val edad = etEdadPaciente.text.toString().toIntOrNull()
            val peso = etPesoPaciente.text.toString().toDoubleOrNull()
            val altura = etAlturaPaciente.text.toString().toDoubleOrNull()
            val fechaRegistro = etFechaRegistro.text.toString()
            val estatus = spEstatusPaciente.selectedItem.toString()

            // Validación de campos antes de registrar
            if (nombre.isNotEmpty() && edad != null && peso != null && altura != null && fechaRegistro.isNotEmpty()) {
                // Validar edad (0-150 años)
                if (edad < 0 || edad > 150) {
                    Toast.makeText(this, "Edad debe estar entre 0 y 150 años", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Validar peso (1-500 kg)
                if (peso < 1 || peso > 500) {
                    Toast.makeText(this, "Peso debe estar entre 1 y 500 kg", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Validar altura (0.5-3 metros)
                if (altura < 20 || altura > 350) {
                    Toast.makeText(this, "Altura debe estar entre 20cm y 3.5 metros ", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val database = DatabaseInstance.getDatabase(this)
                val pacienteRepository = PacienteRepository(database.pacienteDao())
                val pacienteViewModel = PacienteViewModel(pacienteRepository)
                val usuarioId = ActualSession.usuarioLogeado?.id ?: return@setOnClickListener

                // Registrar paciente
                pacienteViewModel.registrarPaciente(nombre, edad, peso, altura, fechaRegistro, estatus, usuarioId)
                Toast.makeText(this, "Paciente registrado exitosamente", Toast.LENGTH_SHORT).show()
                bottomSheetDialog.dismiss()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
            }
        }


        bottomSheetDialog.show()
    }


    //Metodo para mostrar pacientes registrados por usuario
    private fun mostrarConsultaPaciente() {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_consulta_paciente, null)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)

        val rvPacienteList = bottomSheetView.findViewById<RecyclerView>(R.id.rv_patient_list)
        rvPacienteList.layoutManager = LinearLayoutManager(this)

        val btnCerrar = bottomSheetView.findViewById<Button>(R.id.btnCloseBottomSheet) // Botón de cerrar en el diseño
        val usuarioId = ActualSession.usuarioLogeado?.id

        if (usuarioId == null) {
            Toast.makeText(this, "Usuario no válido", Toast.LENGTH_SHORT).show()
            return
        }

        val pacienteViewModel = ViewModelProvider(
            this,
            ViewModelFactory(null, PacienteRepository(DatabaseInstance.getDatabase(this).pacienteDao()))
        ).get(PacienteViewModel::class.java)

        pacienteViewModel.obtenerPacientesPorUsuario(usuarioId).observe(this, { pacientes ->
            if (pacientes.isEmpty()) {
                Toast.makeText(this, "No hay pacientes registrados para este usuario", Toast.LENGTH_SHORT).show()
            } else {
                val adapter = PacienteAdapter(pacientes) { paciente ->
                    Toast.makeText(this, "Paciente seleccionado: ${paciente.nombre}", Toast.LENGTH_SHORT).show()
                    ActualPatient.pacienteSeleccionado = paciente // Actualiza el paciente seleccionado
                    // No cerrar el BottomSheet aquí
                }
                rvPacienteList.adapter = adapter
            }
        })

        // Configuración del botón "Cerrar"
        btnCerrar.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

}

