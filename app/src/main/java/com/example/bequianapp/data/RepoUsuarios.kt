package com.example.bequianapp.data

// Data class para usaurios
data class Usuario(
    val correo: String,
    val password: String
)

// Arreglo para almacenar 5 Usuarios
val Usuarios = arrayOfNulls<Usuario>(5)
