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
├── 📁 app-movil/        # App para reporte de incidencias con cámara (React Native o Java/Kotlin)
├── 📁 app-escritorio/   # Backoffice para gestión masiva y ficheros (Java Swing)
├── 📁 backend/          # Scripts SQL, Triggers
└── 📄 README.md         # Documentación principal del proyecto
```
---

## 🛠️ Requisitos Previos

Apartado aún en desarrollo

---

## 👥 Reglas de Contribución y Equipo

Para mantener el historial limpio y evitar conflictos de fusión, cada miembro trabajará exclusivamente en su directorio asignado.

* **Martín (Project Manager / Fullstack):** `app-web` y `backend`
* **Rafa (Mobile Developer):** `app-movil`
* **Germán (Desktop Developer):** `app-escritorio`

**Regla de Seguridad:** NUNCA hacer commit de archivos `.env`, `.env.local`, contraseñas JDBC ni API Keys de Google Gemini.

---
*Proyecto de Trabajo Final de Ciclo - DAM*

[![](https://mermaid.ink/img/pako:eNp9Vdtu2zgQ_ZUBgRY2YKe6WK7NN6-dtilirDcuUuzCL2NpqrChSIGijKRBPmYf97mfkB8rJdqNvAnMB2FI6pw5mjOkHliqM2Kc5ais3ShwwworCT7p7wgZwVVtEYYw16Zc36sULr6sl9BbUIXGaCk1rNCgJKn7Hp2hpQ_aFGgB_nZjuFwOFwu_h3eiOuy9yd69Kfyyf1aUWqEVfMCKIOQwF4UgZXXlt8-VNZQjzJSl0uh797aGt3BFpYaPwn6qt81bHMMBREE0HgbxMJoe4tEwGHuaNdm6hHVd4rbJ8xa-4FZiBeu_LqFdAU8TnaZ5RXLEYXm96mZx1XOqtVG6gt5X2g5g-fRzJ-QAPuMO-y7NNuxQv3-Ow8DzXOpcKMil3qKE3qy2N314Ofg26kI78V6ty81hJZRzMtWytdWK9JZsdUwTd6HPcfTe03j1HC5URcaiAUt3zoRSotJHPKPTPOdVaoSri9AcLokMlGS-CUmuSp8Xf8z7B5rkNM0rHsQcZjtUPzDDY8mz1QXMn_4tXK_CPcylrjNYOwmYe8d52nEimhziZBiGLzWvjE5d__v2hPn6GgqsxE43NNFpmtaJ813b1653UVpRUNsca90a0vdq4tdpgv2nX6N02lPx9FM5Nyzlvycz6P1Zpq4gKJsGS0dd8Es9r9RwxMGd7lIKymvy-ytTkzscTd9QZc3TfxXQcd69ZVnYSRF14sQTzXVRCrnHNGzfKXX3y7b1_gzL23dndEd9nh1hO_H--58FNhWF3jWZlOT_zwbP4tM0V5Rh6sUsqXDmNs3x0birwS8u3KInGnXBnXjKBiw3ImPcuhoNWEHudmum7KFJsWH2hgraMO7CDM3thm3Uo8OUqP7RujjAjK7zG8a_oazcrC6bS3Qh0JW3-L1qSGVk5rpWlvEwCYKWhfEHdsf4cByehc2YhpNgEkdxMBqwe8ajaHoWTOPxOIqSJJnESfQ4YD_azA4wmY5GYTRJxu4xHQ8YZU2LL_0fof0xPP4ClmC3zg?type=png)](https://mermaid.live/edit#pako:eNp9Vdtu2zgQ_ZUBgRY2YKe6WK7NN6-dtilirDcuUuzCL2NpqrChSIGijKRBPmYf97mfkB8rJdqNvAnMB2FI6pw5mjOkHliqM2Kc5ais3ShwwworCT7p7wgZwVVtEYYw16Zc36sULr6sl9BbUIXGaCk1rNCgJKn7Hp2hpQ_aFGgB_nZjuFwOFwu_h3eiOuy9yd69Kfyyf1aUWqEVfMCKIOQwF4UgZXXlt8-VNZQjzJSl0uh797aGt3BFpYaPwn6qt81bHMMBREE0HgbxMJoe4tEwGHuaNdm6hHVd4rbJ8xa-4FZiBeu_LqFdAU8TnaZ5RXLEYXm96mZx1XOqtVG6gt5X2g5g-fRzJ-QAPuMO-y7NNuxQv3-Ow8DzXOpcKMil3qKE3qy2N314Ofg26kI78V6ty81hJZRzMtWytdWK9JZsdUwTd6HPcfTe03j1HC5URcaiAUt3zoRSotJHPKPTPOdVaoSri9AcLokMlGS-CUmuSp8Xf8z7B5rkNM0rHsQcZjtUPzDDY8mz1QXMn_4tXK_CPcylrjNYOwmYe8d52nEimhziZBiGLzWvjE5d__v2hPn6GgqsxE43NNFpmtaJ813b1653UVpRUNsca90a0vdq4tdpgv2nX6N02lPx9FM5Nyzlvycz6P1Zpq4gKJsGS0dd8Es9r9RwxMGd7lIKymvy-ytTkzscTd9QZc3TfxXQcd69ZVnYSRF14sQTzXVRCrnHNGzfKXX3y7b1_gzL23dndEd9nh1hO_H--58FNhWF3jWZlOT_zwbP4tM0V5Rh6sUsqXDmNs3x0birwS8u3KInGnXBnXjKBiw3ImPcuhoNWEHudmum7KFJsWH2hgraMO7CDM3thm3Uo8OUqP7RujjAjK7zG8a_oazcrC6bS3Qh0JW3-L1qSGVk5rpWlvEwCYKWhfEHdsf4cByehc2YhpNgEkdxMBqwe8ajaHoWTOPxOIqSJJnESfQ4YD_azA4wmY5GYTRJxu4xHQ8YZU2LL_0fof0xPP4ClmC3zg)