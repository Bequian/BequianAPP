package com.example.bequianapp.screens

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.bequianapp.data.AuthService
import com.example.bequianapp.data.HelpiRepository
import com.example.bequianapp.data.Mensaje
import com.example.bequianapp.data.aFechaTexto
import com.example.bequianapp.data.validar
import kotlinx.coroutines.launch

// Vista Escribir: el usuario escribe o dicta (voz a texto) un mensaje,
// lo muestra en pantalla completa a otra persona y lo guarda en Firebase (CRUD).
@Composable
fun EscribirScreen(

    authService: AuthService,
    repo: HelpiRepository,
    navigateBack: () -> Unit

) {

    val uid = remember { authService.uidActual() ?: "sin-sesion" }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    // Lista de mensajes en tiempo real (Read)
    val mensajes by remember(uid) { repo.mensajes(uid) }.collectAsState(initial = emptyList())

    var texto by remember { mutableStateOf("") }

    var mensajeEditando by remember { mutableStateOf<Mensaje?>(null) }

    var textoPantallaCompleta by remember { mutableStateOf<String?>(null) }

    var estado by remember { mutableStateOf("") }

    var esError by remember { mutableStateOf(false) }

    // Recibe el resultado del reconocimiento de voz (lo que otra persona dijo)
    val lanzadorVoz = rememberLauncherForActivityResult(

        ActivityResultContracts.StartActivityForResult()

    ) { resultado ->

        if (resultado.resultCode == Activity.RESULT_OK) {

            val dictado = resultado.data

                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()

            if (!dictado.isNullOrBlank()) {

                texto = if (texto.isBlank()) dictado else "$texto $dictado"

            }
        }
    }

    // Abre el reconocimiento de voz de Google en español
    fun dictar() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {

            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CL")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla ahora, Helpi lo escribirá")

        }

        try {

            lanzadorVoz.launch(intent)

        } catch (e: ActivityNotFoundException) {

            estado = "Este equipo no tiene reconocimiento de voz disponible."
            esError = true

        }
    }

    // Create / Update del mensaje
    fun guardar() {

        if (!validar(texto) { it.isNotBlank() }) {

            estado = "Escribe un mensaje antes de guardar."
            esError = true
            return

        }
        scope.launch {

            try {

                val editando = mensajeEditando

                if (editando == null) {

                    repo.guardarMensaje(uid, texto.trim())
                    estado = "Mensaje guardado."

                } else {

                    repo.actualizarMensaje(uid, editando.copy(texto = texto.trim()))
                    estado = "Mensaje actualizado."

                }

                esError = false
                texto = ""
                mensajeEditando = null

            } catch (e: Exception) {

                estado = "No se pudo guardar. Revisa tu conexión."
                esError = true

            }
        }
    }

    // Delete del mensaje
    fun eliminar(mensaje: Mensaje) {

        scope.launch {

            try {

                repo.eliminarMensaje(uid, mensaje.id)
                estado = "Mensaje eliminado."
                esError = false

            } catch (e: Exception) {

                estado = "No se pudo eliminar. Revisa tu conexión."
                esError = true

            }
        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {

        BannerHelpi()

        Spacer(modifier = Modifier.height(16.dp))

        EncabezadoHelpi(titulo = "Escribir", onBack = navigateBack)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(

            value = texto,
            onValueChange = { texto = it },
            label = { Text("Escribe o dicta tu mensaje", fontSize = 18.sp) },
            textStyle = LocalTextStyle.current.copy(fontSize = 22.sp),
            minLines = 3,
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(

            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()

        ) {
            OutlinedButton(

                onClick = { dictar() },
                modifier = Modifier.weight(1f).height(56.dp)

            ) {

                Icon(Icons.Default.Mic, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Dictar", fontSize = 18.sp)

            }
            OutlinedButton(

                onClick = { if (texto.isNotBlank()) textoPantallaCompleta = texto },
                modifier = Modifier.weight(1f).height(56.dp)

            ) {

                Icon(Icons.Default.Fullscreen, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Mostrar", fontSize = 18.sp)

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(

            onClick = { guardar() },
            modifier = Modifier.fillMaxWidth().height(56.dp)

        ) {

            Text(if (mensajeEditando == null) "Guardar mensaje" else "Actualizar mensaje", fontSize = 20.sp)

        }

        if (mensajeEditando != null) {

            TextButton(onClick = {

                mensajeEditando = null
                texto = ""

            }) {

                Text("Cancelar edición", fontSize = 16.sp)

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        MensajeEstado(texto = estado, esError = esError)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Mis mensajes (${mensajes.size})", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (mensajes.isEmpty()) {

            Text("Aún no tienes mensajes guardados.", fontSize = 16.sp)

        }

        // Lista de mensajes guardados
        LazyColumn(

            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)

        ) {

            items(mensajes, key = { it.id }) { mensaje ->

                Card(modifier = Modifier.fillMaxWidth()) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(mensaje.texto, fontSize = 20.sp)

                        Text(

                            mensaje.fecha.aFechaTexto(),
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant

                        )
                        Row(

                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()

                        ) {

                            IconButton(onClick = { textoPantallaCompleta = mensaje.texto }) {

                                Icon(Icons.Default.Fullscreen, contentDescription = "Mostrar en pantalla completa")

                            }

                            IconButton(onClick = {

                                mensajeEditando = mensaje
                                texto = mensaje.texto

                            }) {

                                Icon(Icons.Default.Edit, contentDescription = "Editar mensaje")

                            }

                            IconButton(onClick = { eliminar(mensaje) }) {

                                Icon(

                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar mensaje",
                                    tint = MaterialTheme.colorScheme.error

                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Muestra el mensaje en letras grandes para que otra persona lo lea. Se cierra con un toque.
    textoPantallaCompleta?.let { textoGrande ->

        Dialog(

            onDismissRequest = { textoPantallaCompleta = null },
            properties = DialogProperties(usePlatformDefaultWidth = false)

        ) {

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,

                modifier = Modifier
                    .fillMaxSize()
                    .clickable(onClickLabel = "Cerrar pantalla completa") { textoPantallaCompleta = null }

            ) {
                Box(

                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize().padding(24.dp)

                ) {

                    Text(

                        text = textoGrande,
                        fontSize = 44.sp,
                        lineHeight = 52.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimaryContainer

                    )
                }
            }
        }
    }
}
