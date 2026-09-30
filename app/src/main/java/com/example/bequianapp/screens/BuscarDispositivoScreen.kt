package com.example.bequianapp.screens

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.core.content.getSystemService
import com.example.bequianapp.data.AuthService
import com.example.bequianapp.data.HelpiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Hace vibrar el teléfono. getSystemService<Vibrator>() es una extensión KTX de core-ktx.
@Suppress("DEPRECATION")
fun vibrar(context: Context, milisegundos: Long) {

    val vibrador = context.getSystemService<Vibrator>() ?: return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        vibrador.vibrate(VibrationEffect.createOneShot(milisegundos, VibrationEffect.DEFAULT_AMPLITUDE))

    } else {

        vibrador.vibrate(milisegundos)

    }
}

// Emite un tono de alarma durante unos segundos (no necesita permisos ni archivos de audio)
suspend fun emitirSonido(milisegundos: Int) {

    try {

        val tono = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        tono.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, milisegundos)
        delay(milisegundos + 200L)
        tono.release()

    } catch (e: RuntimeException) {

    }
}

// Aviso que parpadea mientras el teléfono suena (útil para personas con discapacidad auditiva)
@Composable
fun AlertaSonando(quien: String) {

    val parpadeo = rememberInfiniteTransition(label = "alerta")

    val colorAlerta by parpadeo.animateColor(

        initialValue = MaterialTheme.colorScheme.errorContainer,
        targetValue = MaterialTheme.colorScheme.primaryContainer,
        animationSpec = infiniteRepeatable(tween(400), RepeatMode.Reverse),
        label = "colorAlerta"

    )
    Surface(

        color = colorAlerta,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)

    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Sonando: $quien", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        }
    }
}

@Composable
fun BuscarDispositivoScreen(

    authService: AuthService,
    repo: HelpiRepository,
    navigateBack: () -> Unit

) {

    val uid = remember { authService.uidActual() ?: "sin-sesion" }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var sonando by remember { mutableStateOf<String?>(null) }

    // Hace sonar y vibrar el teléfono durante 3 segundos
    fun hacerSonar(quien: String) {

        if (sonando != null) return

        scope.launch {

            sonando = quien
            vibrar(context, 3000)
            emitirSonido(3000)
            sonando = null

        }
    }


    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {

        BannerHelpi()

        Spacer(modifier = Modifier.height(16.dp))

        EncabezadoHelpi(titulo = "Buscar dispositivo", onBack = navigateBack)

        Spacer(modifier = Modifier.height(16.dp))

        // Alerta visual mientras suena
        sonando?.let { quien -> AlertaSonando(quien) }

        Spacer(modifier = Modifier.height(16.dp))

        Button(

            onClick = { hacerSonar("este teléfono") },
            modifier = Modifier.fillMaxWidth().height(56.dp)

        ) {

            Icon(Icons.Default.NotificationsActive, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Hacer sonar este teléfono", fontSize = 18.sp)

        }

    }
}
