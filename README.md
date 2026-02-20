# 🏀 Gestor Liga Baloncesto

¡Bienvenido al **Gestor de Liga de Baloncesto**! Una aplicación integral diseñada para la gestión de equipos, partidos y usuarios, con integración de datos meteorológicos y seguridad avanzada.

---

## 🚀 Tecnologías Principales

Este proyecto utiliza un stack tecnológico moderno para garantizar robustez y escalabilidad:

*   **🍃 MongoDB:** Base de datos NoSQL para un almacenamiento flexible y dinámico.
*   **🏗️ Spring Boot:** Framework principal para una arquitectura API REST eficiente.
*   **🛡️ Spring Security:** Protección de datos mediante el encriptado de contraseñas con **BCrypt**.
*   **🌤️ OpenWeatherMap API:** Integración de clima en tiempo real para la planificación de partidos.

---

## 🛠️ Configuración del Proyecto

Para mantener el proyecto seguro, las claves sensibles se gestionan de forma externa:

1.  **Clonación:**
    ```bash
    git clone https://github.com/hfercam2308/GestorLigaBaloncesto.git
    ```
2.  **Clave de API:**
    Crea un archivo en `src/main/resources/application-local.properties` (este archivo está ignorado por Git) y añade tu clave:
    ```properties
    weather.api.key=TU_API_KEY_AQUI
    ```

---

## 📸 Funcionalidades

*   ✅ Gestión completa de Equipos y Jugadores.
*   ✅ Programación de Partidos con predicción meteorológica.
*   ✅ Sistema de autenticación seguro.
*   ✅ API REST documentada y lista para su consumo.

---

Desarrollado con ❤️ por [hfercam2308](https://github.com/hfercam2308).
