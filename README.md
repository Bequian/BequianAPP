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
*   **`/navigation`**: Logica para la navegacion con navcontroller.
    *   [NavigationWrapper.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/navigation/NavigationWrapper.kt): Motor de navegacion, se configura el NavHost y asignamos por defecto Login, conectamos el resto de las pantallas como objetos @Serializable para movernos entre ellas con el navController.
    *   [Screens.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/navigation/Screens.kt) Objetos @Serializable para cada pantalla 
*   **`/data`**: Contiene [RepoUsuarios.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/data/RepoUsuarios.kt), que por ahora simula nuestra "base de datos" usando un arreglo temporal en memoria para guardar los registros.


## Navegación
Para la navegacion se utilizo `navigation-compose` y `kotlin-serialization`, en base a Video Tutorial compartido en el foro de la asignatura, controlados por dos archivos:

### 1. [Screens.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/navigation/Screens.kt)
Se utilizo `Kotlin Serialization` para crear objetos para cada pantalla y se agrego la anotacion `@Serialiable` 

### 2. [NavigationWrapper.kt](../BequianAPP/app/src/main/java/com/example/bequianapp/navigation/NavigationWrapper.kt)
Archivo para la configuracion del NavHost y NavController, se especifico la pantalla de inicio Login


## 🎨 Consideraciones de Accesibilidad
Textos e Inputs de mayor tamaño