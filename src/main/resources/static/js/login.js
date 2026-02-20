if (localStorage.getItem('userData')) {
    const userData = JSON.parse(localStorage.getItem('userData'));
    redirigirSegunRol(userData.rol);
}

const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const errorMessage = document.getElementById('errorMessage');

        try {
            const response = await fetch('http://localhost:8081/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.mensaje || 'Error al iniciar sesión');
            }

            const data = await response.json();

            localStorage.setItem('userData', JSON.stringify(data));

            redirigirSegunRol(data.rol);

        } catch (error) {
            errorMessage.textContent = error.message;
            errorMessage.classList.add('active');
        }
    });
}

const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const nombre = document.getElementById('nombre').value;
        const apellidos = document.getElementById('apellidos').value;
        const email = document.getElementById('email').value;
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('confirmPassword').value;
        const rol = document.getElementById('rol').value;

        const errorMessage = document.getElementById('errorMessage');
        const successMessage = document.getElementById('successMessage');

        errorMessage.classList.remove('active');
        successMessage.classList.remove('active');

        if (password !== confirmPassword) {
            errorMessage.textContent = 'Las contraseñas no coinciden';
            errorMessage.classList.add('active');
            return;
        }

        try {
            const response = await fetch('http://localhost:8081/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    nombre,
                    apellidos,
                    email,
                    username,
                    password,
                    rol
                })
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.mensaje || 'Error al registrar usuario');
            }

            successMessage.textContent = 'Registro exitoso. Redirigiendo al login...';
            successMessage.classList.add('active');

            setTimeout(() => {
                window.location.href = '/index.html';
            }, 2000);

        } catch (error) {
            errorMessage.textContent = error.message;
            errorMessage.classList.add('active');
        }
    });
}

function redirigirSegunRol(rol) {
    const dashboards = {
        'ADMIN': '/html/dashboard-admin.html',
        'JUGADOR': '/html/dashboard-jugador.html'
    };
    window.location.href = dashboards[rol] || '/index.html';
}