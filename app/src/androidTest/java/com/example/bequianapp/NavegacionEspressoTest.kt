package com.example.bequianapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bequianapp.navigation.NavigationWrapper
import com.example.bequianapp.ui.theme.BequianAPPTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// 5 pruebas instrumentadas (UI) con Espresso + Compose.
// Se ejecutan en un emulador, en un teléfono o en Firebase Test Lab.
// La última prueba se conecta de verdad a Firebase Authentication.
@RunWith(AndroidJUnit4::class)
class NavegacionEspressoTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        // Cada prueba parte sin sesión iniciada, para que la app abra en Login
        Firebase.auth.signOut()
        composeRule.setContent {
            BequianAPPTheme {
                NavigationWrapper()
            }
        }
    }

    @Test
    fun appInicia_enPantallaLogin() {
        composeRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }

    @Test
    fun navegarARegistro_yVolverConBotonAtras() {
        composeRule.onNodeWithText("Crear una cuenta nueva").performClick()
        composeRule.onNodeWithText("Registro de Usuario").assertIsDisplayed()

        // Espresso simula el botón "atrás" del teléfono
        Espresso.pressBack()

        composeRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }

    @Test
    fun registroSinDatos_muestraError() {
        composeRule.onNodeWithText("Crear una cuenta nueva").performClick()

        composeRule.onNodeWithText("Registrarse").performScrollTo().performClick()

        composeRule.onNodeWithText("Por favor, completa todos los campos.")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun navegarARecuperarContrasena() {
        composeRule.onNodeWithText("¿Olvidaste tu contraseña?").performClick()
        composeRule.onNodeWithText("Recuperar Contraseña").assertIsDisplayed()
    }

    @Test
    fun loginConCredencialesIncorrectas_FirebaseRespondeConError() {
        composeRule.onNodeWithText("Correo Electrónico").performTextInput("prueba.testlab@helpi.cl")
        composeRule.onNodeWithText("Contraseña").performTextInput("clave-incorrecta")
        composeRule.onNodeWithText("Ingresar").performClick()

        // Espera hasta 15 segundos la respuesta de Firebase
        val mensajesPosibles = listOf(
            "Correo o contraseña incorrectos.",
            "El usuario no existe. Por favor, regístrate."
        )
        composeRule.waitUntil(timeoutMillis = 15_000) {
            mensajesPosibles.any { composeRule.onAllNodesWithText(it).fetchSemanticsNodes().isNotEmpty() }
        }
    }
}
