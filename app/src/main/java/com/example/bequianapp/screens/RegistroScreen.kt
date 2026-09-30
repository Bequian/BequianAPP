package com.example.bequianapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import com.example.bequianapp.data.AuthService
import com.example.bequianapp.data.HelpiRepository
import com.example.bequianapp.data.Resultado
import com.example.bequianapp.data.Usuario
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun RegistroScreen(

    authService: AuthService,
    repo: HelpiRepository,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit

){

    // Estados para correo y password
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Control para mostrar u ocultar pass
    var passwordVisible by remember { mutableStateOf(false) }

    // Control de mensajes y estados
    var errorMsg by remember { mutableStateOf("") }
    var registroExitoso by remember { mutableStateOf(false) }

    // Estado de carga mientras Firebase responde y scope para lanzar corrutinas
    var cargando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Controles para opciones de accesibilidad (simulados)
    var activarVibracion by remember { mutableStateOf(true)}
    val opcionesContraste = listOf("Normal", "Alto Contraste")
    var contrasteSelect by remember {mutableStateOf(opcionesContraste[0])}

    // Control para mostrar select con opciones de texto
    var expandirMenu by remember { mutableStateOf(false) }
    val opcionesTexto = listOf("Pequeño", "Mediano", "Grande")
    var textoSeleccionado by remember { mutableStateOf(opcionesTexto[1]) }

    // Después de un registro exitoso esperamos 2 segundos y entramos al HomeMenú
    // (Firebase deja la sesión iniciada automáticamente al crear la cuenta)
    LaunchedEffect(key1 = registroExitoso) {

        if (registroExitoso) {
            delay(2.seconds)
            navigateToHome()

        }
    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){
        BannerHelpi()

        Spacer(modifier = Modifier.height(16.dp))

        Text(

            text = "Registro de Usuario",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(

            value = correo,
            onValueChange = {correo = it},
            label = { Text("Correo", fontSize = 18.sp) },
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),

            leadingIcon = {

                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Ícono de correo electrónico"
                )

            },

            keyboardOptions = KeyboardOptions(

                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next

            ),

            singleLine = true,
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(

            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", fontSize = 18.sp) },
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),

            leadingIcon = {

                Icon(

                    imageVector = Icons.Default.Lock,
                    contentDescription = "Ícono de candado de seguridad"

                )

            },
            trailingIcon = {

                val imagen = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                val descripcion = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {

                    Icon(imageVector = imagen, contentDescription = descripcion)

                }
            },

            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(

                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done

            ),

            singleLine = true,
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(

            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()

        ) {
            Checkbox(

                checked = activarVibracion,
                onCheckedChange = { activarVibracion = it }
            )

            Text("Activar vibración en botones", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Preferencia visual:", modifier = Modifier
            .fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        opcionesContraste.forEach { opcion ->

            if(opcion.isBlank()) return@forEach

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(

                        selected = (opcion == contrasteSelect),
                        onClick = { contrasteSelect = opcion }

                    )
            ) {

                RadioButton(

                    selected = (opcion == contrasteSelect),
                    onClick = { contrasteSelect = opcion }
                )

                Text(text = opcion, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {

            OutlinedTextField(

                value = textoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tamaño de texto", fontSize = 18.sp) },
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),

                trailingIcon = {

                    Icon(

                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Desplegar opciones de tamaño de texto"

                    )

                },
                modifier = Modifier.fillMaxWidth()
            )

            Box(

                modifier = Modifier
                    .matchParentSize()
                    .clickable(

                        onClickLabel = "Abrir menú de tamaños de texto"

                    ) { expandirMenu = true }

            )

            DropdownMenu(

                expanded = expandirMenu,
                onDismissRequest = { expandirMenu = false }

            ) {

                opcionesTexto.forEach { seleccion ->

                    DropdownMenuItem(

                        text = { Text(seleccion, fontSize = 16.sp) },
                        onClick = {

                            textoSeleccionado = seleccion
                            expandirMenu = false

                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
        }

        if (registroExitoso) {

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

                        text = "¡Registro exitoso! Ingresando a Helpi...",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp

                    )
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registro: crea la cuenta en Firebase Auth y guarda el perfil en Realtime Database
        Button(
            onClick = {
                scope.launch {
                    cargando = true
                    errorMsg = ""

                    when (val resultado = authService.registrar(correo, password)) {

                        is Resultado.Exito -> {

                            try {
                                // resultado dato es el uid del nuevo usuario (Create del CRUD)
                                repo.guardarPerfil(
                                    resultado.dato,

                                    Usuario(

                                        correo = correo.trim(),
                                        vibracion = activarVibracion,
                                        contraste = contrasteSelect,
                                        tamanoTexto = textoSeleccionado

                                    )
                                )
                            } catch (e: Exception) {

                            }
                            registroExitoso = true
                        }

                        is Resultado.Error -> {

                            errorMsg = resultado.mensaje
                            registroExitoso = false

                        }
                    }
                    cargando = false
                }
            },

            enabled = !cargando && !registroExitoso,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            if (cargando) {

                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 3.dp)

            } else {

                Text("Registrarse", fontSize = 20.sp)

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(

            text = "¿Ya tienes cuenta? Inicia sesión aquí",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,

            modifier = Modifier.clickable {

                navigateBack()

            }
        )

    }
}