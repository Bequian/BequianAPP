package com.example.bequianapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelo de cada tema de ayuda
data class TemaAyuda(

    val titulo: String,
    val explicacion: String,
    val icono: ImageVector

)

// Vista de Ayuda: pequeño tutorial de cada función de Helpi
@Composable
fun AyudaScreen(

    navigateToHome: () -> Unit

) {

    val temas = listOf(

        TemaAyuda("Escribir", "Escribe o dicta con el micrófono un mensaje y muéstralo en pantalla completa con letras grandes. Puedes guardarlo para usarlo después.", Icons.Default.Edit),
        TemaAyuda("Hablar", "Escribe un texto y el teléfono lo lee en voz alta. Guarda frases rápidas para las situaciones que más usas.", Icons.AutoMirrored.Filled.VolumeUp),
        TemaAyuda("Buscar dispositivo", "Presiona \"Hacer sonar este teléfono\": suena una alarma, vibra y aparece un aviso que parpadea en pantalla.", Icons.Default.Search),
        TemaAyuda("Preferencias", "En el menú principal puedes activar o desactivar la vibración al tocar las opciones.", Icons.Default.Settings)

    )

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        BannerHelpi()

        Spacer(modifier = Modifier.height(16.dp))

        Text(

            text = "Ayuda",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center

        )

        Text(

            text = "¿Cómo usar Helpi?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjetas con la explicación de cada función
        temas.forEach { tema ->

            ElevatedCard(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)

            ) {
                Row(

                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Icon(

                        imageVector = tema.icono,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary

                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {

                        Text(tema.titulo, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text(tema.explicacion, fontSize = 16.sp)

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(

            onClick = { navigateToHome() },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)

        ) {

            Text("Volver al menú principal", fontSize = 20.sp)

        }
    }
}
