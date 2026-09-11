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


