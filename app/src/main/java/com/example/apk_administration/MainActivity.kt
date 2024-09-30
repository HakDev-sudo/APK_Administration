package com.example.apk_administration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.apk_administration.ui.theme.Apk_administrationTheme
import com.example.apk_administration.ui.theme.account.RegisterStructre
import com.example.apk_administration.ui.theme.login.LoginStructre
import com.example.apk_administration.ui.theme.navigation.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Apk_administrationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginStructre(modifier = Modifier.background(skinColor))
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