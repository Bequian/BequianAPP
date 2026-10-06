package com.example.bequianapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bequianapp.data.AuthService

// Vista de Bienvenida: se muestra después de iniciar sesión o registrarse
@Composable
fun BienvenidaScreen(

    authService: AuthService,
    navigateToHome: () -> Unit

) {
    // Nombre para el saludo: la parte del correo antes de la @ (ej: angelo@mail.com -> Angelo)
    val nombre = remember {
        authService.correoActual()
            .substringBefore("@")
            .replaceFirstChar { it.uppercase() }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        BannerHelpi()

        Spacer(modifier = Modifier.height(32.dp))

        Text(

            text = if (nombre.isNotEmpty()) "¡Hola," else "¡Hola!",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(

            text = if (nombre.isNotEmpty()) "$nombre!" else "¡Usuario!",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(

            text = "Bienvenido a Helpi, tu ayuda para comunicarte con el mundo.",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(

            text = "Desde el menú principal puedes escribir mensajes en letras grandes, " +
                    "hacer que el teléfono hable por ti y buscar tu teléfono haciéndolo sonar.",
            fontSize = 18.sp,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(

            onClick = { navigateToHome() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)

        ) {

            Text("Ir al menú principal", fontSize = 20.sp)

        }
    }
}
