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

import androidx.compose.material.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicInfoScreen(
    onNextClick: (String, String, String, String, String) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    padding: PaddingValues
) {
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") } // Campo para ingresar la fecha manualmente
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = topAppBarColors(Color(0xFF63A7E1))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
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

            // Campo para ingresar la fecha manualmente
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Fecha de Nacimiento (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Sección de información de contacto
            ContactInfoContent(
                email = email,
                phone = phone,
                onEmailChange = { email = it },
                onPhoneChange = { phone = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones "Cancelar" y "Siguiente" en la misma fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.popBackStack() }, // Navega al login
                    colors = ButtonDefaults.buttonColors(Color(0xFF63A7E1))
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = { onNextClick(name, lastname, year, email, phone)
                        navController.navigate("profile_image")
                        {
                            popUpTo("profile_image") { inclusive = true } // Limpia la pantalla anterior
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF63A7E1))
                ) {
                    Text("Siguiente")
                }
            }
        }
    }
}




