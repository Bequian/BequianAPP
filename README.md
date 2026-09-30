# Helpi - App de Accesibilidad

Proyecto desarrollado para la asignatura **Desarrollo de Aplicaciones Móviles (DSY2204)** de Duoc UC.

Helpi es una aplicación móvil Android enfocada en la accesibilidad, diseñada para facilitar la comunicación (escribir y hablar) de personas con discapacidad sensorial auditiva en su entorno cotidiano.

**Versión actual:** 1.3.0 (versionCode 3) · **Tecnologías:** Kotlin, Jetpack Compose, Material Design 3, Navigation Compose, Firebase Authentication y Firebase Realtime Database.

---

**Usuario para pruebas** (Se pueden registrar nuevos usuarios)
correo: user1@correo.cl
passowrd: 123123

## Actualización 1.3 (Semana 8 - Sumativa 3)

En esta entrega la app deja de ser un prototipo con datos en memoria: se integra el **front end** con un **back end real en Firebase** y se prepara la app para su distribución.

* **Firebase Authentication** (correo y contraseña) reemplaza al arreglo de usuarios en memoria: registro, inicio de sesión, recuperación de contraseña real por correo y sesión persistente.
* **Firebase Realtime Database** guarda el perfil, los mensajes y las frases de cada usuario, con operaciones CRUD y lectura en tiempo real.
* **4 vistas nuevas o renovadas:** HomeMenú, Escribir, Hablar y BuscarDispositivo.
* **5 pruebas instrumentadas** con Espresso, ejecutadas en Firebase Test Lab.
* **APK firmado** con clave de liberación y **publicado en Firebase App Distribution**.

---

## Pantallas

| Pantalla | Descripción |
|---|---|
| [LoginScreen.kt](app/src/main/java/com/example/bequianapp/screens/LoginScreen.kt) | Inicio de sesión con Firebase Auth. Valida los campos antes de consultar a Firebase, muestra un indicador de carga y mensajes de error en español. |
| [RegistroScreen.kt](app/src/main/java/com/example/bequianapp/screens/RegistroScreen.kt) | Crea la cuenta en Firebase Auth y guarda el perfil con las preferencias (vibración, contraste y tamaño de texto) en Realtime Database. |
| [RecuperarPassScreen.kt](app/src/main/java/com/example/bequianapp/screens/RecuperarPassScreen.kt) | Recuperación **real** por correo (Firebase envía el enlace). La opción SMS se mantiene simulada. |
| [HomeScreen.kt](app/src/main/java/com/example/bequianapp/screens/HomeScreen.kt) | HomeMenú con tarjetas hacia Escribir, Hablar y Buscar dispositivo. Permite activar o desactivar la vibración (se guarda en Firebase), cerrar sesión y eliminar la cuenta con confirmación. |
| [EscribirScreen.kt](app/src/main/java/com/example/bequianapp/screens/EscribirScreen.kt) | Escribir o **dictar** un mensaje (voz a texto), mostrarlo en **pantalla completa** con letras grandes, compartirlo y guardarlo en Firebase (crear, listar, editar y eliminar). |
| [HablarScreen.kt](app/src/main/java/com/example/bequianapp/screens/HablarScreen.kt) | El teléfono **lee en voz alta** el texto escrito (TextToSpeech en español), con velocidad ajustable mediante un Slider. Frases rápidas guardadas en Firebase (crear, listar, editar y eliminar). |
| [BuscarDispositivoScreen.kt](app/src/main/java/com/example/bequianapp/screens/BuscarDispositivoScreen.kt) | Vista **simulada**: el botón "Hacer sonar este teléfono" emite un tono de alarma, hace vibrar el equipo y muestra un aviso que parpadea en pantalla, para que también se perciba visualmente. |

---

## Estructura del proyecto

Código dividido en paquetes lógicos dentro de `app/src/main/java/com/example/bequianapp/`:

