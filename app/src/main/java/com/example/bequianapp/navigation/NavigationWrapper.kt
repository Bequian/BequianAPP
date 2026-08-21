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


@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login ){

        composable<Login> {
            LoginScreen (
                { navController.navigate(Registro) },
                { navController.navigate(Recuperar) },
                { correo -> navController.navigate(Home(correo = correo))}
            )

        }

        composable<Registro> {

            RegistroScreen{ navController.navigate(Login){
                popUpTo<Login>{inclusive = true}
            } }

        }

        composable<Recuperar> {

            RecuperarPassScreen{ navController.navigate(Login){

                popUpTo<Login>{inclusive = true}

            }}

        }

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