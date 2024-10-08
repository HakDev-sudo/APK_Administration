package com.example.apk_administration.ui.theme.account

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.apk_administration.ui.theme.login.GeetingBuntosnEnter
import com.example.apk_administration.ui.theme.login.GeetingLogoLog

@Composable
fun BasicInfoScreen(onNextClick: (String, String, String) -> Unit,
                    modifier: Modifier = Modifier,
                    navController: NavHostController,padding: PaddingValues
                    ) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Estado para mostrar u ocultar el DatePickerDialog
    var showDatePicker by remember { mutableStateOf(false) }

    // Maneja la selección de fecha
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, yearSelected, month, dayOfMonth ->
            // Actualiza el estado con la fecha seleccionada
            year = "$dayOfMonth/${month + 1}/$yearSelected"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center
    ) {
        GeetingLogoLog(modifier)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Información Personal", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = lastname,
            onValueChange = { lastname = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )

        // Campo para seleccionar fecha de nacimiento
        OutlinedTextField(
            value = year,
            onValueChange = { },
            label = { Text("Fecha de Nacimiento") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            readOnly = true,
            trailingIcon = {
                Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNextClick(name, lastname, year) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF63A7E1))
        ) {
            Text("Siguiente")
        }

        // Mostrar el DatePickerDialog
        if (showDatePicker==true) {
            datePickerDialog.show()
            showDatePicker = false
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BasicInfoScreenPreview() {
    BasicInfoScreen(onNextClick = { name, lastname, year ->
        // Acción de navegación simulada
    }, navController = NavHostController(LocalContext.current),padding = PaddingValues())
}