* **`HelpiApp.kt`**: clase `Application` que activa la persistencia sin conexión de Firebase.
* **`/screens`**: las 7 pantallas de la app y [Componentes.kt](app/src/main/java/com/example/bequianapp/screens/Componentes.kt) (banner, encabezado con botón volver y mensajes de error/éxito reutilizables).
* **`/navigation`**
    * [Screens.kt](app/src/main/java/com/example/bequianapp/navigation/Screens.kt): rutas `@Serializable` de cada pantalla.
    * [NavigationWrapper.kt](app/src/main/java/com/example/bequianapp/navigation/NavigationWrapper.kt): `NavHost` y `NavController`. Si hay una sesión activa, la app abre directamente en el HomeMenú. La extensión `navegarLimpiando()` borra el historial al iniciar y cerrar sesión.
* **`/data`**
    * [RepoUsuarios.kt](app/src/main/java/com/example/bequianapp/data/RepoUsuarios.kt): modelos `Usuario`, `Mensaje` y `Frase`, y las validaciones de la S5 (`esCorreoValido()`, `esPasswordValida`, `validar()`).
    * [AuthRepository.kt](app/src/main/java/com/example/bequianapp/data/AuthRepository.kt): interfaz de autenticación, su implementación con Firebase y la excepción propia `AuthException`, que traduce los errores de Firebase a mensajes en español.
    * [AuthService.kt](app/src/main/java/com/example/bequianapp/data/AuthService.kt): valida los datos antes de llamar a Firebase y devuelve un `Resultado` (clase sellada `Exito` / `Error`).
    * [HelpiRepository.kt](app/src/main/java/com/example/bequianapp/data/HelpiRepository.kt): todas las operaciones CRUD contra Realtime Database.
    * [Utilidades.kt](app/src/main/java/com/example/bequianapp/data/Utilidades.kt): extensión `Long.aFechaTexto()` para mostrar fechas.

---

## Firebase

**Authentication:** método correo electrónico / contraseña.

**Realtime Database:** cada usuario solo puede leer y escribir su propio nodo.

```
usuarios
  └─ {uid}
       ├─ perfil          → correo, vibracion, contraste, tamanoTexto
       ├─ mensajes/{id}   → id, texto, fecha      (vista Escribir)
       └─ frases/{id}     → id, texto             (vista Hablar)
```

Reglas de seguridad (resumen):

```json
"usuarios":{
  "$uid": {
    ".read":  "auth != null && auth.uid === $uid",
    ".write": "auth != null && auth.uid === $uid"
  }
}
```

---

## Extensiones KTX utilizadas

* `Firebase.auth` y `Firebase.database`: inicialización KTX de Firebase.
* `Query.snapshots`: cambios de la base de datos en tiempo real como `Flow`.
* `DataSnapshot.getValue<T>()`: lectura tipada del perfil.
* `Task.await()` (kotlinx-coroutines-play-services): operaciones de Firebase con corrutinas, sin callbacks.
* `Context.getSystemService<Vibrator>()` (core-ktx): vibración en BuscarDispositivo.
* `rememberLauncherForActivityResult` (activity-compose): reconocimiento de voz en Escribir.
* Extensiones propias: `DatabaseReference.comoLista<T>()`, `NavHostController.navegarLimpiando()`, `Long.aFechaTexto()`, `String.esCorreoValido()` y `String.esPasswordValida`.

## Componentes de la interfaz

* **ViewGroups:** `Column`, `Row`, `Box`, `Card`, `Surface`, `LazyColumn` y `LazyVerticalGrid`.
* **Palette texts y buttons:** `Text`, `OutlinedTextField`, `Button`, `OutlinedButton`, `TextButton` e `IconButton`.
* **Palette widgets:** `Switch`, `Slider`, `Checkbox`, `RadioButton`, `DropdownMenu`, `AlertDialog` y `CircularProgressIndicator`.

---

## Pruebas (Firebase Test Lab)

[NavegacionEspressoTest.kt](app/src/androidTest/java/com/example/bequianapp/NavegacionEspressoTest.kt) contiene 5 pruebas instrumentadas con Espresso + Compose:

