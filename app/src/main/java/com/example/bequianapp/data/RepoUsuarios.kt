package com.example.bequianapp.data

import android.util.Patterns

// Data class para usuarios
data class Usuario(
    val correo: String,
    val password: String,
    val vibration: Boolean
)

// Las validaciones usadas en cada screen se agruparon
// Ahora no se repiten en cada una de las Screen

// Función de extensión para validar correo
fun String.esCorreoValido(): Boolean =
    Patterns.EMAIL_ADDRESS.matcher( this ).matches()


// Propiedad de extensión para validar largo de la contraseña
val String.esPasswordValida: Boolean
    get() = this.length >= 6


// Función de orden superior
fun validar(valor: String, regla: (String) -> Boolean): Boolean = regla(valor)


// Excepción para límite de usuarios
class LimiteUsuarioException(mensaje: String) : Exception(mensaje)


// Arreglo para almacenar 10 usuarios en memoria, 5 de ellos ya precargados.
val Usuarios = arrayOfNulls<Usuario>(10).apply {
    this[0] = Usuario(correo = "user1@correo.cl", password = "123123", vibration = true)
    this[1] = Usuario(correo = "user2@correo.cl", password = "123123", vibration = false)
    this[2] = Usuario(correo = "user3@correo.cl", password = "123123", vibration = false)
    this[3] = Usuario(correo = "user4@correo.cl", password = "123123", vibration = true)
    this[4] = Usuario(correo = "user5@correo.cl", password = "123123", vibration = false)
}


// Funciones para buscar usuarios registrados por correo
fun buscarUsuario(correo: String): Usuario? =
    Usuarios.find { it?.correo == correo }


// Función para agregar usuarios en el primer espacio vacío del arreglo
// Verifica que hay espacio disponible y si no ejecuta LimiteUsuarioException
fun agregarUsuarios(correo: String, password: String, vibration: Boolean): Usuario{

    val indiceVacio = Usuarios.indexOfFirst { it == null }

    if ( indiceVacio == -1 ){

        throw LimiteUsuarioException("Limite de Usuarios alcanzado (Máx 10).")

    }

    val nuevoUsuario = Usuario(correo = correo, password = password, vibration = vibration)
    Usuarios[indiceVacio] = nuevoUsuario
    return nuevoUsuario

}