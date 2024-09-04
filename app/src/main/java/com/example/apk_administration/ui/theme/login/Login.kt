package com.example.apk_administration.ui.theme.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.DividerDefaults.color
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDefaults.color
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.apk_administration.R

@Composable
fun LoginStructre(modifier: Modifier){
    Column (
        modifier = Modifier
            .background(Color(0xFFFDFDFD))

    ){
        GeetingLogoLog(modifier)
        GeetingInputsLog(modifier)
        GeetingButtonsLog(modifier)

    }
}

@Composable
fun GeetingLogoLog(modifier: Modifier){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center



    ){
        Image(painter = painterResource(id = R.drawable.logo), contentDescription ="", modifier = Modifier.size(100.dp) )
    }

}
@Composable
fun GeetingInputsLog(modifier: Modifier){
    var email by remember {
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }

    var isCorrect by remember {
        mutableStateOf(true)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email, onValueChange ={email = it},
            label = { Text(text = "Ingrese su correo") },
            suffix = { Text(text = "@gmail.com") },
        )
        OutlinedTextField(value = password, onValueChange ={password = it},
            label = { Text(text = "Ingrese su contraseña")},
            placeholder = { Text(text = "Contraseña")},
            visualTransformation = PasswordVisualTransformation()
        )
    }

}


@Composable
fun GeetingBuntosnEnter(modifier: Modifier){

    Column(modifier = Modifier){
        OutlinedButton(onClick = {}, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 54.dp, end = 54.dp)) {
            Text(text = "Entrar")
        }
        OutlinedButton(onClick = {}, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 54.dp, end = 54.dp)){
            Text(text = "Registrarse")

        }
    }
}

@Composable
fun GeetingButtonsLog(modifier : Modifier){
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 54.dp, end = 54.dp),
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

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 56.dp, end = 54.dp),
            onClick = {}
        ){
            Icon(painter = painterResource(id = R.drawable.facebook), contentDescription ="Facebook Logo" , tint = Color.Unspecified)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "Registrase con Facebook")
        }
    }

}


@Composable
fun recoveriCount(modifier: Modifier){

}

@Preview(showBackground = true)
@Composable
fun previewsTotal(){
    LoginStructre(modifier = Modifier)
}
