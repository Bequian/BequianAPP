package com.example.bequianapp

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.database.database

// Clase Application: se ejecuta una sola vez al abrir la app, antes que cualquier pantalla.
class HelpiApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Guarda en el equipo una copia local de los datos de Firebase,
        // así la app sigue mostrando la información si se pierde la conexión por un momento.
        Firebase.database.setPersistenceEnabled(true)
    }
}
