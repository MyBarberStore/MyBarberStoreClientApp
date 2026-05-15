# 📱 BarberPro - Android App

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-2.9.0-blue?style=for-the-badge)

**BarberPro** es la aplicación móvil nativa desarrollada para clientes, diseñada para modernizar y agilizar la reserva de servicios en barberías. Este repositorio forma parte del ecosistema **BarberPro**, integrándose con una API REST (Spring Boot) y un panel administrativo (Angular).

---

## 🚀 Funcionalidades Principales

La aplicación ha sido diseñada siguiendo las guías de **Material Design 3**, ofreciendo una experiencia intuitiva y robusta:

### 1. Proceso de Reserva Inteligente
* **Flujo por pasos:** Selección de servicio, barbero, fecha y hora mediante una interfaz de fragmentos dinámicos.
* **Validación en tiempo real:** Comprobación de disponibilidad consultando el backend.
* **Resumen detallado:** Pantalla de confirmación con el desglose de precio y tiempo antes de confirmar la cita.

### 2. Gestión de Historial y Facturación
* **Citas Próximas y Pasadas:** Organización visual de las reservas del usuario.
* **Descarga de Facturas PDF:** Generación y visualización de facturas oficiales mediante la integración de `FileProvider` y servicios de almacenamiento.
* **Cancelación:** Opción de anular citas directamente desde la app con actualización inmediata del estado.

### 3. Localización y Soporte Multidioma
* **Mapa Interactivo:** Integración con **OpenStreetMap (osmdroid)** para mostrar la ubicación exacta del local.
* **Bilingüe:** Soporte completo en **Español** e **Inglés**, incluyendo menús, diálogos y mensajes de error.

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **Arquitectura:** MVVM (Model-View-ViewModel).
* **Consumo de API:** [Retrofit 2](https://square.github.io/retrofit/) & OkHttp.
* **Concurrencia:** Kotlin Coroutines.
* **Navegación:** Custom Fragment Transactions para el flujo de reserva.
* **UI Components:** ViewPager2, RecyclerView, MaterialCardView, CoordinatorLayout.
* **Gestión de Sesión:** SharedPreferences con un `SessionManager` centralizado.

---

## 📂 Estructura del Proyecto

```text
com.example.mybarberstore
├── data
│   ├── api          # Definición de endpoints y cliente Retrofit
│   └── model        # Clases de datos (DTOs) y respuestas de la API
├── ui
│   ├── adapter      # Adaptadores para Listas (HistoryAdapter, etc.)
│   ├── booking      # Fragments del flujo de reserva (Steps 1-4)
│   ├── history      # Gestión de historial y visualización de PDFs
│   └── home         # Pantalla principal con mapa y promociones
├── utils            # SessionManager y formateadores
└── MainActivity     # Controlador principal de navegación (BottomNav)
