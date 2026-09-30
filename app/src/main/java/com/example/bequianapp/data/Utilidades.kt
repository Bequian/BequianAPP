package com.example.bequianapp.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Función de extensión: convierte la fecha guardada (milisegundos) en texto
fun Long.aFechaTexto(): String =
    SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(this))
