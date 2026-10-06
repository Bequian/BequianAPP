package com.example.bequianapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bequianapp.data.AuthService
import com.example.bequianapp.data.FirebaseAuthRepository
import com.example.bequianapp.data.HelpiRepository
import com.example.bequianapp.screens.AyudaScreen
import com.example.bequianapp.screens.BienvenidaScreen
import com.example.bequianapp.screens.BuscarDispositivoScreen
import com.example.bequianapp.screens.EscribirScreen
import com.example.bequianapp.screens.HablarScreen
import com.example.bequianapp.screens.HomeScreen
import com.example.bequianapp.screens.LoginScreen
import com.example.bequianapp.screens.RecuperarPassScreen
import com.example.bequianapp.screens.RegistroScreen

// Función de extensión: navega a una ruta borrando todo el historial
// (se usa al iniciar y al cerrar sesión, para que "atrás" no vuelva a la pantalla anterior)
fun NavHostController.navegarLimpiando(ruta: Any) {
    navigate(ruta) {
        popUpTo(graph.id) { inclusive = true }
    }
}

// Función principal que gestiona la navegación de la app
@Composable
fun NavigationWrapper() {

    val navController = rememberNavController()

    // Servicios de datos: se crean una sola vez y se comparten con las pantallas
    val authService = remember { AuthService(FirebaseAuthRepository()) }
    val repo = remember { HelpiRepository() }

    // Si Firebase recuerda una sesión activa, se abre directamente la Bienvenida
    val inicio: Any = remember { if (authService.sesionActiva()) Bienvenida else Login }

    NavHost(navController = navController, startDestination = inicio) {

        // Config ruta pantalla login
        composable<Login> {
            LoginScreen(
                authService = authService,
                navigateToRegistro = { navController.navigate(Registro) },
                navigateToRecuperar = { navController.navigate(Recuperar) },
                navigateToHome = { navController.navegarLimpiando(Bienvenida) }
            )
        }

        // Config ruta pantalla Registro
        composable<Registro> {
            RegistroScreen(
                authService = authService,
                repo = repo,
                navigateBack = { navController.popBackStack() },
                navigateToHome = { navController.navegarLimpiando(Bienvenida) }
            )
        }

        // Config ruta pantalla Recuperar
        composable<Recuperar> {
            RecuperarPassScreen(
                authService = authService,
                navigateBack = { navController.popBackStack() }
            )
        }

        // Config ruta Bienvenida (después de iniciar sesión o registrarse)
        composable<Bienvenida> {
            BienvenidaScreen(
                authService = authService,
                navigateToHome = { navController.navegarLimpiando(Home) }
            )
        }

        // Config ruta HomeMenú
        composable<Home> {
            HomeScreen(
                authService = authService,
                repo = repo,
                navigateToEscribir = { navController.navigate(Escribir) },
                navigateToHablar = { navController.navigate(Hablar) },
                navigateToBuscar = { navController.navigate(BuscarDispositivo) },
                navigateToAyuda = { navController.navigate(Ayuda) },
                onLogout = { navController.navegarLimpiando(Login) }
            )
        }

        composable<Escribir> {
            EscribirScreen(
                authService = authService,
                repo = repo,
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<Hablar> {
            HablarScreen(
                authService = authService,
                repo = repo,
                navigateBack = { navController.popBackStack() }
            )
        }

        composable<BuscarDispositivo> {
            BuscarDispositivoScreen(
                authService = authService,
                repo = repo,
                navigateBack = { navController.popBackStack() }
            )
        }

        // Config ruta Ayuda: el botón vuelve al menú principal
        composable<Ayuda> {
            AyudaScreen(
                navigateToHome = { navController.popBackStack() }
            )
        }
    }
}