1. `appInicia_enPantallaLogin`: sin sesión, la app abre en Login.
2. `navegarARegistro_yVolverConBotonAtras`: Login → Registro → botón atrás (`Espresso.pressBack()`).
3. `registroSinDatos_muestraError`: validación del formulario de registro vacío.
4. `navegarARecuperarContrasena`: Login → Recuperar contraseña.
5. `loginConCredencialesIncorrectas_FirebaseRespondeConError`: conexión real con Firebase Auth y mensaje de error traducido.

**Ejecutar en el emulador:** clic derecho sobre la clase → *Run 'NavegacionEspressoTest'*.

**Ejecutar en Firebase Test Lab:**

```
.\gradlew assembleDebug assembleDebugAndroidTest
```

Luego, en la consola de Firebase → Test Lab → *Instrumentación*, subir `app/build/outputs/apk/debug/app-debug.apk` y `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk`.

---

## Versionado, firma y distribución

* Versionado en `app/build.gradle.kts`: `versionCode = 3`, `versionName = "1.3.0"`.
* APK de release generado desde *Build → Generate Signed App Bundle or APK → APK*, firmado con una clave de liberación propia. El keystore se guarda fuera del repositorio.
* Distribución gratuita mediante **Firebase App Distribution**.

---

## Restricciones

* Requiere conexión a internet para registrarse, iniciar sesión y sincronizar los datos.
* BuscarDispositivo es simulado: no usa geolocalización ni Bluetooth real (la geolocalización no forma parte de la evaluación).
* La recuperación por SMS es simulada, ya que la autenticación por teléfono de Firebase requiere un plan de pago.
* El dictado depende del reconocimiento de voz de Google, y Hablar depende del motor Texto a Voz del equipo con el idioma español.
* Las preferencias de contraste y tamaño de texto se guardan en el perfil, pero aún no se aplican visualmente; la vibración sí se aplica.

---

## Consideraciones de accesibilidad

* Textos e inputs de mayor tamaño (16 a 44 sp) y botones grandes.
* Íconos con `contentDescription` y `onClickLabel` para lectores de pantalla.
* Mensajes de error y éxito visibles con ícono y color.
* Pantalla completa con letras grandes, lectura en voz alta, transcripción de voz y aviso visual al hacer sonar el teléfono.
* Vibración opcional en los botones del menú.

---

## Historial de versiones

| Versión | Semana | Cambios |
|---|---|---|
| 1.0 | S2 | Pantallas Login, Registro y Recuperar contraseña con Jetpack Compose y Material Design. Navegación con Navigation Compose. |
| 1.1 | S5 | Funciones de extensión, función de orden superior y manejo de excepciones en Kotlin. Arreglo de 5 usuarios en memoria. |
| 1.2 | S6 | HomeScreen con prueba de vibración según la preferencia del usuario. |
| 1.3 | S8 | Firebase Auth y Realtime Database, vistas HomeMenú, Escribir, Hablar y BuscarDispositivo, pruebas en Firebase Test Lab, APK firmado y publicado en Firebase App Distribution. |



# Helpi - Actualziación 1.2

* **[HomeScreen.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/screens/HomeScreen.kt)**: Recibe la preferencia `vibrationUsuario` del usuario conectado (leída desde `RepoUsuarios.kt` a través de la navegación) y muestra un botón de prueba que activa la vibración del dispositivo mediante `LocalHapticFeedback` solo si esa preferencia está activada.

* **[RepoUsuarios.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/data/RepoUsuarios.kt)**: Ahora contiene toda la lógica de validación y gestion de usuarios.

**Funciones y propiedades agregadas en RepoUsuarios.kt:**

* `Usuario` (data class): modelo de usuario con los campos `correo`, `password` y `vibration`.

* `esCorreoValido()` (función de extensión sobre `String`): valida que el texto tenga formato de correo electrónico.

* `esPasswordValida` (propiedad de extensión sobre `String`): valida que la contraseña tenga al menos 6 caracteres.

