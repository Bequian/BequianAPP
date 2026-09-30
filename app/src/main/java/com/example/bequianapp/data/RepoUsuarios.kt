package com.example.bequianapp.data

// ---------------------------------------------------------------
// MODELOS DE DATOS (se guardan en Firebase Realtime Database)
// Firebase necesita:
//  - valores por defecto en todos los campos (constructor vacío)
//  - propiedades "var" para poder asignar los valores al leerlos
// ---------------------------------------------------------------

// Perfil del usuario: usuarios/{uid}/perfil
data class Usuario(
    var correo: String = "",
    var vibracion: Boolean = true,
    var contraste: String = "Normal",
    var tamanoTexto: String = "Mediano"
)

// Mensajes escritos o dictados: usuarios/{uid}/mensajes/{id}
data class Mensaje(
    var id: String = "",
    var texto: String = "",
    var fecha: Long = 0L
)

// Frases rápidas para la vista Hablar: usuarios/{uid}/frases/{id}
data class Frase(
    var id: String = "",
    var texto: String = ""
)

// Expresión regular propia para el correo.
private val REGEX_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

// Función de extensión para validar correo
fun String.esCorreoValido(): Boolean = REGEX_CORREO.matches(this.trim())

// Propiedad de extensión para validar largo de la contraseña (Firebase exige mínimo 6)
val String.esPasswordValida: Boolean
    get() = this.length >= 6

// Función de orden superior
fun validar(valor: String, regla: (String) -> Boolean): Boolean = regla(valor)
