package com.example.bequianapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bequianapp.screens.LoginScreen
import com.example.bequianapp.screens.RecuperarPassScreen
import com.example.bequianapp.screens.RegistroScreen


@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login){

        composable<Login> {
            LoginScreen(navController, navController.navigate(RecuperarPassScreen()))
        }

        composable<Registro> {

            RegistroScreen(navController)

        }

        composable<Recuperar> {

            RecuperarPassScreen(navController)

        }
    }

}