* `validar(valor, regla)` (función de orden superior): recibe un valor y una regla de validación (lambda) y devuelve si el valor la cumple. Se usa para no repetir la misma validación en Login, Registro y Recuperar.

* `LimiteUsuarioException` (excepción propia): se lanza cuando se intenta registrar un usuario y ya no hay espacio disponible en el arreglo.

* `Usuarios` (arreglo en memoria): almacena los usuarios registrados, con los 5 usuarios de prueba precargados.

* `buscarUsuario(correo)`: busca un usuario dentro del arreglo por su correo y devuelve el `Usuario` encontrado (o `null` si no existe).

* `agregarUsuarios(correo, password, vibration)`: agrega un nuevo usuario en el primer espacio vacío del arreglo; si ya no queda espacio, lanza `LimiteUsuarioException`.

**Usuarios registrados en Array:**

Correo: user1@correo.cl - Contraseña: 123123 - Opción Vibración: True

Correo: user2@correo.cl - Contraseña: 123123 - Opción Vibración: false

Correo: user3@correo.cl - Contraseña: 123123 - Opción Vibración: false

Correo: user4@correo.cl - Contraseña: 123123 - Opción Vibración: True

Correo: user5@correo.cl - Contraseña: 123123 - Opción Vibración: false



# Helpi - App de Accesibilidad

Proyecto desarrollado como parte de la evaluacion de la asignatura de Desarrollo de Aplicaciones Moviles.

## ¿De qué trata el proyecto?
Es una aplicacion movil enfocada en la accesibilidad, diseñada para ayudar a personas con discapacidad auditiva

En esta etapa se construyo una interfaz de usuario accesible con **Jetpack Compose** y **Material Design**


## Estructura del Proyecto
Para no enredarme con el código y mantener todo escalable, tome como ejemplo el video tutorial compartido en el foro y dividí el proyecto en paquetes lógicos dentro de `app/src/main/java/com/example/bequianapp/`:

*   **`/screens`**: Aquí están las 3 pantallas solicitadas de la APP
    *   [LoginScreen.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/screens/LoginScreen.kt): Login sencillo con el logo de la APP.
    *   [RegistroScreen.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/screens/RegistroScreen.kt): Formulario de registro, se incluyeron checkbox y un select para obtener detalles de preferencias de usuario.
    *   [RecuperarPassScreen.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/screens/RecuperarPassScreen.kt): Interfaz de recuperacion de contraseña, se utilizo una grilla, lazyVerticalGrid, para mostrar tarjetas con las opciones.
    *   [HomeScreen.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/screens/HomeScreen.kt): Página de bienvenida, obtiene los parámetros del correo y la opción de vibración de botones desde el Login, añadido un botón para pruebas de vibración, cambia el contenido dependiendo de la configuración.    
*   **`/navigation`**: Logica para la navegacion con navcontroller.
    *   [NavigationWrapper.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/navigation/NavigationWrapper.kt): Motor de navegacion, se configura el NavHost y asignamos por defecto Login, conectamos el resto de las pantallas como objetos @Serializable para movernos entre ellas con el navController.
    *   [Screens.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/navigation/Screens.kt) Objetos @Serializable para cada pantalla 
*   **`/data`**: Contiene [RepoUsuarios.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/data/RepoUsuarios.kt), que por ahora simula nuestra "base de datos" usando un arreglo temporal en memoria para guardar los registros.


## Navegación
Para la navegacion se utilizo `navigation-compose` y `kotlin-serialization`, en base a Video Tutorial compartido en el foro de la asignatura, controlados por dos archivos:

### 1. [Screens.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/navigation/Screens.kt)
Se utilizo `Kotlin Serialization` para crear objetos para cada pantalla y se agrego la anotacion `@Serializable` 

### 2. [NavigationWrapper.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/navigation/NavigationWrapper.kt)
Archivo para la configuracion del NavHost y NavController, se especifico la pantalla de inicio Login


## Consideraciones de Accesibilidad
Textos e Inputs de mayor tamaño

