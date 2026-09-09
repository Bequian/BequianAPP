package com.example.bequianapp.data

import android.util.Patterns

// Data class para usuarios
data class Usuario(
    val correo: String,
    val password: String
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


// Arreglo para almacenar 5 usuarios en memoria
val Usuarios = arrayOfNulls<Usuario>(5).apply {
    this[0] = Usuario(correo = "asd@asd.cl", password = "123123")
}


// Funciones para buscar usuarios registrados por correo
fun buscarUsuario(correo: String): Usuario? =
    Usuarios.find { it?.correo == correo }


// Función para agregar usuarios en el primer espacio vacío del arreglo
// Verifica que hay espacio disponible y si no ejecuta LimiteUsuarioException
fun agregarUsuarios(correo: String, password: String): Usuario{

    val indiceVacio = Usuarios.indexOfFirst { it == null }

    if ( indiceVacio == -1 ){

        throw LimiteUsuarioException("Limite de Usuarios alcanzado (Máx 5).")

    }

    val nuevoUsuario = Usuario(correo = correo, password = password)
    Usuarios[indiceVacio] = nuevoUsuario
    return nuevoUsuario

}