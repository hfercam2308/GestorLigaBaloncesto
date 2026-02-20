# 🏀 Gestor Liga Baloncesto

¡Bienvenido al **Gestor de Liga de Baloncesto**! Esta aplicación es una solución robusta y moderna diseñada para gestionar equipos, partidos y usuarios de una liga de baloncesto, integrando datos en tiempo real y seguridad avanzada.

---

## 🚀 Tecnologías Utilizadas

Este proyecto ha sido desarrollado utilizando un stack tecnológico moderno y eficiente:

*   **🍃 Base de Datos MongoDB:** Almacenamiento no relacional flexible y escalable para gestionar toda la información de la liga.
*   **🏗️ Arquitectura API REST + Spring Boot:** Estructura sólida y modular para el backend, garantizando un rendimiento óptimo y facilidad de mantenimiento.
*   **🛡️ Spring Security (Hashing BCrypt):** Implementación de seguridad de alto nivel para la protección de usuarios, utilizando BCrypt para el encriptado de contraseñas.
*   **🌤️ API OpenWeatherMap:** Sistema inteligente que indica la temperatura y condiciones meteorológicas para los encuentros deportivos.

---

## 🛠️ Instalación y Configuración

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/hfercam2308/GestorLigaBaloncesto.git
    ```
2.  **Configurar las variables de entorno:**
    Crea un archivo `application.properties` o configura las variables de entorno necesarias (ver sección de Seguridad).
3.  **Ejecutar la aplicación:**
    ```bash
    mvn spring-boot:run
    ```

---

## 🔒 Seguridad y Claves Privadas

Para mantener tus claves a salvo y no exponerlas en repositorios públicos:

1.  **Variables de Entorno:** No escribas las APIs directamente en el código. Usa `${WEATHER_API_KEY}` en tu `application.properties`.
2.  **.gitignore:** Asegúrate de incluir archivos sensibles como `.env` o configuraciones locales en tu `.gitignore`.
3.  **Configuración Recomendada:**
    ```properties
    weather.api.key=${WEATHER_API_KEY}
    ```

---

Desarrollado con ❤️ para la gestión deportiva.
