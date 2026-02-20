const userData = obtenerUsuarioActual();
if (!userData || userData.rol !== 'JUGADOR') {
    window.location.href = '/index.html';
}

document.getElementById('jugadorName').textContent = userData.username;

let miEquipo = null;
let miPerfil = null;

document.addEventListener('DOMContentLoaded', async () => {
    await cargarMiPerfil();
    cargarCompaneros();
});

async function cargarMiPerfil() {
    try {
        miPerfil = await fetchGET(`/usuarios/${userData.userId}`);

        if (miPerfil.equipoId) {
            miEquipo = await fetchGET(`/equipos/${miPerfil.equipoId}`);
            document.getElementById('equipoNombre').textContent = miEquipo.nombre;
        } else {
            document.getElementById('equipoNombre').textContent = 'Sin equipo asignado';
        }

        document.getElementById('dorsal').textContent = `#${miPerfil.dorsal || '-'}`;
        document.getElementById('posicion').textContent = formatearPosicion(miPerfil.posicion);
    } catch (error) {
        console.error('Error cargando perfil:', error);
        document.getElementById('equipoNombre').textContent = 'Sin equipo asignado';
    }
}

function mostrarSeccion(seccion) {
    document.querySelectorAll('.seccion').forEach(s => s.classList.remove('active'));
    document.getElementById(`seccion-${seccion}`).classList.add('active');

    document.querySelectorAll('.sidebar nav a').forEach(a => a.classList.remove('active'));
    event.target.classList.add('active');

    switch (seccion) {
        case 'companeros':
            cargarCompaneros();
            break;
        case 'partidos':
            cargarPartidos();
            break;
    }
}

async function cargarCompaneros() {
    try {
        if (!miPerfil || !miPerfil.equipoId) return;

        const jugadores = await fetchGET(`/usuarios/jugadores/equipo/${miPerfil.equipoId}`);
        const container = document.getElementById('jugadoresGrid');
        container.innerHTML = '';

        for (const jugador of jugadores) {
            const card = document.createElement('div');
            card.className = 'jugador-card';
            card.innerHTML = `
                <div class="jugador-dorsal">#${jugador.dorsal || '-'}</div>
                <div class="jugador-info">
                    <p><strong>${jugador.nombre} ${jugador.apellidos}</strong></p>
                    <p>Posicion: ${formatearPosicion(jugador.posicion)}</p>
                    <p>Altura: ${jugador.altura ? jugador.altura + ' cm' : '-'}</p>
                    <p>Peso: ${jugador.peso ? jugador.peso + ' kg' : '-'}</p>
                </div>
            `;
            container.appendChild(card);
        }
    } catch (error) {
        console.error('Error cargando companeros:', error);
    }
}

async function cargarPartidos() {
    try {
        if (!miPerfil || !miPerfil.equipoId) return;

        const partidos = await fetchGET(`/partidos/equipo/${miPerfil.equipoId}`);

        const proximosPartidos = partidos.filter(p => p.estado === 'PROGRAMADO');
        const partidosFinalizados = partidos.filter(p => p.estado === 'FINALIZADO');

        const containerProximos = document.getElementById('proximosPartidos');
        containerProximos.innerHTML = '';

        for (const partido of proximosPartidos) {
            const partidoDTO = await fetchGET(`/partidos/${partido.id}/dto`);
            const card = crearPartidoCard(partidoDTO);
            containerProximos.appendChild(card);
        }

        const containerResultados = document.getElementById('resultadosRecientes');
        containerResultados.innerHTML = '';

        for (const partido of partidosFinalizados.slice(0, 5)) {
            if (partido.equipoLocalPuntos == null) continue;

            const partidoDTO = await fetchGET(`/partidos/${partido.id}/dto`);

            const div = document.createElement('div');
            div.className = 'resultado-card';
            div.innerHTML = `
                <div class="resultado-equipos">
                    ${partidoDTO.equipoLocalNombre} vs ${partidoDTO.equipoVisitanteNombre}
                </div>
                <div class="resultado-puntuacion">
                    ${partido.equipoLocalPuntos} - ${partido.equipoVisitantePuntos}
                </div>
            `;
            containerResultados.appendChild(div);
        }
    } catch (error) {
        console.error('Error cargando partidos:', error);
    }
}

function crearPartidoCard(partido) {
    const div = document.createElement('div');
    div.className = 'partido-card';

    const clima = partido.condicionesMeteorologicas
        ? `<div class="clima-info">
             <img src="https://openweathermap.org/img/wn/${partido.condicionesMeteorologicas.icono}@2x.png" alt="clima" style="width:30px;vertical-align:middle;">
             ${partido.condicionesMeteorologicas.temperatura}°C - ${partido.condicionesMeteorologicas.descripcion}
           </div>`
        : '';

    div.innerHTML = `
        <div class="partido-header">
            <div class="partido-jornada">Jornada ${partido.jornada}</div>
            <div class="partido-fecha">${formatearFecha(partido.fecha)} - ${partido.hora}</div>
        </div>
        <div class="partido-equipos">${partido.equipoLocalNombre} vs ${partido.equipoVisitanteNombre}</div>
        <div class="partido-detalles">
            <p>Pabellon: ${partido.pabellonNombre}</p>
        </div>
        ${clima}
    `;

    return div;
}
