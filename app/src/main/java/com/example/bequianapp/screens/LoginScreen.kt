package com.example.bequianapp.screens

import android.util.Patterns
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.bequianapp.R



@Composable
fun LoginScreen( navigateToRegistro: () -> Unit, navigateToRecuperar: () -> Unit ){

    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }

    var errorMsg by remember { mutableStateOf("")}
    var loginExito by remember { mutableStateOf(false)}

    val logoActual =
        if (isSystemInDarkTheme()){
            R.drawable.helpi_logo_darkmode
        } else {
            R.drawable.helpi_logo
        }

    Column(

        modifier = Modifier

            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        Image(

            painter = painterResource(id = logoActual),
            contentDescription = "Logotipo de la aplicación Helpi: Conectándote con el mundo",
            modifier = Modifier

                .size(250.dp)
                .padding(bottom = 5.dp)

        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(

            text = "Iniciar Sesión",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(

            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo Electrónico", fontSize = 18.sp) },
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),

            leadingIcon = {

                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Ícono de correo"
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
                    contentDescription = "Ícono de candado"

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

        if (loginExito) {
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

                        text = "¡Login Correcto! Ingresando al sistema...",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp

                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                if(correo.isBlank() || password.isBlank()){

                    errorMsg = "Por favor, completa todos los campos"
                    loginExito = false

                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){

                    errorMsg = "Por favor, ingresa un correo válido (Ej: nombre@correo.cl)"
                    loginExito = false

                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {

            Text("Ingresar", fontSize = 20.sp)

        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {

                navigateToRegistro()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)

        ) {

            Text("Crear una cuenta nueva", fontSize = 20.sp)

        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {

                navigateToRecuperar()

            }
        ) {
            Text("¿Olvidaste tu contraseña?", fontSize = 16.sp)
        }

    }
}