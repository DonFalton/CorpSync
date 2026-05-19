# 🚀 CorpSync - Sistema de Gestión Inteligente de Tickets IT

![Estado](https://img.shields.io/badge/Estado-Listo--TFC-success)
![Curso](https://img.shields.io/badge/Proyecto_Final-DAM-blue)
![Arquitectura](https://img.shields.io/badge/Arquitectura-Cloud_Native-success)

**CorpSync AI** es una plataforma de clase empresarial para la gestión de servicios de tecnología (ITSM) diseñada para automatizar y optimizar la resolución de incidencias en entornos corporativos. El sistema implementa un modelo multiplataforma desacoplado y reactivo, sirviendo como solución integral para el proyecto de fin de ciclo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

---

## 🏗️ Arquitectura del Sistema

El ecosistema está estructurado bajo un patrón de microservicios y clientes especializados multiplataforma que convergen en una única fuente de verdad en la nube (Single Source of Truth):

* **🧠 Backend de Datos & Servidor de Eventos:** Supabase Cloud (PostgreSQL, Triggers reactivos, Realtime CDC vía WebSockets y Edge Functions en Deno con Gemini AI).
* **🌐 Consola de Operaciones Web (Técnicos/Admins):** Single Page Application (SPA) reactiva e interactiva basada en **React 19**, **Vite 8**, **Tailwind CSS v4** y gestión de estado con **Zustand 5**.
* **📱 Aplicación de Campo Móvil (Empleados):** Aplicación nativa en **Kotlin** para Android con integración de cámara para captura y reporte visual de incidencias.
* **💻 Consola de Gestión Masiva (Desktop):** Cliente local de escritorio implementado en **Java Swing** con persistencia relacional a través del driver JDBC.

---

## 📂 Estructura del Monorepo

El proyecto se organiza bajo una arquitectura de monorepo unificado, simplificando el control de versiones y el despliegue del ecosistema tecnológico:

```text
CorpSync/
├── 📁 app-web/          # Panel de control Kanban y monitor SLA para técnicos (React 19)
├── 📁 app-movil/        # Cliente Android nativo para reporte de incidencias (Kotlin)
├── 📁 app-escritorio/   # Backoffice Swing para auditoría y gestión masiva (Java)
├── 📁 supabase/         # Infraestructura de BD, triggers relacionales y Edge Functions (Deno)
└── 📄 README.md         # Documentación principal del monorepo
```

---

## 🛠️ Requisitos Previos

Para levantar el ecosistema completo localmente o en un entorno de desarrollo integrado, el sistema requiere:

1. **Entorno Node.js**: Node.js `v20.x` o superior (junto con `npm` o `pnpm`).
2. **Entorno Java (Escritorio)**: Java JDK `17` o superior para compilar y ejecutar el cliente desktop.
3. **Entorno Android (Móvil)**: Android Studio Jellyfish+ con SDK API `34` instalado.
4. **Infraestructura Local**: Docker Desktop en ejecución (requerido para levantar Supabase de forma local con la CLI).
5. **Supabase CLI**: Instalado en el sistema para la gestión de migraciones y funciones en Deno.
6. **API Key de Gemini**: Clave de API activa de Google AI Studio para el motor de triaje con Inteligencia Artificial.

---

## 🚀 Instalación y Despliegue Local

Sigue esta guía paso a paso para inicializar todos los componentes del ecosistema CorpSync de forma secuencial:

### 1. Preparación del Backend (Supabase Cloud-First)
El proyecto está diseñado para funcionar de forma nativa en la nube, aprovechando la infraestructura Serverless de Supabase.

**A. Configuración de Base de Datos y Cliente:**
1. Crea un nuevo proyecto en [Supabase](https://supabase.com/).
2. Ejecuta los scripts de creación de tablas, políticas RLS y el trigger asíncrono (`pg_net`) directamente en el SQL Editor del panel de control de Supabase.
3. Copia la `URL` del proyecto y la `anon key` y configúralas en el archivo `app-web/.env` de tu cliente local.

**B. Despliegue de Edge Functions (Triaje IA):**
Para desplegar la lógica de triaje con Gemini, enlaza tu entorno local con tu proyecto en la nube mediante la CLI de Supabase:

```bash
# 1. Autentícate en Supabase desde la terminal
npx supabase login

# 2. Enlaza tu repositorio con tu proyecto en la nube (sustituye por tu ID de proyecto)
npx supabase link --project-ref tu_project_id

# 3. Inyecta los secretos de entorno de forma segura en la nube
npx supabase secrets set GEMINI_API_KEY="tu_api_key_de_google"
npx supabase secrets set ITSM_WEBHOOK_SECRET="corpsync_dev_secret_2026"

# 4. Despliega la función desactivando la verificación JWT por defecto 
# para permitir la validación de firma simétrica personalizada
npx supabase functions deploy gemini-triage --no-verify-jwt
```

### 2. Lanzamiento del Cliente Web (`app-web`)
Instala las dependencias y ejecuta el servidor de desarrollo local de Vite:
```bash
cd app-web
npm install
npm run dev
```
La aplicación web se levantará por defecto en `http://localhost:5173`.

### 3. Compilación de la Aplicación de Escritorio (`app-escritorio`)
Abre el directorio `/app-escritorio` en tu IDE favorito (IntelliJ IDEA o Eclipse):
1. Asegúrate de añadir las librerías del directorio `/lib` (Driver JDBC de Postgres, Gson) a la biblioteca del proyecto (Build Path).
2. Ejecuta la clase de entrada principal `App.java` (o equivalente) como aplicación de Java.

### 4. Lanzamiento de la Aplicación Móvil (`app-movil`)
1. Importa el subdirectorio `/app-movil` en Android Studio.
2. Sincroniza Gradle (`Gradle Sync`).
3. Compila y ejecuta la aplicación en un dispositivo físico o emulador con servicios de Google Play.

---

## 👥 Reglas de Contribución y Equipo

Para asegurar la consistencia metodológica del repositorio de cara a la defensa del tribunal, cada miembro del equipo trabaja en su respectiva sección:

* **Martín (Lead Architect & Fullstack):** Propietario de `app-web/` y la infraestructura central `supabase/`.
* **Rafa (Mobile Specialist):** Propietario de `app-movil/` (Kotlin).
* **Germán (Desktop Specialist):** Propietario de `app-escritorio/` (Java Swing).

**Regla de Seguridad Estricta:** Queda terminantemente prohibido subir contraseñas de red, credenciales JDBC activas, tokens de Supabase, API keys de Google Cloud o el fichero de entorno `.env` local.

---
*Proyecto de Trabajo Final de Ciclo - DAM*

[![](https://mermaid.ink/img/pako:eNp9Vdtu2zgQ_ZUBgRY2YKe6WK7NN6-dtilirDcuUuzCL2NpqrChSIGijKRBPmYf97mfkB8rJdqNvAnMB2FI6pw5mjOkHliqM2Kc5ais3ShwwworCT7p7wgZwVVtEYYw16Zc36sULr6sl9BbUIXGaCk1rNCgJKn7Hp2hpQ_aFGgB_nZjuFwOFwu_h3eiOuy9yd69Kfyyf1aUWqEVfMCKIOQwF4UgZXXlt8-VNZQjzJSl0uh797aGt3BFpYaPwn6qt81bHMMBREE0HgbxMJoe4tEwGHuaNdm6hHVd4rbJ8xa-4FZiBeu_LqFdAU8TnaZ5RXLEYXm96mZx1XOqtVG6gt5X2g5g-fRzJ-QAPuMO-y7NNuxQv3-Ow8DzXOpcKMil3qKE3qy2N314Ofg26kI78V6ty81hJZRzMtWytdWK9JZsdUwTd6HPcfTe03j1HC5URcaiAUt3zoRSotJHPKPTPOdVaoSri9AcLokMlGS-CUmuSp8Xf8z7B5rkNM0rHsQcZjtUPzDDY8mz1QXMn_4tXK_CPcylrjNYOwmYe8d52nEimhziZBiGLzWvjE5d__v2hPn6GgqsxE43NNFpmtaJ813b1653UVpRUNsca90a0vdq4tdpgv2nX6N02lPx9FM5Nyzlvycz6P1Zpq4gKJsGS0dd8Es9r9RwxMGd7lIKymvy-ytTkzscTd9QZc3TfxXQcd69ZVnYSRF14sQTzXVRCrnHNGzfKXX3y7b1_gzL23dndEd9nh1hO_H--58FNhWF3jWZlOT_zwbP4tM0V5Rh6sUsqXDmNs3x0birwS8u3KInGnXBnXjKBiw3ImPcuhoNWEHudmum7KFJsWH2hgraMO7CDM3thm3Uo8OUqP7RujjAjK7zG8a_oazcrC6bS3Qh0JW3-L1qSGVk5rpWlvEwCYKWhfEHdsf4cByehc2YhpNgEkdxMBqwe8ajaHoWTOPxOIqSJJnESfQ4YD_azA4wmY5GYTRJxu4xHQ8YZU2LL_0fof0xPP4ClmC3zg?type=png)](https://mermaid.live/edit#pako:eNp9Vdtu2zgQ_ZUBgRY2YKe6WK7NN6-dtilirDcuUuzCL2NpqrChSIGijKRBPmYf97mfkB8rJdqNvAnMB2FI6pw5mjOkHliqM2Kc5ais3ShwwworCT7p7wgZwVVtEYYw16Zc36sULr6sl9BbUIXGaCk1rNCgJKn7Hp2hpQ_aFGgB_nZjuFwOFwu_h3eiOuy9yd69Kfyyf1aUWqEVfMCKIOQwF4UgZXXlt8-VNZQjzJSl0uh797aGt3BFpYaPwn6qt81bHMMBREE0HgbxMJoe4tEwGHuaNdm6hHVd4rbJ8xa-4FZiBeu_LqFdAU8TnaZ5RXLEYXm96mZx1XOqtVG6gt5X2g5g-fRzJ-QAPuMO-y7NNuxQv3-Ow8DzXOpcKMil3qKE3qy2N314Ofg26kI78V6ty81hJZRzMtWytdWK9JZsdUwTd6HPcfTe03j1HC5URcaiAUt3zoRSotJHPKPTPOdVaoSri9AcLokMlGS-CUmuSp8Xf8z7B5rkNM0rHsQcZjtUPzDDY8mz1QXMn_4tXK_CPcylrjNYOwmYe8d52nEimhziZBiGLzWvjE5d__v2hPn6GgqsxE43NNFpmtaJ813b1653UVpRUNsca90a0vdq4tdpgv2nX6N02lPx9FM5Nyzlvycz6P1Zpq4gKJsGS0dd8Es9r9RwxMGd7lIKymvy-ytTkzscTd9QZc3TfxXQcd69ZVnYSRF14sQTzXVRCrnHNGzfKXX3y7b1_gzL23dndEd9nh1hO_H--58FNhWF3jWZlOT_zwbP4tM0V5Rh6sUsqXDmNs3x0birwS8u3KInGnXBnXjKBiw3ImPcuhoNWEHudmum7KFJsWH2hgraMO7CDM3thm3Uo8OUqP7RujjAjK7zG8a_oazcrC6bS3Qh0JW3-L1qSGVk5rpWlvEwCYKWhfEHdsf4cByehc2YhpNgEkdxMBqwe8ajaHoWTOPxOIqSJJnESfQ4YD_azA4wmY5GYTRJxu4xHQ8YZU2LL_0fof0xPP4ClmC3zg)