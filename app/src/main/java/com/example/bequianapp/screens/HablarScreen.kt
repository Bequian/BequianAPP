package com.example.bequianapp.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bequianapp.data.AuthService
import com.example.bequianapp.data.Frase
import com.example.bequianapp.data.HelpiRepository
import com.example.bequianapp.data.validar
import kotlinx.coroutines.launch
import java.util.Locale

// Frases que se pueden agregar con un toque la primera vez
private val frasesSugeridas = listOf(

    "Hola, tengo discapacidad auditiva. ¿Me puedes escribir?",
    "¿Puedes hablar más lento, por favor?",
    "Muchas gracias por tu ayuda.",
    "Necesito ayuda, por favor."

)

// Vista Hablar: el teléfono lee en voz alta (Texto a Voz) lo que el usuario escribe.
// Las frases rápidas se guardan en Firebase (CRUD).
@Composable
fun HablarScreen(

    authService: AuthService,
    repo: HelpiRepository,
    navigateBack: () -> Unit

) {

    val uid = remember { authService.uidActual() ?: "sin-sesion" }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    // Frases rápidas en tiempo real (Read)
    val frases by remember(uid) { repo.frases(uid) }.collectAsState(initial = emptyList())

    var texto by remember { mutableStateOf("") }

    var fraseEditando by remember { mutableStateOf<Frase?>(null) }

    var velocidad by remember { mutableFloatStateOf(1f) }

    var estado by remember { mutableStateOf("") }

    var esError by remember { mutableStateOf(false) }

    var ttsListo by remember { mutableStateOf(false) }

    // Motor de Texto a Voz del equipo
    val tts = remember {

        TextToSpeech(context) { status -> ttsListo = status == TextToSpeech.SUCCESS }

    }

    LaunchedEffect(ttsListo) {

        if (ttsListo) {

            val resultado = tts.setLanguage(Locale.forLanguageTag("es-CL"))

            if (resultado == TextToSpeech.LANG_MISSING_DATA || resultado == TextToSpeech.LANG_NOT_SUPPORTED) {

                tts.setLanguage(Locale.forLanguageTag("es"))

            }
        }
    }

    DisposableEffect(Unit) {

        onDispose {

            tts.stop()
            tts.shutdown()

        }

    }

    fun hablar(textoAHablar: String) {

        if (!ttsListo) {

            estado = "El lector de voz aún no está listo."
            esError = true
            return

        }

        if (textoAHablar.isBlank()) {

            estado = "Escribe o elige una frase para hablar."
            esError = true
            return

        }

        estado = ""

        tts.setSpeechRate(velocidad)

        tts.speak(textoAHablar, TextToSpeech.QUEUE_FLUSH, null, "helpi")

    }

    // Create / Update de frases
    fun guardarFrase() {

        if (!validar(texto) { it.isNotBlank() }) {

            estado = "Escribe una frase antes de guardarla."
            esError = true
            return

        }
        scope.launch {

            try {

                val editando = fraseEditando

                if (editando == null) {

                    repo.guardarFrase(uid, texto.trim())
                    estado = "Frase guardada."

                } else {

                    repo.actualizarFrase(uid, editando.copy(texto = texto.trim()))
                    estado = "Frase actualizada."

                }

                esError = false
                texto = ""
                fraseEditando = null

            } catch (e: Exception) {

                estado = "No se pudo guardar. Revisa tu conexión."
                esError = true

            }
        }
    }

    // Delete de frases
    fun eliminarFrase(frase: Frase) {

        scope.launch {

            try {

                repo.eliminarFrase(uid, frase.id)
                estado = "Frase eliminada."
                esError = false

            } catch (e: Exception) {

                estado = "No se pudo eliminar. Revisa tu conexión."
                esError = true

            }
        }
    }

    fun agregarSugeridas() {

        scope.launch {

            try {

                frasesSugeridas.forEach { repo.guardarFrase(uid, it) }

            } catch (e: Exception) {

                estado = "No se pudieron agregar las frases."
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

        EncabezadoHelpi(titulo = "Hablar", onBack = navigateBack)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Escribe lo que quieres decir y Helpi lo leerá en voz alta.", fontSize = 16.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(

            value = texto,
            onValueChange = { texto = it },
            label = { Text("¿Qué quieres decir?", fontSize = 18.sp) },
            textStyle = LocalTextStyle.current.copy(fontSize = 22.sp),
            minLines = 2,
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(

            onClick = { hablar(texto) },
            modifier = Modifier.fillMaxWidth().height(64.dp)

        ) {

            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hablar", fontSize = 22.sp)

        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(

            onClick = { guardarFrase() },
            modifier = Modifier.fillMaxWidth().height(56.dp)

        ) {

            Text(if (fraseEditando == null) "Guardar como frase rápida" else "Actualizar frase", fontSize = 18.sp)

        }

        if (fraseEditando != null) {

            TextButton(onClick = {

                fraseEditando = null
                texto = ""

            }) {

                Text("Cancelar edición", fontSize = 16.sp)

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        MensajeEstado(texto = estado, esError = esError)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Frases rápidas (toca una para decirla)", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        if (frases.isEmpty()) {

            OutlinedButton(onClick = { agregarSugeridas() }, modifier = Modifier.fillMaxWidth()) {
                Text("Agregar frases sugeridas", fontSize = 16.sp)

            }
        }

        LazyColumn(

            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)

        ) {

            items(frases, key = { it.id }) { frase ->
                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClickLabel = "Decir en voz alta") { hablar(frase.texto) }

                ) {
                    Row(

                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(

                            Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary

                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(frase.texto, fontSize = 18.sp, modifier = Modifier.weight(1f))

                        IconButton(onClick = {

                            fraseEditando = frase
                            texto = frase.texto

                        }) {

                            Icon(Icons.Default.Edit, contentDescription = "Editar frase")

                        }

                        IconButton(onClick = { eliminarFrase(frase) }) {

                            Icon(

                                Icons.Default.Delete,
                                contentDescription = "Eliminar frase",
                                tint = MaterialTheme.colorScheme.error

                            )
                        }
                    }
                }
            }
        }
    }
}
