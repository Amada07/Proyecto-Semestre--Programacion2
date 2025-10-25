# 🧘 Sistema de Gestión de Sesiones de Bienestar

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)
![React](https://img.shields.io/badge/React-18-blue)
![Android](https://img.shields.io/badge/Android-API%2024+-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Railway-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

Sistema completo de gestión de citas y sesiones para centros de bienestar, con backend Spring Boot, frontend React y aplicación móvil Android nativa.

---

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Casos de Uso](#-casos-de-uso)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [API Endpoints](#-api-endpoints)
- [Patrones de Diseño](#-patrones-de-diseño)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Testing](#-testing)
- [Despliegue](#-despliegue)
- [Contribución](#-contribución)
- [Licencia](#-licencia)

---

## ✨ Características

### Backend (Spring Boot)
- ✅ **Autenticación JWT** con roles (Admin, Recepcionista, Cliente)
- ✅ **CRUD completo** para clientes, servicios, citas, facturas
- ✅ **Gestión de sesiones** con historial completo
- ✅ **Sistema de notificaciones** (Email, SMS simulado)
- ✅ **Auditoría automática** de todas las operaciones
- ✅ **Validaciones de negocio** (antelación mínima, cupos, cancelaciones)
- ✅ **Reportes generales** con métricas e ingresos
- ✅ **Base de datos PostgreSQL** en Railway
- ✅ **API REST documentada** con Swagger/OpenAPI

### Frontend Web (React)
- ✅ **Dashboard administrativo** moderno y responsivo
- ✅ **Gestión de clientes** (registro, edición, desactivación)
- ✅ **Gestión de servicios** (CRUD completo)
- ✅ **Gestión de citas** (agendar, confirmar, cancelar, atender)
- ✅ **Generación de facturas** automática
- ✅ **Historial de sesiones** con filtros avanzados
- ✅ **Reportes visuales** con gráficos y métricas
- ✅ **Diseño con Tailwind CSS** y componentes modernos

### App Móvil (Android)
- ✅ **Login con JWT** y persistencia de sesión
- ✅ **Consulta de servicios** disponibles
- ✅ **Agendar citas** con validaciones
- ✅ **Historial de citas** del cliente
- ✅ **Consulta de facturas** personales
- ✅ **Perfil de usuario** con datos completos
- ✅ **Notificaciones** simuladas
- ✅ **Material Design 3** con arquitectura MVVM

---

## 🏗️ Arquitectura
![sistema](https://github.com/Amada07/Proyecto-Semestre--Programacion2/raw/main/imagen/sistema%202.png)

### Patrones Arquitectónicos Implementados

1. **MVC (Model-View-Controller)**
   - Separación clara entre capas
   - Controladores REST
   - Servicios de negocio
   - Repositorios JPA

2. **Repository Pattern**
   - Abstracción de acceso a datos
   - JPA Repositories

3. **Service Layer Pattern**
   - Lógica de negocio encapsulada
   - Transacciones manejadas

4. **DTO Pattern**
   - Transferencia de datos sin exponer entidades

---

## 🛠️ Tecnologías

### Backend
| Tecnología | Versión | Uso |
|------------|---------|-----|
| Java | 17 | Lenguaje principal |
| Spring Boot | 3.5.5 | Framework principal |
| Spring Security | 3.5.5 | Autenticación/Autorización |
| Spring Data JPA | 3.5.5 | ORM |
| JWT (jjwt) | 0.12.5 | Tokens de autenticación |
| PostgreSQL | 16+ | Base de datos |
| Hibernate | 6.x | ORM |
| Maven | 3.8+ | Gestor de dependencias |
| Swagger/OpenAPI | 3.0 | Documentación API |

### Frontend
| Tecnología | Versión | Uso |
|------------|---------|-----|
| React | 18 | Framework UI |
| Tailwind CSS | 3.x | Estilos |
| Lucide Icons | Latest | Iconografía |
| Axios | Latest | Cliente HTTP |

### Móvil
| Tecnología | Versión | Uso |
|------------|---------|-----|
| Android SDK | 24+ (Nougat) | Plataforma |
| Kotlin | 2.0.21 | Lenguaje principal |
| Java | 17 | Compatibilidad |
| Retrofit | 2.9.0 | Cliente HTTP |
| Material Design 3 | 1.11.0 | Diseño UI |
| Jetpack Compose | 1.5.3 | UI declarativa |

---

## 📱 Casos de Uso

### Casos de Uso Web (Administrador/Recepcionista)

| ID | Caso de Uso | Descripción |
|----|-------------|-------------|
| **UC-01** | Registrar Cliente | Registro completo con validación de DPI |
| **UC-W02** | Gestionar Servicios | CRUD de servicios con precios y duración |
| **UC-W03** | Agendar Cita | Programar sesión con validación de cupos |
| **UC-W04** | Consultar Historial | Filtros avanzados por cliente/servicio/fecha |
| **UC-W05** | Gestionar Solicitudes | Confirmar o rechazar citas pendientes |
| **UC-W06** | Generar Reportes | Métricas, ingresos y estadísticas |
| **UC-W07** | Iniciar/Finalizar Sesión | Control de asistencia con observaciones |
| **UC-W08** | Generar Factura | Facturación automática post-sesión |
| **UC-10** | Editar Cliente | Actualización de datos personales |
| **UC-11** | Desactivar Cliente | Soft delete con validaciones |

### Casos de Uso Móvil (Cliente)

| ID | Caso de Uso | Descripción |
|----|-------------|-------------|
| **UC-02** | Iniciar Sesión | Login con JWT persistente |
| **UC-M02** | Ver Perfil | Consulta de datos personales |
| **UC-M03** | Consultar Servicios | Lista de servicios activos |
| **UC-M04** | Agendar Cita | Solicitud con 2 horas de antelación |
| **UC-M05** | Ver Historial | Lista de citas propias |
| **UC-M06** | Consultar Facturas | Historial de pagos |

---

## 📦 Requisitos Previos

### Para Backend
- ☕ **Java 17+** (OpenJDK o Oracle JDK)
- 📦 **Maven 3.8+**
- 🐘 **PostgreSQL 16+** (Railway o local)
- 🔧 **IDE recomendado**: IntelliJ IDEA, Eclipse o VS Code

### Para Frontend
- 📦 **Node.js 18+**
- 📦 **npm 9+** o **yarn**

### Para Android
- 🤖 **Android Studio Ladybug+**
- 📦 **Android SDK 24+** (Nougat)
- ☕ **JDK 17**

---

## 🚀 Instalación

### 1️⃣ Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/sesiones-bienestar.git
cd sesiones-bienestar
```

### 2️⃣ Backend (Spring Boot)

#### Configurar Base de Datos

1. **Crear base de datos en Railway** (recomendado):
   ```
   https://railway.app/
   → New Project → PostgreSQL
   ```

2. **Copiar credenciales** de Railway Dashboard

3. **Configurar variables de entorno**:
   ```bash
   # Windows
   copy .env.example .env
   
   # Mac/Linux
   cp .env.example .env
   ```

4. **Editar `.env`** con tus credenciales:
   ```properties
   DB_URL=jdbc:postgresql://containers-us-west-XXX.railway.app:XXXX/railway
   DB_USERNAME=postgres
   DB_PASSWORD=tu-password-railway
   
   JWT_SECRET=tu-secret-generado-con-openssl
   JWT_EXPIRATION=86400000
   
   CORS_ORIGINS=http://localhost:3000,http://localhost:5173
   ```

#### Generar JWT Secret

```bash
# Linux/Mac
openssl rand -base64 64

# Online (si no tienes OpenSSL)
https://generate-secret.vercel.app/64
```

#### Compilar y ejecutar

```bash
cd sesiones-bienestar
mvn clean install
mvn spring-boot:run
```

El backend estará en: **http://localhost:8080**

Swagger UI: **http://localhost:8080/swagger-ui.html**

### 3️⃣ Frontend (React)

```bash
cd frontend
npm install
npm start
```

El frontend estará en: **http://localhost:3000**

**Credenciales de prueba**:
- Usuario: `admin`
- Contraseña: `admin123`

### 4️⃣ App Android

1. **Abrir Android Studio**
2. **Open Project** → Seleccionar carpeta `App Android`
3. **Configurar URL del backend** en:
   ```java
   App Android/app/src/main/java/com/umg/bienestar/cliente/utils/Constants.java
   ```
   
   ```java
   // Para emulador
   public static final String BASE_URL = "http://10.0.2.2:8080/";
   
   // Para dispositivo físico (reemplaza con tu IP local)
   // public static final String BASE_URL = "http://192.168.1.X:8080/";
   ```

4. **Sync Gradle** (Build → Make Project)
5. **Run** (Shift + F10)

**Credenciales de prueba**:
- Usuario: `cliente1` (o cualquier cliente registrado desde web)
- Contraseña: `12345678`

---

## ⚙️ Configuración

### Variables de Entorno (.env)

```properties
# ============================================
# DATABASE CONFIGURATION - RAILWAY
# ============================================
DB_URL=jdbc:postgresql://containers-us-west-XXX.railway.app:XXXX/railway
DB_USERNAME=postgres
DB_PASSWORD=TU_PASSWORD_AQUI

# ============================================
# JWT CONFIGURATION
# ============================================
JWT_SECRET=TU_SECRET_BASE64_AQUI
JWT_EXPIRATION=86400000

# ============================================
# CORS CONFIGURATION
# ============================================
CORS_ORIGINS=http://localhost:3000,http://localhost:5173
```

### Configuración de Seguridad (SecurityConfig.java)

```java
// Endpoints públicos
.requestMatchers(
    "/api/auth/**", 
    "/api/clientes/registro", 
    "/swagger-ui/**", 
    "/v3/api-docs/**"
).permitAll()

// Endpoints por rol
.requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
.requestMatchers("/api/servicios/**").hasAnyRole("ADMINISTRADOR", "RECEPCIONISTA")
```

### CORS (Cross-Origin Resource Sharing)

Configurado en `SecurityConfig.java`:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost:3000",      // React
        "http://localhost:5173",      // Vite
        "http://10.0.2.2:8080"        // Android Emulator
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    // ...
}
```

---

## 📖 Uso

### 1️⃣ Registro de Usuario (Cliente)

**Desde Web (Admin/Recepcionista)**:
```bash
POST /api/clientes/registro
```

**Desde Android**:
- Login → "Registrarse" (si se implementa la UI)
- O registrar desde web primero

### 2️⃣ Login

**Web**:
```javascript
POST /api/auth/login
Body: { "username": "admin", "password": "admin123" }
Response: { "token": "eyJ...", "username": "admin", "rol": "ADMINISTRADOR", "id": 1 }
```

**Android**:
```kotlin
// LoginActivity automáticamente guarda el token
SharedPrefsManager.saveToken(token)
```

### 3️⃣ Flujo Completo de Cita

1. **Cliente solicita cita** (Web o Android)
   ```
   POST /api/citas
   Body: { clienteId, servicioId, fechaHora, notas }
   → Estado: PENDIENTE
   ```

2. **Recepcionista confirma/rechaza** (Web)
   ```
   PATCH /api/citas/{id}/confirmar
   → Estado: CONFIRMADA
   
   PATCH /api/citas/{id}/rechazar?motivo=...
   → Estado: RECHAZADA
   ```

3. **Terapeuta marca como atendida** (Web)
   ```
   PATCH /api/citas/{id}/atender
   → Estado: ATENDIDA
   ```

4. **Sistema genera factura** (Web - automático)
   ```
   POST /api/facturas
   Body: { citaId }
   → Factura creada con impuestos calculados
   ```

### 4️⃣ Consultar Historial con Filtros

```javascript
GET /api/sesiones?clienteId=1&servicioId=2&estado=ATENDIDA&fechaInicio=2025-01-01&fechaFin=2025-12-31
```

### 5️⃣ Generar Reportes

```javascript
GET /api/facturas/ingresos?inicio=2025-01-01T00:00:00&fin=2025-12-31T23:59:59
Response: 15000.50
```

---

## 🔌 API Endpoints

### Autenticación

| Método | Endpoint | Descripción | Público |
|--------|----------|-------------|---------|
| POST | `/api/auth/login` | Login con JWT | ✅ |

### Clientes

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| POST | `/api/clientes/registro` | Registrar cliente | ✅ Público |
| GET | `/api/clientes` | Listar todos | 🔒 Admin |
| GET | `/api/clientes/{id}` | Obtener por ID | 🔒 Admin, Cliente (propio) |
| PUT | `/api/clientes/{id}` | Actualizar | 🔒 Admin |
| DELETE | `/api/clientes/{id}` | Eliminar | 🔒 Admin |

### Servicios

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| POST | `/api/servicios` | Crear servicio | 🔒 Admin |
| GET | `/api/servicios` | Listar todos | 🔒 Autenticado |
| GET | `/api/servicios/activos` | Solo activos | 🔒 Autenticado |
| PUT | `/api/servicios/{id}` | Actualizar | 🔒 Admin |
| PATCH | `/api/servicios/{id}/desactivar` | Desactivar | 🔒 Admin |

### Citas

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| POST | `/api/citas` | Agendar cita | 🔒 Autenticado |
| GET | `/api/citas` | Listar todas | 🔒 Admin, Recepcionista |
| GET | `/api/citas/estado/{estado}` | Por estado | 🔒 Admin, Recepcionista |
| GET | `/api/citas/cliente/{id}` | Por cliente | 🔒 Cliente (propio) |
| PATCH | `/api/citas/{id}/confirmar` | Confirmar | 🔒 Admin, Recepcionista |
| PATCH | `/api/citas/{id}/rechazar` | Rechazar | 🔒 Admin, Recepcionista |
| PATCH | `/api/citas/{id}/cancelar` | Cancelar | 🔒 Cliente (propio) |
| PATCH | `/api/citas/{id}/atender` | Marcar atendida | 🔒 Admin, Recepcionista |

### Sesiones

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| POST | `/api/sesiones/{citaId}/iniciar` | Iniciar sesión | 🔒 Admin, Recepcionista |
| PATCH | `/api/sesiones/{id}/finalizar` | Finalizar | 🔒 Admin, Recepcionista |
| GET | `/api/sesiones` | Historial con filtros | 🔒 Admin, Recepcionista |
| GET | `/api/sesiones/cliente/{id}` | Por cliente | 🔒 Cliente (propio) |

### Facturas

| Método | Endpoint | Descripción | Roles |
|--------|----------|-------------|-------|
| POST | `/api/facturas` | Generar factura | 🔒 Admin, Recepcionista |
| GET | `/api/facturas` | Listar todas | 🔒 Admin |
| GET | `/api/facturas/cliente/{id}` | Por cliente | 🔒 Cliente (propio) |
| PATCH | `/api/facturas/{id}/pagar` | Marcar pagada | 🔒 Admin |
| GET | `/api/facturas/ingresos` | Calcular ingresos | 🔒 Admin |

---

## 🎨 Patrones de Diseño

### 1. Singleton (ConfiguracionSingleton)

```java
public class ConfiguracionSingleton {
    private static ConfiguracionSingleton instancia;
    
    private ConfiguracionSingleton() {
        this.maxCitasPendientesPorCliente = 3;
        this.horasMinAnticipacion = 2;
        this.horasLimiteCancelacion = 24;
    }
    
    public static synchronized ConfiguracionSingleton getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionSingleton();
        }
        return instancia;
    }
}
```

**Uso**: Configuración global única del sistema.

### 2. Observer (NotificacionObserver)

```java
public abstract class NotificacionObserver {
    protected List<Notificacion> notificacionesPendientes = new ArrayList<>();
    
    public abstract void notificar(Usuario usuario, String titulo, String mensaje);
    public abstract boolean enviarPendientes();
}

@Component
public class EmailNotificacionObserver extends NotificacionObserver {
    @Override
    public void notificar(Usuario usuario, String titulo, String mensaje) {
        Notificacion notif = new Notificacion(usuario, tipo, titulo, mensaje);
        notificacionesPendientes.add(notif);
    }
    
    @Override
    public boolean enviarPendientes() {
        // Envío batch de emails
    }
}
```

**Uso**: Sistema de notificaciones desacoplado.

### 3. Facade (GestionCitasFacade)

```java
@Component
public class GestionCitasFacade {
    @Autowired private CitaService citaService;
    @Autowired private SesionService sesionService;
    @Autowired private FacturaService facturaService;
    @Autowired private EmailNotificacionObserver emailObserver;
    
    public Cita agendarYNotificar(Long clienteId, Long servicioId, ...) {
        Cita cita = citaService.agendarCita(...);
        emailObserver.notificar(...);
        return cita;
    }
    
    public void completarSesionYFacturar(Long citaId, String observaciones) {
        Sesion sesion = sesionService.iniciar(citaId);
        sesionService.finalizar(sesion.getId(), observaciones);
        Factura factura = facturaService.generar(citaId);
        emailObserver.notificar(...);
    }
}
```

**Uso**: Simplifica operaciones complejas multi-servicio.

### 4. Repository Pattern (JPA)

```java
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByClienteId(Long clienteId);
    List<Cita> findByEstado(EstadoCita estado);
    
    @Query("SELECT COUNT(c) FROM Cita c WHERE ...")
    Long contarCitasEnFranja(...);
}
```

**Uso**: Abstracción de acceso a datos.

### 5. DTO Pattern

```java
public class CitaDTO {
    private Long id;
    private Long clienteId;
    private Long servicioId;
    private LocalDateTime fechaHora;
    private String clienteNombre;  // Campo adicional para frontend
    private String servicioNombre; // Campo adicional para frontend
    // ...
}
```

**Uso**: Transferencia de datos sin exponer entidades.

---

## 📁 Estructura del Proyecto

```
sesiones-bienestar/
│
├── src/main/java/com/umg/bienestar/sesiones_bienestar/
│   ├── controller/          # Controladores REST
│   │   ├── AuthController.java
│   │   ├── ClienteController.java
│   │   ├── CitaController.java
│   │   ├── ServicioController.java
│   │   ├── SesionController.java
│   │   └── FacturaController.java
│   │
│   ├── service/             # Lógica de negocio
│   │   └── impl/
│   │       ├── ClienteService.java
│   │       ├── CitaService.java
│   │       ├── ServicioService.java
│   │       ├── SesionService.java
│   │       ├── FacturaService.java
│   │       └── AuditoriaService.java
│   │
│   ├── repository/          # Acceso a datos
│   │   └── jpa/
│   │       ├── ClienteRepository.java
│   │       ├── CitaRepository.java
│   │       ├── ServicioRepository.java
│   │       ├── SesionRepository.java
│   │       └── FacturaRepository.java
│   │
│   ├── entity/              # Entidades JPA
│   │   ├── Usuario.java
│   │   ├── Cliente.java
│   │   ├── Servicio.java
│   │   ├── Cita.java
│   │   ├── Sesion.java
│   │   ├── Factura.java
│   │   ├── Notificacion.java
│   │   └── AuditoriaLog.java
│   │
│   ├── dto/                 # Data Transfer Objects
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   ├── ClienteDTO.java
│   │   ├── CitaDTO.java
│   │   └── SesionHistorialDTO.java
│   │
│   ├── security/            # Seguridad JWT
│   │   ├── JwtService.java
│   │   ├── JwtAuthenticationFilter.java
│   │   ├── CustomUserDetails.java
│   │   └── CustomUserDetailsService.java
│   │
│   ├── pattern/             # Patrones de diseño
│   │   ├── singleton/
│   │   │   └── ConfiguracionSingleton.java
│   │   ├── observer/
│   │   │   ├── NotificacionObserver.java
│   │   │   └── EmailNotificacionObserver.java
│   │   └── facade/
│   │       └── GestionCitasFacade.java
│   │
│   ├── exception/           # Manejo de errores
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   ├── ValidationException.java
│   │   └── BusinessException.java
│   │
│   └── util/                # Utilidades
│       └── SecurityUtils.java
│
├── frontend/                # React App
│   ├── src/
│   │   ├── App.js           # Componente principal
│   │   ├── index.js
│   │   └── index.css
│   └── package.json
│
├── App Android/             # Android App
│   ├── app/src/main/java/com/umg/bienestar/cliente/
│   │   ├── activities/
│   │   │   ├── LoginActivity.java
│   │   │   ├── MainActivity.java
│   │   │   ├── ServiciosActivity.java
│   │   │   ├── AgendarCitaActivity.java
│   │   │   ├── MisSesionesActivity.java
│   │   │   ├── MisFacturasActivity.java
│   │   │   ├── PerfilActivity.java
│   │   │   └── NotificacionesActivity.java
│   │   │
│   │   ├── adapters/
│   │   │   ├── ServiciosAdapter.java
│   │   │   ├── CitasAdapter.java
│   │   │   ├── FacturasAdapter.java
│   │   │   └── NotificacionesAdapter.java
│   │   │
│   │   ├── models/
│   │   │   ├── Cliente.java
│   │   │   ├── Servicio.java
│   │   │   ├── Cita.java
│   │   │   ├── Factura.java
│   │   │   └── Notificacion.java
│   │   │
│   │   ├── api/
│   │   │   ├── ApiClient.java
│   │   │   ├── ApiService.java
│   │   │   └── AuthInterceptor.java
│   │   │
│   │   └── utils/
│   │       ├── Constants.java
│   │       └── SharedPrefsManager.java
│   │
│   └── app/src/main/res/
│       ├── layout/
│       └── values/
│
├── .env.example             # Ejemplo de variables
├── .gitignore
├── pom.xml                  # Maven dependencies
└── README.md
```

---
