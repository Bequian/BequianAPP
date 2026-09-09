package com.example.bequianapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bequianapp.R
import com.example.bequianapp.data.esCorreoValido
import com.example.bequianapp.data.validar

// Modelo simple para las opciones de recuperación
data class MetodoRecuperacion(val titulo: String, val icono: ImageVector, val descripcion: String)

// Pantalla para la recuperación de contraseña
@Composable
fun RecuperarPassScreen( navigateBack: () -> Unit ) {

    // Estados para mostrar en pantalla
    var metodoSeleccionado by remember { mutableStateOf("") }
    var datoIngresado by remember { mutableStateOf("") }
    var mensajeEnviado by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf("")}

    // Metodos de recuperación
    val metodos = listOf(
        MetodoRecuperacion("Correo", Icons.Default.Email, "Enviar instrucciones al correo"),
        MetodoRecuperacion("SMS", Icons.Default.Phone, "Enviar código al celular")
    )

    // Estado que muestra la imagen dependiendo del modo oscuro del sistema
    val logoActual =
        if(isSystemInDarkTheme()){
            R.drawable.helpi_banner_darkmode
        } else {
            R.drawable.helpi_banner
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

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

        Spacer(modifier = Modifier.height(32.dp))

        Text(

            text = "Recuperar Contraseña",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary

        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(

            text = "Selecciona cómo prefieres recuperar tu acceso. Te enviaremos instrucciones detalladas.",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.primary

        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(

            text = "Método de recuperación:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary

        )

        Spacer(modifier = Modifier.height(16.dp))

        // Grilla con estados de recuperación
        LazyVerticalGrid(

            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
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
                            datoIngresado = ""

                        },

                    border = BorderStroke(

                        width = 2.dp,
                        color = if (metodoSeleccionado == metodo.titulo) MaterialTheme.colorScheme.primary else Color.LightGray

                    ),

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

        Spacer(modifier = Modifier.height(16.dp))

        // Muestra el campo texto según metodo de recuperación
        if (metodoSeleccionado.isNotEmpty()) {

            val labelTexto = if (metodoSeleccionado == "Correo") "Ingresa tu correo" else "Ingresa tu celular"
            val tipoTeclado = if (metodoSeleccionado == "Correo") KeyboardType.Email else KeyboardType.Phone

            OutlinedTextField(
                value = datoIngresado,
                onValueChange = { nuevoValor ->
                    // Según modo de recuperación, si es teléfono valida que sea numero y menor o oigual a 8
                    if (metodoSeleccionado == "SMS") {
                        val soloNumeros = nuevoValor.filter { it.isDigit() }
                        if (soloNumeros.length <= 8) {
                            datoIngresado = soloNumeros
                        }
                    } else {
                        datoIngresado = nuevoValor
                    }
                },
                label = { Text(labelTexto, fontSize = 18.sp) },
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),

                leadingIcon = {

                    Icon(imageVector = if (metodoSeleccionado == "Correo") Icons.Default.Email else Icons.Default.Phone, contentDescription = "Ícono de $metodoSeleccionado")

                },
                prefix = if (metodoSeleccionado == "SMS") {

                    { Text("+56 9 ", fontSize = 18.sp) }

                } else null,

                keyboardOptions = KeyboardOptions(

                    keyboardType = tipoTeclado,
                    imeAction = ImeAction.Done

                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (errorMsg.isNotEmpty()) {

                Surface(

                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Row(

                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(

                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alerta de error",
                            tint = MaterialTheme.colorScheme.onErrorContainer

                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(

                            text = errorMsg,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp

                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (mensajeEnviado) {

                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Row(

                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(

                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Éxito",
                            tint = Color(0xFF2E7D32)

                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(

                            text = "¡Instrucciones enviadas con éxito!",
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp

                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón para simular el envío de instrucciones
            Button(
                onClick = {
                    errorMsg = ""
                    mensajeEnviado = false

                    // Ciclo de selección de la opción de recuperación

                    // Valida si los campos están vacíos
                    if ( !validar( datoIngresado) { it.isNotBlank() }) {

                        errorMsg = "Por favor, ingresa los datos solicitados."

                    }
                    // Ciclo recuperación por correo, válida si el correo tiene formato válido
                    else if( metodoSeleccionado == "Correo" && !validar( datoIngresado, String::esCorreoValido)) {

                        errorMsg = "Por favor, ingresa un correo válido."

                    }
                    // Ciclo recuperación por SMS, válida si el celular tiene 8 digitos
                    else if( metodoSeleccionado == "SMS" && datoIngresado.length < 8) {

                        errorMsg = "El número de celular debe tener 8 dígitos"

                    }
                    else {

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

        Text(

            text = "Volver al inicio de sesión",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            modifier = Modifier.clickable {

                navigateBack()

            }
        )
    }
}