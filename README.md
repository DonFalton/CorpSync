# 🚀 CorpSync - Sistema de Gestión Inteligente de Tickets IT

![Estado](https://img.shields.io/badge/Estado-En_Desarrollo-orange)
![Curso](https://img.shields.io/badge/Proyecto_Final-DAM-blue)
![Arquitectura](https://img.shields.io/badge/Arquitectura-Cloud_Native-success)

**CorpSync AI** es una plataforma integral de gestión de servicios IT (ITSM) diseñada para automatizar y agilizar la resolución de incidencias en entornos corporativos. El sistema utiliza un modelo de arquitectura cliente-servidor desacoplado.

---

## 🏗️ Arquitectura del Sistema

El proyecto está diseñado como un ecosistema multiplataforma con una única fuente de la verdad en la nube, cumpliendo con todos los requisitos del ciclo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

* **🧠 Backend:** Supabase (PostgreSQL, Auth, Storage).
* **🌐 App Web (Técnicos):** React.js + Vite + Tailwind CSS.
* **📱 App Móvil (Empleados):** Por decidir.
* **💻 App Escritorio (Administración):** Java Swing + JDBC.

---

## 📂 Estructura del Monorepo

Este repositorio utiliza una estructura de Monorepo. Cada cliente es un ecosistema independiente que se conecta al backend central.

```text
Corpsync-AI/
├── 📁 app-web/          # Panel de control Kanban para técnicos (React)
├── 📁 app-movil/        # App para reporte de incidencias con cámara (React Native)
├── 📁 app-escritorio/   # Backoffice para gestión masiva y ficheros (Java Swing)
├── 📁 backend/          # Scripts SQL, Triggers
└── 📄 README.md         # Documentación principal del proyecto