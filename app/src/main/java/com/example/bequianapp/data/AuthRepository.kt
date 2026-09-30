package com.example.bequianapp.data

import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

// Excepción propia de la app (reemplaza a LimiteUsuarioException de la S5).
// Lleva un mensaje ya traducido y listo para mostrar al usuario.
class AuthException(mensaje: String) : Exception(mensaje)

interface AuthRepository {
    fun uidActual(): String?
    fun correoActual(): String?
    suspend fun iniciarSesion(correo: String, password: String): String
    suspend fun registrar(correo: String, password: String): String
    suspend fun enviarRecuperacion(correo: String)
    suspend fun eliminarCuenta()
    fun cerrarSesion()
}

// Implementación real con Firebase Authentication (correo y contraseña)
class FirebaseAuthRepository(
    private val auth: FirebaseAuth = Firebase.auth
) : AuthRepository {

    override fun uidActual(): String? = auth.currentUser?.uid

    override fun correoActual(): String? = auth.currentUser?.email

    override suspend fun iniciarSesion(correo: String, password: String): String = traducirErrores {

        val resultado = auth.signInWithEmailAndPassword(correo, password).await()
        resultado.user?.uid ?: throw AuthException("No se pudo obtener el usuario.")

    }

    override suspend fun registrar(correo: String, password: String): String = traducirErrores {

        val resultado = auth.createUserWithEmailAndPassword(correo, password).await()
        resultado.user?.uid ?: throw AuthException("No se pudo crear el usuario.")

    }

    override suspend fun enviarRecuperacion(correo: String) = traducirErrores {

        auth.sendPasswordResetEmail(correo).await()
        Unit

    }

    override suspend fun eliminarCuenta() = traducirErrores {

        auth.currentUser?.delete()?.await()
        Unit

    }

    override fun cerrarSesion() = auth.signOut()

    // Función de orden superior, ejecuta accion y traduce errores de Firebase en texto
    private suspend fun <T> traducirErrores(accion: suspend () -> T): T {
        try {
            return accion()
        } catch (e: FirebaseAuthWeakPasswordException) {

            throw AuthException("La contraseña debe tener al menos 6 caracteres.")

        } catch (e: FirebaseAuthUserCollisionException) {

            throw AuthException("El correo ya se encuentra registrado.")

        } catch (e: FirebaseAuthRecentLoginRequiredException) {

            throw AuthException("Por seguridad, cierra sesión, vuelve a ingresar e inténtalo de nuevo.")

        } catch (e: FirebaseAuthInvalidUserException) {

            throw AuthException("El usuario no existe. Por favor, regístrate.")

        } catch (e: FirebaseAuthInvalidCredentialsException) {

            throw AuthException("Correo o contraseña incorrectos.")

        } catch (e: FirebaseNetworkException) {

            throw AuthException("Sin conexión a internet. Revisa tu red e inténtalo de nuevo.")

        } catch (e: FirebaseTooManyRequestsException) {

            throw AuthException("Demasiados intentos. Espera un momento e inténtalo de nuevo.")

        }
    }
}
