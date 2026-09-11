package com.example.bequianapp.navigation
import kotlinx.serialization.Serializable

// Objetos para definir rutas de pantalla
@Serializable
object Login

@Serializable
object Registro

@Serializable
object Recuperar

// Data class para definir ruta HOME con parametro correo
@Serializable
data class Home (val correo: String, val vibration: Boolean)