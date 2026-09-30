package com.example.bequianapp.data

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.database.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

// Función de extensión genérica (KTX + reified):
// escucha un nodo en tiempo real y lo convierte en una lista de objetos Kotlin.
// Si falla (por ejemplo, al cerrar sesión), entrega una lista vacía en vez de cerrar la app.
private inline fun <reified T : Any> DatabaseReference.comoLista(): Flow<List<T>> =
    snapshots
        // Se usa T::class.java (y no getValue<T>()) porque, dentro de una función genérica,
        // getValue<T>() pierde el tipo y Firebase devuelve un HashMap en vez del objeto.
        .map { snapshot -> snapshot.children.mapNotNull { it.getValue(T::class.java) } }
        .catch { emit(emptyList()) }


// Repositorio de datos: todas las operaciones CRUD contra Firebase Realtime Database.
// Estructura: usuarios/{uid}/perfil | mensajes | frases
class HelpiRepository(

    private val db: FirebaseDatabase = Firebase.database

) {

    private fun usuarioRef(uid: String): DatabaseReference =
        db.reference.child("usuarios").child(uid)


    // CREA YA ACTUALIZA PERFIL
    suspend fun guardarPerfil(uid: String, usuario: Usuario) {
        usuarioRef(uid).child("perfil").setValue(usuario).await()
    }

    // LEE EL PERFIL DEL USUARIO
    fun perfil(uid: String): Flow<Usuario> =

        usuarioRef(uid).child("perfil").snapshots

            .map { it.getValue<Usuario>() ?: Usuario() }
            .catch { emit(Usuario()) }

    // CAMBIA OPCION DE VIBRACION
    suspend fun actualizarVibracion(uid: String, activa: Boolean) {

        usuarioRef(uid).child("perfil").child("vibracion").setValue(activa).await()

    }

    // Delete: borra todos los datos del usuario
    suspend fun eliminarDatosUsuario(uid: String) {

        usuarioRef(uid).removeValue().await()

    }

    // CRUD TEXTOS EN SCREEN ESCRIBIR

    fun mensajes(uid: String): Flow<List<Mensaje>> =

        usuarioRef(uid).child("mensajes").comoLista<Mensaje>()

            .map { lista -> lista.sortedByDescending { it.fecha } }

    suspend fun guardarMensaje(uid: String, texto: String) {

        val ref = usuarioRef(uid).child("mensajes").push()
        val mensaje = Mensaje(id = ref.key ?: "", texto = texto, fecha = System.currentTimeMillis())
        ref.setValue(mensaje).await()

    }

    suspend fun actualizarMensaje(uid: String, mensaje: Mensaje) {

        usuarioRef(uid).child("mensajes").child(mensaje.id).setValue(mensaje).await()

    }

    suspend fun eliminarMensaje(uid: String, id: String) {

        usuarioRef(uid).child("mensajes").child(id).removeValue().await()

    }

    // CRUD FRASES SCREEN HABLAR

    fun frases(uid: String): Flow<List<Frase>> =
        usuarioRef(uid).child("frases").comoLista<Frase>()

    suspend fun guardarFrase(uid: String, texto: String) {

        val ref = usuarioRef(uid).child("frases").push()
        ref.setValue(Frase(id = ref.key ?: "", texto = texto)).await()

    }

    suspend fun actualizarFrase(uid: String, frase: Frase) {

        usuarioRef(uid).child("frases").child(frase.id).setValue(frase).await()

    }

    suspend fun eliminarFrase(uid: String, id: String) {

        usuarioRef(uid).child("frases").child(id).removeValue().await()

    }

}
