package com.example.bequianapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RecuperarPassScreen(navController: NavController) {
    // Estados de la pantalla
    var metodoSeleccionado by remember { mutableStateOf("") }
    var datoIngresado by remember { mutableStateOf("") }
    var mensajeEnviado by remember { mutableStateOf(false) }

    // Lista de datos para alimentar nuestra Grilla
    val metodos = listOf(
        MetodoRecuperacion("Correo", Icons.Default.Email, "Enviar instrucciones al correo"),
        MetodoRecuperacion("SMS", Icons.Default.Phone, "Enviar código al celular")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Habilitamos el scroll igual que en el registro
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recuperar Contraseña",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selecciona cómo prefieres recuperar tu acceso. Te enviaremos instrucciones detalladas.",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- INICIO DE LA GRILLA (REQUERIMIENTO CUMPLIDO) ---
        Text(
            text = "Método de recuperación:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2 columnas
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp), // Altura fija necesaria cuando está dentro de un verticalScroll
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(metodos) { metodo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clickable(
                            onClickLabel = "Seleccionar método por ${metodo.titulo}"
                        ) {
                            metodoSeleccionado = metodo.titulo
                            mensajeEnviado = false
                            datoIngresado = "" // Limpiamos el input si cambia de método
                        },
                    // Cambiamos el color del borde si está seleccionado
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (metodoSeleccionado == metodo.titulo) MaterialTheme.colorScheme.primary else Color.LightGray
                    ),
                    // Cambiamos el color de fondo si está seleccionado
                    colors = CardDefaults.cardColors(
                        containerColor = if (metodoSeleccionado == metodo.titulo) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = metodo.icono,
                            contentDescription = metodo.descripcion,
                            modifier = Modifier.size(40.dp),
                            tint = if (metodoSeleccionado == metodo.titulo) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = metodo.titulo,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        // --- FIN DE LA GRILLA ---

        Spacer(modifier = Modifier.height(16.dp))

        // Formulario que aparece dinámicamente SOLO si el usuario seleccionó una tarjeta
        if (metodoSeleccionado.isNotEmpty()) {
            val labelTexto = if (metodoSeleccionado == "Correo") "Ingresa tu correo" else "Ingresa tu celular"
            val tipoTeclado = if (metodoSeleccionado == "Correo") KeyboardType.Email else KeyboardType.Phone
            val iconoInput = if (metodoSeleccionado == "Correo") Icons.Default.Email else Icons.Default.Phone

            OutlinedTextField(
                value = datoIngresado,
                onValueChange = { datoIngresado = it },
                label = { Text(labelTexto, fontSize = 18.sp) },
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                leadingIcon = {
                    Icon(imageVector = iconoInput, contentDescription = "Ícono de $metodoSeleccionado")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = tipoTeclado,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de éxito
            if (mensajeEnviado) {
                Text(
                    text = "¡Instrucciones enviadas con éxito!",
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (datoIngresado.isNotBlank()) {
                        mensajeEnviado = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Enviar instrucciones", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Vínculo para volver
        Text(
            text = "Volver al inicio de sesión",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
        )
    }
}

// Data class auxiliar para organizar los datos de la grilla limpiamente
data class MetodoRecuperacion(val titulo: String, val icono: ImageVector, val descripcion: String)