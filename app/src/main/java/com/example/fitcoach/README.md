
# FitCoach - Guía Técnica Completa

## 1. Configuración de Firebase

1. Crear proyecto en Firebase Console
2. Habilitar Google Analytics
3. Añadir aplicación Android
4. Descargar y colocar `google-services.json` en `/app`
5. Habilitar Authentication con email/password
6. Configurar Crashlytics
7. Configurar Firestore para la gestión de datos

## 2. Estructura del Proyecto

```tree
app/
├── MainActivity.kt
├── ui/
│   ├── navigation/
│   │   ├── Navigation.kt
│   │   └── Screen.kt
│   ├── screens/
│   │   ├── splash/
│   │   ├── login/
│   │   │   ├── components/
│   │   │   └── dialogs/
│   │   ├── home/
│   │   ├── timer/
│   │   └── calendar/
│   └── theme/
└── utils/
    └── DialogUtils.kt
```

### 2.1 Componentes Principales

#### MainActivity.kt

- Arranque de la aplicación mostrando SplashScreen

#### Navigation.kt

Sistema central que controla la navegación entre pantallas con destino inicial en SplashScreen.
Gestiona la navegación desde notificaciones y maneja la inicialización de ViewModels.
Utiliza `NavHost` para definir las rutas de navegación.
Se usa popBackStack para evitar que el usuario vuelva a la pantalla anterior.
Gestión de Notificaciones verificando si la app se abre desde una notificación y redirigiendo a la pantalla correspondiente.

#### Screen.kt

Clase sellada que define las rutas disponibles en la aplicación:

- SplashScreen: pantalla inicial con logo
- Login: inicio de sesión con Firebase Auth
- Home: interfaz principal con todas las funcionalidades
- Timer: temporizador para descansos
- Calendar: calendario para el seguimiento de los entrenamientos

## 3. Pantallas Principales

### 3.1 Splash Screen

- **SplashScreen.kt**: Pantalla inicial con una imagen de fondo, logo y botón de acceso a la pantalla de login.

### 3.2 Login

- **LoginScreen.kt**: Sistema de autenticación completo con validación de campos, opción de recordar sesión y sistema de recuperación de contraseña.
- **LoginViewModel.kt**: Gestiona autenticación con Firebase, maneja errores y estado de la sesión.
- **Components/**: Campos personalizados para email y contraseña, botón de login y fondo personalizado.
- **Dialogs/**: Diálogo para la recuperación de contraseña.

### 3.3 Home (En Construcción)

- **HomeScreen.kt**: Pantalla principal con biblioteca de ejercicios, categorías principales y acceso al blog.
- **HomeViewModel.kt**: Maneja el estado de la pantalla principal, carga de ejercicios y navegación.
- **Models/**: Define estructuras para ejercicios, categorías y entradas del blog.
- **Components/**: Incluye barra de navegación común, menú lateral y componentes de la interfaz principal.

### 3.4 Timer

- **Timer.kt**: Temporizador personalizable con botones de acceso rápido y notificaciones.
- **TimerViewModel.kt**: Gestiona la cuenta regresiva y estados del temporizador.
- **TimerComponents.kt**: Selectores de tiempo y controles del temporizador.
- **NotificationService.kt**: Sistema de notificaciones para el temporizador.

### 3.5 Calendar (En Construcción)

- **Calendar.kt**: Calendario mensual con sistema de notas y valoraciones por día.
- **CalendarViewModel.kt**: Maneja selección de fechas, notas y estado del calendario.
- WorkoutNote y WorkoutRating: Estructuras para gestionar notas y valoraciones de entrenamientos.

## 4. Características Técnicas

### 4.1 Control de Orientación

- Orientación vertical forzada en Timer y Calendar
- Gestión mediante DisposableEffect

### 4.2 Sistema de Notificaciones

- Notificaciones del temporizador

### 4.3 UI/UX

- Compose para interfaces
- Temas claro/oscuro
- Componentes reutilizables
- Diseño responsive

## 5. Implementación de Firebase

### 5.1 Autenticación

- Email/password
- Recuperación de contraseña
- Persistencia de sesión

### 5.2 Crashlytics

- Monitoreo de errores
- Reportes automáticos
