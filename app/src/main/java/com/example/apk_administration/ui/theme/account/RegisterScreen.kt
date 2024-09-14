package com.example.apk_administration.ui.theme.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import com.example.apk_administration.ui.theme.login.GeetingBuntosnEnter
import com.example.apk_administration.ui.theme.login.GeetingLogoLog

@Composable
fun RegisterStructre(modifier: Modifier){
    Column (
        modifier = Modifier
            .background(Color(0xFFFDFDFD))

    ){
        GeetingLogoLog(modifier)
        GeetingInputsRegis(modifier)
        GeetingInputsRegis(modifier)
        GeetingBuntosnEnter(modifier = Modifier)

    }
}


@Composable
fun GeetingInputsRegis(modifier: Modifier){
    var name by remember{mutableStateOf("")}
    var lastName by remember{mutableStateOf("")}
    var sex by remember{mutableStateOf("")}
    var yeard by remember{mutableStateOf("")}
    var phoneNumber by remember{mutableStateOf("")}
    Column(modifier = Modifier){
       OutlinedTextField(value = name, onValueChange = {name = it}, label = {Text(text = "Ingrese su nombre")})
        OutlinedTextField(value = lastName, onValueChange = {lastName = it}, label = {Text(text = "Ingrese su apellido")})
        OutlinedTextField(value = sex, onValueChange = {sex = it}, label = {Text(text = "Ingrese su sexo")})
        OutlinedTextField(value = yeard, onValueChange = {yeard = it}, label = {Text(text = "Ingrese su edad")})
        OutlinedTextField(value = phoneNumber, onValueChange = {phoneNumber = it}, label = {Text(text = "Ingrese su Número de celular")})
    }
}