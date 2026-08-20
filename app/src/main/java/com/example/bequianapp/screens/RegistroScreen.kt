package com.example.bequianapp.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bequianapp.R
import com.example.bequianapp.data.Usuarios

@Composable
fun RegistroScreen( navController: NavController){

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf("") }
    var registroExitoso by remember { mutableStateOf(false) }

    var activarVibracion by remember { mutableStateOf(true)}

    val opcionesContraste = listOf("Normal", "Alto Contrase")
    var contrasteSelect by remember {mutableStateOf(opcionesContraste[0])}

    var expandirMenu by remember { mutableStateOf(false) }
    val opcionesTexto = listOf("Pequeño", "Mediano", "Grande")
    var textoSeleccionado by remember { mutableStateOf(opcionesTexto[1]) }


    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){
        Image(

            painter = painterResource(id = R.drawable.helpi_banner),
            contentDescription = "Logotipo de la aplicación Helpi: Conectándote con el mundo",
            modifier = Modifier
                .fillMaxWidth()
                .size(120.dp)
                .padding(top = 16.dp),
            contentScale = ContentScale.Fit

        )
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

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMsg.isNotEmpty()) {

            Text(text = errorMsg, color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        }
        if (registroExitoso) {

            Text(text = "¡Registro exitoso!", color = Color.Green, fontWeight = FontWeight.Bold, fontSize = 16.sp)

        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(

            onClick = {

                if (correo.isBlank() || password.isBlank()) {

                    errorMsg = "Por favor, completa todos los campos."
                    registroExitoso = false

                } else if (password.length < 6) {

                    errorMsg = "La contraseña debe tener al menos 6 caracteres."
                    registroExitoso = false

                } else {

                    val indiceVacio = Usuarios.indexOfFirst { it == null }

                    if (indiceVacio != -1) {

                        Usuarios[indiceVacio] = correo
                        errorMsg = ""
                        registroExitoso = true

                    } else {

                        errorMsg = "Límite de usuarios alcanzado (Máx 5)."
                        registroExitoso = false

                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {

            Text("Registrarse", fontSize = 20.sp)

        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(

            text = "¿Ya tienes cuenta? Inicia sesión aquí",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            modifier = Modifier.clickable {

                navController.popBackStack()

            }
        )

    }
}