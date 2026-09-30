package com.example.bequianapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bequianapp.R

// Componentes reutilizables para las vistas nuevas (Escribir, Hablar, BuscarDispositivo)

// Encabezado con botón "volver" y título grande
@Composable
fun EncabezadoHelpi(titulo: String, onBack: () -> Unit) {

    Row(

        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()

    ) {

        IconButton(onClick = onBack) {

            Icon(

                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver al menú"

            )
        }

        Text(

            text = titulo,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary

        )
    }
}

// Mensaje visual de error (rojo) o de éxito (verde), igual al usado en Login y Registro
@Composable
fun MensajeEstado(texto: String, esError: Boolean) {

    if (texto.isEmpty()) return

    val fondo = if (esError) MaterialTheme.colorScheme.errorContainer else Color(0xFFE8F5E9)
    val color = if (esError) MaterialTheme.colorScheme.onErrorContainer else Color(0xFF2E7D32)

    Surface(

        color = fondo,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()

    ) {

        Row(

            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(

                imageVector = if (esError) Icons.Default.Warning else Icons.Default.CheckCircle,
                contentDescription = if (esError) "Alerta de error" else "Éxito",
                tint = color

            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = texto, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun BannerHelpi() {

    val logoActual =
        if(isSystemInDarkTheme()){
            R.drawable.helpi_banner_darkmode
        } else {
            R.drawable.helpi_banner
        }

    Row(

        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()

    ) {

        Image(

            painter = painterResource(id = logoActual),
            contentDescription = "Logotipo de la aplicación Helpi: Conectándote con el mundo",

            modifier = Modifier
                .fillMaxWidth()
                .size(120.dp)
                .padding(top = 16.dp),

            contentScale = ContentScale.Fit

        )
    }
}