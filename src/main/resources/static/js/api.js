const API_BASE_URL = 'http://localhost:8081/api';

async function fetchGET(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error en GET:', error);
        throw error;
    }
}

async function fetchPOST(endpoint, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.mensaje || 'Error en la petición');
        }

        return await response.json();
    } catch (error) {
        console.error('Error en POST:', error);
        throw error;
    }
}

async function fetchPUT(endpoint, data) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.mensaje || 'Error en la petición');
        }

        return await response.json();
    } catch (error) {
        console.error('Error en PUT:', error);
        throw error;
    }
}

async function fetchDELETE(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        return true;
    } catch (error) {
        console.error('Error en DELETE:', error);
        throw error;
    }
}

function obtenerUsuarioActual() {
    const userDataStr = localStorage.getItem('userData');
    if (!userDataStr) {
        window.location.href = '/index.html';
        return null;
    }
    return JSON.parse(userDataStr);
}

function cerrarSesion() {
    localStorage.removeItem('userData');
    window.location.href = '/index.html';
}

function formatearFecha(fecha) {
    const date = new Date(fecha);
    const opciones = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('es-ES', opciones);
}

function formatearPosicion(posicion) {
    const posiciones = {
        'BASE': 'Base',
        'ESCOLTA': 'Escolta',
        'ALERO': 'Alero',
        'ALA_PIVOT': 'Ala-Pívot',
        'PIVOT': 'Pívot'
    };
    return posiciones[posicion] || posicion;
}

function formatearEstado(estado) {
    const estados = {
        'PROGRAMADO': 'Programado',
        'EN_CURSO': 'En Curso',
        'FINALIZADO': 'Finalizado',
        'SUSPENDIDO': 'Suspendido'
    };
    return estados[estado] || estado;
}