package com.example.bequianapp.data

import kotlinx.coroutines.CancellationException

// Resultado de una operación: Exito con un dato, o Error con un mensaje para mostrar
sealed class Resultado<out T> {
    data class Exito<T>(val dato: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}

// Servicio de autenticación: valida los datos ANTES de llamar a Firebase
// y centraliza el manejo de errores. Las pantallas solo hablan con esta clase.
class AuthService(private val repo: AuthRepository) {

    fun sesionActiva(): Boolean = repo.uidActual() != null

    fun uidActual(): String? = repo.uidActual()

    fun correoActual(): String = repo.correoActual() ?: ""

    // Devuelve el mensaje de error, o null si los datos son válidos
    fun validarCredenciales(correo: String, password: String): String? = when {

        !validar(correo) { it.isNotBlank() } || !validar(password) { it.isNotBlank() } ->
            "Por favor, completa todos los campos."

        !validar(correo, String::esCorreoValido) ->
            "Por favor, ingresa un correo válido (Ej: nombre@correo.cl)."

        !password.esPasswordValida ->
            "La contraseña debe tener al menos 6 caracteres."
        else -> null

    }

    suspend fun login(correo: String, password: String): Resultado<String> {

        validarCredenciales(correo, password)?.let { return Resultado.Error(it) }
        return ejecutar { repo.iniciarSesion(correo.trim(), password) }

    }

    suspend fun registrar(correo: String, password: String): Resultado<String> {

        validarCredenciales(correo, password)?.let { return Resultado.Error(it) }
        return ejecutar { repo.registrar(correo.trim(), password) }


    }

    suspend fun recuperar(correo: String): Resultado<Unit> {

        if (!validar(correo) { it.isNotBlank() }) return Resultado.Error("Por favor, ingresa los datos solicitados.")
        if (!correo.esCorreoValido()) return Resultado.Error("Por favor, ingresa un correo válido.")
        return ejecutar { repo.enviarRecuperacion(correo.trim()) }

    }

    suspend fun eliminarCuenta(): Resultado<Unit> = ejecutar { repo.eliminarCuenta() }

    fun cerrarSesion() = repo.cerrarSesion()

    // Función de orden superior: ejecuta la acción y transforma las excepciones en Resultado. Error
    private suspend fun <T> ejecutar(accion: suspend () -> T): Resultado<T> =
        try {

            Resultado.Exito(accion())

        } catch (e: CancellationException) {

            throw e // se deja pasar la cancelación normal de corrutinas

        } catch (e: AuthException) {

            Resultado.Error(e.message ?: "Error de autenticación.")

        } catch (e: Exception) {

            Resultado.Error("Ocurrió un error inesperado. Inténtalo de nuevo.")

        }
}
