package com.example.bequianapp.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bequianapp.data.AuthService
import com.example.bequianapp.data.HelpiRepository
import com.example.bequianapp.data.Resultado
import com.example.bequianapp.data.Usuario
import kotlinx.coroutines.launch

// Modelo de cada opción del menú
data class OpcionMenu(

    val titulo: String,
    val descripcion: String,
    val icono: ImageVector,
    val accion: () -> Unit

)

// HomeMenú: acceso a Escribir, Hablar y BuscarDispositivo + preferencias del usuario
@Composable
fun HomeScreen(

    authService: AuthService,
    repo: HelpiRepository,
    navigateToEscribir: () -> Unit,
    navigateToHablar: () -> Unit,
    navigateToBuscar: () -> Unit,
    navigateToAyuda: () -> Unit,
    onLogout: () -> Unit

) {
    // Se guarda el uid una sola vez para no perderlo al cerrar sesión
    val uid = remember { authService.uidActual() ?: "sin-sesion" }

    val correo = remember { authService.correoActual() }

    val scope = rememberCoroutineScope()

    val haptic = LocalHapticFeedback.current

    // Perfil leído en tiempo real desde Firebase (Read del CRUD)
    val perfil by remember(uid) { repo.perfil(uid) }.collectAsState(initial = Usuario())

    var mostrarDialogo by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf("") }

    val opciones = listOf(

        OpcionMenu("Escribir", "Escribe o dicta mensajes y muéstralos en grande", Icons.Default.Edit, navigateToEscribir),
        OpcionMenu("Hablar", "El teléfono lee en voz alta lo que escribes", Icons.AutoMirrored.Filled.VolumeUp, navigateToHablar),
        OpcionMenu("Buscar dispositivo", "Encuentra tus audífonos y equipos haciéndolos sonar", Icons.Default.Search, navigateToBuscar),
        OpcionMenu("Ayuda", "Aprende a usar cada función de Helpi", Icons.Default.Info, navigateToAyuda)

    )

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),

        ) {
        BannerHelpi()

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Usuario: "+correo, fontSize = 20.sp, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(16.dp))

        // Tarjetas del menú (botones grandes y con descripción para facilitar su uso)
        opciones.forEach { opcion ->

            ElevatedCard(

                modifier = Modifier

                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable(onClickLabel = "Abrir ${opcion.titulo}") {

                        if (perfil.vibracion) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        opcion.accion()

                    }
            ) {
                Row(

                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Icon(

                        imageVector = opcion.icono,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary

                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {

                        Text(opcion.titulo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(opcion.descripcion, fontSize = 16.sp)

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Preferencias guardadas en Firebase (Update del CRUD)
        Text("Mis preferencias", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Row(

            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Vibración en botones", fontSize = 18.sp, modifier = Modifier.weight(1f))

            Switch(

                checked = perfil.vibracion,
                onCheckedChange = { activa ->

                    scope.launch {
                        try {

                            repo.actualizarVibracion(uid, activa)

                        } catch (e: Exception) {

                            errorMsg = "No se pudo guardar la preferencia."

                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        MensajeEstado(texto = errorMsg, esError = true)

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cerrar sesión
        Button(
            onClick = {

                authService.cerrarSesion()
                onLogout()

            },

            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {

            Text("Cerrar Sesión", fontSize = 20.sp)

        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón para eliminar la cuenta (Delete del CRUD)
        OutlinedButton(

            onClick = { mostrarDialogo = true },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth().height(56.dp)

        ) {

            Text("Eliminar mi cuenta", fontSize = 20.sp)

        }
    }

    // Diálogo de confirmación antes de eliminar la cuenta
    if (mostrarDialogo) {

        AlertDialog(

            onDismissRequest = { mostrarDialogo = false },

            title = { Text("¿Eliminar tu cuenta?") },

            text = { Text("Se borrarán tu perfil, mensajes, frases y dispositivos. Esta acción no se puede deshacer.") },

            confirmButton = {

                TextButton(onClick = {

                    mostrarDialogo = false
                    scope.launch {
                        try {

                            repo.eliminarDatosUsuario(uid)

                        } catch (e: Exception) {

                        }

                        when (val resultado = authService.eliminarCuenta()) {

                            is Resultado.Exito -> onLogout()
                            is Resultado.Error -> errorMsg = resultado.mensaje

                        }
                    }
                }) {

                    Text("Eliminar", color = MaterialTheme.colorScheme.error)

                }
            },
            dismissButton = {

                TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }

            }
        )
    }
}
