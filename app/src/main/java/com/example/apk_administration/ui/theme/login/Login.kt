package com.example.apk_administration.ui.theme.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDefaults.color
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.PermissionInfoCompat.Protection
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.apk_administration.R

@Composable
fun LoginStructre(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by authViewModel.isLoading.observeAsState(false)

    // Observar el estado de autenticación
    LaunchedEffect(authViewModel.authState.observeAsState().value) {
        val user = authViewModel.authState.value
        if (user != null) {
            navController.navigate("home") // Navegar a la pantalla principal si está autenticado
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp, start = 50.dp, end = 50.dp, bottom = 0.dp)
                    .border(20.dp, Color(0xFF2196F3), RoundedCornerShape(20.dp))
                    .padding(10.dp)
                    .background(Color(0xFF2196F3)),
            ) {
                GeetingLogoLog(modifier)
            }
            Column(
                modifier = Modifier
                    .padding(start = 50.dp, end = 50.dp, top = 255.dp)
                    .border(4.dp, Color(0xFF2196F3), RoundedCornerShape(20.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GeetingInputsLog(modifier, email, { email = it }, password, { password = it })
                GeetingButtonsEnter(
                    modifier = Modifier,
                    email = email,
                    password = password,
                    isLoading = isLoading,
                    onLoginClick = { authViewModel.signIn(email, password) },
                    onRegisterNavigate = { navController.navigate("register") }
                )
                GeetingButtonsLog(modifier)
            }
        }
    }
}


@Composable
fun GeetingLogoLog(modifier: Modifier){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize(0.2.toFloat()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center



    ){
        Image(painter = painterResource(id = R.drawable.logo), contentDescription ="", modifier = Modifier.size(170.dp) )
    }

}
@Composable
fun GeetingInputsLog(
    modifier: Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(text = "Ingrese su correo") },
            suffix = { Text(text = "@gmail.com") },
        )
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(text = "Ingrese su contraseña") },
            placeholder = { Text(text = "Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
    }

}


@Composable
fun GeetingButtonsEnter(
    modifier: Modifier,
    email: String,
    password: String,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterNavigate: () -> Unit
) {
    Column(modifier = Modifier) {
        OutlinedButton(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    onLoginClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Text(text = "Entrar")
            }
        }
        OutlinedButton(
            onClick = onRegisterNavigate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Text(text = "Registrarse")
        }
    }
}

@Composable
fun GeetingButtonsLog(modifier : Modifier){
    Column (
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            onClick = {  },
            enabled = true,
            // add background color
            colors = buttonColors(colorResource(id = R.color.black,))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Logo",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Registrarse con Google")
        }
    }

}


