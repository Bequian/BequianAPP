package com.example.bequianapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bequianapp.screens.HomeScreen
import com.example.bequianapp.screens.LoginScreen
import com.example.bequianapp.screens.RecuperarPassScreen
import com.example.bequianapp.screens.RegistroScreen


// Función principal que gestiona la navegación de la app
@Composable
fun NavigationWrapper() {

    val navController = rememberNavController()
    // Definición del NavHost con la pantalla de Login como inicio
    NavHost(navController = navController, startDestination = Login ){

        // Config ruta pantalla login
        composable<Login> {
            LoginScreen (
                navigateToRegistro = { navController.navigate(Registro) },
                navigateToRecuperar = { navController.navigate(Recuperar) },
                navigateToHome = { correo -> navController.navigate(Home(correo = correo))}
            )

        }

        // Config ruta pantalla Registro
        composable<Registro> {
            RegistroScreen{ 
                navController.navigate(Login){
                    popUpTo<Login>{inclusive = true} // Limpia el historial para no volver atrás al registrarse
                } 
            }
        }

        // Config ruta pantalla Recuperar
        composable<Recuperar> {

            RecuperarPassScreen{ navController.navigate(Login){

                popUpTo<Login>{inclusive = true}

            }}

        }

        // Config ruta pantalla Home, incluye el parametro correo para mostrar.
        composable<Home> { backStackEntry ->

            val homeData = backStackEntry.toRoute<Home>()

            HomeScreen(
                correoUsuario = homeData.correo,
                onLogout = {

                    navController.navigate(Login) {
                        popUpTo<Login>{ inclusive = true }

                    }
                }
            )
        }

    }
}