package com.example.apk_administration

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext
import androidx.navigation.NavHostController
import com.example.apk_administration.ui.theme.Apk_administrationTheme
import com.example.apk_administration.ui.theme.NFC.NFCWindow
import com.example.apk_administration.ui.theme.login.AppNavigation
import com.example.apk_administration.ui.theme.login.AuthViewModel
import com.example.apk_administration.ui.theme.login.LoginStructure
import com.example.apk_administration.ui.theme.navigation.AlmacenApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val authviewModel = AuthViewModel(auth, firestore)
        enableEdgeToEdge()
        setContent {
            Apk_administrationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(authviewModel = authviewModel)
                }
            }
        }
    }
}
val skinColor = Color(0xFFF0D4C9)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Apk_administrationTheme {
    }
}



