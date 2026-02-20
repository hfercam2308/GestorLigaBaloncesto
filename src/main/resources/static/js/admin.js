const userData = obtenerUsuarioActual();
if (!userData || userData.rol !== 'ADMIN') {
    window.location.href = '/index.html';
}

document.getElementById('adminName').textContent = userData.username;

let equipos = [];
let usuarios = [];

document.addEventListener('DOMContentLoaded', async () => {
    await cargarDatos();
    cargarUsuarios();
});

async function cargarDatos() {
    try {
        equipos = await fetchGET('/equipos');
        usuarios = await fetchGET('/usuarios');
    } catch (error) {
        console.error('Error cargando datos:', error);
    }
}

// ==================== NAVEGACION ====================

function mostrarSeccion(seccion) {
    document.querySelectorAll('.seccion').forEach(s => s.classList.remove('active'));
    document.getElementById(`seccion-${seccion}`).classList.add('active');

    document.querySelectorAll('.sidebar nav a').forEach(a => a.classList.remove('active'));
    event.target.classList.add('active');

    switch (seccion) {
        case 'usuarios': cargarUsuarios(); break;
        case 'equipos': cargarEquipos(); break;
        case 'jugadores': cargarJugadoresTabla(); break;
        case 'partidos': cargarPartidos(); break;
        case 'resultados': cargarResultados(); break;
        case 'clasificacion': cargarClasificacion(); break;
    }
}

// ==================== MODALES ====================

function cerrarModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

document.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal')) {
        e.target.classList.remove('active');
    }
});

// ==================== USUARIOS ====================

async function cargarUsuarios() {
    try {
        const lista = await fetchGET('/usuarios');
        const tbody = document.querySelector('#tablaUsuarios tbody');
        tbody.innerHTML = '';

        lista.forEach(usuario => {
            const equipo = equipos.find(e => e.id === usuario.equipoId);
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${usuario.username}</td>
                <td>${usuario.nombre} ${usuario.apellidos || ''}</td>
                <td>${usuario.email}</td>
                <td><span class="rol-badge rol-${usuario.rol}">${usuario.rol}</span></td>
                <td>${equipo ? equipo.nombre : '-'}</td>
                <td>${usuario.activo ? '<span style="color:green">Activo</span>' : '<span style="color:red">Inactivo</span>'}</td>
                <td>
                    <button class="btn-secondary" onclick="abrirModalEditarUsuario('${usuario.id}')">Editar</button>
                    <button class="btn-secondary" onclick="toggleActivoUsuario('${usuario.id}', ${!usuario.activo})">
                        ${usuario.activo ? 'Desactivar' : 'Activar'}
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error cargando usuarios:', error);
    }
}

async function toggleActivoUsuario(id, nuevoEstado) {
    try {
        const usuario = await fetchGET(`/usuarios/${id}`);
        usuario.activo = nuevoEstado;
        await fetchPUT(`/usuarios/${id}`, usuario);
        await cargarDatos();
        cargarUsuarios();
    } catch (error) {
        alert('Error al actualizar usuario: ' + error.message);
    }
}

async function abrirModalEditarUsuario(id) {
    try {
        const usuario = await fetchGET(`/usuarios/${id}`);
        document.getElementById('usuarioEditId').value = usuario.id;
        document.getElementById('usuarioNombre').value = usuario.nombre || '';
        document.getElementById('usuarioApellidos').value = usuario.apellidos || '';
        document.getElementById('usuarioEmail').value = usuario.email || '';
        document.getElementById('usuarioRol').value = usuario.rol;

        const selectEquipo = document.getElementById('usuarioEquipoSelect');
        selectEquipo.innerHTML = '<option value="">Sin equipo</option>';
        equipos.forEach(eq => {
            const selected = eq.id === usuario.equipoId ? 'selected' : '';
            selectEquipo.innerHTML += `<option value="${eq.id}" ${selected}>${eq.nombre}</option>`;
        });

        document.getElementById('modalEditarUsuario').classList.add('active');
    } catch (error) {
        alert('Error al cargar datos del usuario: ' + error.message);
    }
}

document.getElementById('formEditarUsuario').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('usuarioEditId').value;
    const data = {
        nombre: document.getElementById('usuarioNombre').value,
        apellidos: document.getElementById('usuarioApellidos').value,
        email: document.getElementById('usuarioEmail').value,
        rol: document.getElementById('usuarioRol').value,
        equipoId: document.getElementById('usuarioEquipoSelect').value || null
    };

    try {
        const usuarioOriginal = await fetchGET(`/usuarios/${id}`);
        const usuarioActualizado = { ...usuarioOriginal, ...data };

        await fetchPUT(`/usuarios/${id}`, usuarioActualizado);
        alert('Usuario actualizado correctamente');
        cerrarModal('modalEditarUsuario');
        await cargarDatos();
        cargarUsuarios();
    } catch (error) {
        alert('Error al actualizar usuario: ' + error.message);
    }
});

// ==================== EQUIPOS ====================

async function cargarEquipos() {
    try {
        equipos = await fetchGET('/equipos');
        const tbody = document.querySelector('#tablaEquipos tbody');
        tbody.innerHTML = '';

        equipos.forEach(equipo => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${equipo.nombre}</td>
                <td>${equipo.universidad || '-'}</td>
                <td>${equipo.ciudad || '-'}</td>
                <td>${equipo.victorias || 0}-${equipo.derrotas || 0}</td>
                <td>${equipo.puntos || 0}</td>
                <td>
                    <button class="btn-secondary" onclick="editarEquipo('${equipo.id}')">Editar</button>
                    <button class="btn-danger" onclick="eliminarEquipo('${equipo.id}')">Eliminar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error cargando equipos:', error);
    }
}

function mostrarModalEquipo() {
    document.getElementById('modalEquipoTitulo').textContent = 'Crear Equipo';
    document.getElementById('equipoEditId').value = '';
    document.getElementById('equipoNombreInput').value = '';
    document.getElementById('equipoUniversidad').value = '';
    document.getElementById('equipoCiudad').value = '';
    document.getElementById('equipoColorPrimario').value = '#667eea';
    document.getElementById('equipoColorSecundario').value = '#764ba2';
    document.getElementById('modalEquipo').classList.add('active');
}

async function editarEquipo(id) {
    try {
        const equipo = await fetchGET(`/equipos/${id}`);
        document.getElementById('modalEquipoTitulo').textContent = 'Editar Equipo';
        document.getElementById('equipoEditId').value = equipo.id;
        document.getElementById('equipoNombreInput').value = equipo.nombre || '';
        document.getElementById('equipoUniversidad').value = equipo.universidad || '';
        document.getElementById('equipoCiudad').value = equipo.ciudad || '';
        document.getElementById('equipoColorPrimario').value = equipo.colorPrimario || '#667eea';
        document.getElementById('equipoColorSecundario').value = equipo.colorSecundario || '#764ba2';
        document.getElementById('modalEquipo').classList.add('active');
    } catch (error) {
        alert('Error al cargar equipo: ' + error.message);
    }
}

document.getElementById('formEquipo').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('equipoEditId').value;
    const data = {
        nombre: document.getElementById('equipoNombreInput').value,
        universidad: document.getElementById('equipoUniversidad').value,
        ciudad: document.getElementById('equipoCiudad').value,
        colorPrimario: document.getElementById('equipoColorPrimario').value,
        colorSecundario: document.getElementById('equipoColorSecundario').value
    };

    try {
        if (id) {
            await fetchPUT(`/equipos/${id}`, data);
            alert('Equipo actualizado correctamente');
        } else {
            await fetchPOST('/equipos', data);
            alert('Equipo creado correctamente');
        }
        cerrarModal('modalEquipo');
        await cargarDatos();
        cargarEquipos();
    } catch (error) {
        alert('Error al guardar equipo: ' + error.message);
    }
});

async function eliminarEquipo(id) {
    if (!confirm('¿Estas seguro de eliminar este equipo?')) return;
    try {
        await fetchDELETE(`/equipos/${id}`);
        alert('Equipo eliminado correctamente');
        await cargarDatos();
        cargarEquipos();
    } catch (error) {
        alert('Error al eliminar equipo: ' + error.message);
    }
}

// ==================== JUGADORES ====================

async function cargarJugadoresTabla() {
    try {
        const jugadores = await fetchGET('/usuarios/jugadores');
        const tbody = document.querySelector('#tablaJugadores tbody');
        tbody.innerHTML = '';

        jugadores.forEach(jugador => {
            const equipo = equipos.find(e => e.id === jugador.equipoId);
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>#${jugador.dorsal || '-'}</td>
                <td>${jugador.nombre} ${jugador.apellidos || ''}</td>
                <td>${equipo ? equipo.nombre : 'Sin equipo'}</td>
                <td>${formatearPosicion(jugador.posicion)}</td>
                <td>${jugador.altura ? jugador.altura + ' cm' : '-'}</td>
                <td>
                    <button class="btn-secondary" onclick="editarJugador('${jugador.id}')">Editar</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error cargando jugadores:', error);
    }
}

async function editarJugador(id) {
    try {
        const jugador = await fetchGET(`/usuarios/${id}`);

        document.getElementById('jugadorEditId').value = jugador.id;
        document.getElementById('jugadorDorsal').value = jugador.dorsal || '';
        document.getElementById('jugadorPosicion').value = jugador.posicion || '';
        document.getElementById('jugadorAltura').value = jugador.altura || '';
        document.getElementById('jugadorPeso').value = jugador.peso || '';
        document.getElementById('jugadorNacionalidad').value = jugador.nacionalidad || '';

        const selectEquipo = document.getElementById('jugadorEquipo');
        selectEquipo.innerHTML = '<option value="">Sin equipo</option>';
        equipos.forEach(eq => {
            const selected = eq.id === jugador.equipoId ? 'selected' : '';
            selectEquipo.innerHTML += `<option value="${eq.id}" ${selected}>${eq.nombre}</option>`;
        });

        document.getElementById('modalJugador').classList.add('active');
    } catch (error) {
        alert('Error al cargar jugador: ' + error.message);
    }
}

document.getElementById('formJugador').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('jugadorEditId').value;
    const data = {
        dorsal: document.getElementById('jugadorDorsal').value ? parseInt(document.getElementById('jugadorDorsal').value) : null,
        posicion: document.getElementById('jugadorPosicion').value || null,
        altura: document.getElementById('jugadorAltura').value ? parseFloat(document.getElementById('jugadorAltura').value) : null,
        peso: document.getElementById('jugadorPeso').value ? parseFloat(document.getElementById('jugadorPeso').value) : null,
        nacionalidad: document.getElementById('jugadorNacionalidad').value || null,
        equipoId: document.getElementById('jugadorEquipo').value || null
    };

    try {
        await fetchPUT(`/usuarios/${id}/datos-jugador`, data);
        alert('Datos de jugador actualizados correctamente');
        cerrarModal('modalJugador');
        await cargarDatos();
        cargarJugadoresTabla();
    } catch (error) {
        alert('Error al actualizar jugador: ' + error.message);
    }
});

// ==================== PARTIDOS ====================

async function cargarPartidos() {
    try {
        const partidos = await fetchGET('/partidos/dtos');
        const container = document.getElementById('partidosLista');
        container.innerHTML = '';

        if (partidos.length === 0) {
            container.innerHTML = '<p style="text-align:center;color:#666;">No hay partidos programados</p>';
            return;
        }

        partidos.forEach(partido => {
            const card = crearPartidoCard(partido);
            container.appendChild(card);
        });
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

    const resultado = partido.equipoLocalPuntos != null
        ? `<div class="resultado-puntuacion">${partido.equipoLocalPuntos} - ${partido.equipoVisitantePuntos}</div>`
        : '';

    div.innerHTML = `
        <div class="partido-header">
            <div>
                <div class="partido-jornada">Jornada ${partido.jornada}</div>
                <div class="partido-fecha">${formatearFecha(partido.fecha)} - ${partido.hora || ''}</div>
            </div>
            <span class="estado-badge estado-${partido.estado}">${formatearEstado(partido.estado)}</span>
        </div>
        <div class="partido-equipos">${partido.equipoLocalNombre} vs ${partido.equipoVisitanteNombre}</div>
        ${resultado}
        <div class="partido-info">
            <p>Pabellon: ${partido.pabellonNombre || 'Sin pabellon'}</p>
        </div>
        ${clima}
        <div style="margin-top: 15px;">
            <button class="btn-secondary" onclick="actualizarClimaPartido('${partido.id}')">Actualizar Clima</button>
            ${partido.estado !== 'FINALIZADO' ? `<button class="btn-primary" onclick="mostrarModalResultado('${partido.id}', '${partido.equipoLocalNombre}', '${partido.equipoVisitanteNombre}')">Registrar Resultado</button>` : ''}
            <button class="btn-danger" onclick="eliminarPartido('${partido.id}')">Eliminar</button>
        </div>
    `;

    return div;
}

async function actualizarClimaPartido(partidoId) {
    try {
        await fetchPOST(`/weather/partido/${partidoId}/actualizar`, {});
        alert('Clima actualizado correctamente');
        cargarPartidos();
    } catch (error) {
        alert('Error al actualizar el clima: ' + error.message);
    }
}

async function mostrarModalPartido() {
    const selectLocal = document.getElementById('partidoEquipoLocal');
    const selectVisitante = document.getElementById('partidoEquipoVisitante');

    selectLocal.innerHTML = '<option value="">Seleccionar...</option>';
    selectVisitante.innerHTML = '<option value="">Seleccionar...</option>';

    equipos.forEach(eq => {
        selectLocal.innerHTML += `<option value="${eq.id}">${eq.nombre}</option>`;
        selectVisitante.innerHTML += `<option value="${eq.id}">${eq.nombre}</option>`;
    });

    document.getElementById('partidoFecha').value = '';
    document.getElementById('partidoHora').value = '';
    document.getElementById('partidoJornada').value = '';
    document.getElementById('partidoPabellonNombre').value = '';
    document.getElementById('partidoPabellonCiudad').value = '';
    document.getElementById('partidoObservaciones').value = '';

    document.getElementById('modalPartido').classList.add('active');
}

document.getElementById('formPartido').addEventListener('submit', async (e) => {
    e.preventDefault();

    const equipoLocalId = document.getElementById('partidoEquipoLocal').value;
    const equipoVisitanteId = document.getElementById('partidoEquipoVisitante').value;

    if (equipoLocalId === equipoVisitanteId) {
        alert('El equipo local y visitante no pueden ser el mismo');
        return;
    }

    const data = {
        equipoLocalId: equipoLocalId,
        equipoVisitanteId: equipoVisitanteId,
        fecha: document.getElementById('partidoFecha').value,
        hora: document.getElementById('partidoHora').value,
        jornada: parseInt(document.getElementById('partidoJornada').value),
        estado: 'PROGRAMADO',
        datosPabellon: {
            nombre: document.getElementById('partidoPabellonNombre').value || null,
            ciudad: document.getElementById('partidoPabellonCiudad').value || null
        },
        observaciones: document.getElementById('partidoObservaciones').value || null
    };

    try {
        await fetchPOST('/partidos', data);
        alert('Partido creado correctamente');
        cerrarModal('modalPartido');
        cargarPartidos();
    } catch (error) {
        alert('Error al crear partido: ' + error.message);
    }
});

async function eliminarPartido(id) {
    if (!confirm('¿Estas seguro de eliminar este partido?')) return;
    try {
        await fetchDELETE(`/partidos/${id}`);
        alert('Partido eliminado correctamente');
        cargarPartidos();
    } catch (error) {
        alert('Error al eliminar partido: ' + error.message);
    }
}

// ==================== RESULTADOS ====================

function mostrarModalResultado(partidoId, equipoLocal, equipoVisitante) {
    document.getElementById('resultadoPartidoId').value = partidoId;
    document.getElementById('resultadoPartidoInfo').innerHTML = `
        <p><strong>${equipoLocal} vs ${equipoVisitante}</strong></p>
    `;
    document.getElementById('resultadoPuntosLocal').value = '';
    document.getElementById('resultadoPuntosVisitante').value = '';
    document.getElementById('resultadoObservaciones').value = '';
    document.getElementById('modalResultado').classList.add('active');
}

document.getElementById('formResultado').addEventListener('submit', async (e) => {
    e.preventDefault();

    const partidoId = document.getElementById('resultadoPartidoId').value;
    const data = {
        equipoLocalPuntos: parseInt(document.getElementById('resultadoPuntosLocal').value),
        equipoVisitantePuntos: parseInt(document.getElementById('resultadoPuntosVisitante').value),
        observaciones: document.getElementById('resultadoObservaciones').value || null
    };

    try {
        await fetchPOST(`/partidos/${partidoId}/resultado`, data);
        alert('Resultado registrado correctamente');
        cerrarModal('modalResultado');
        cargarPartidos();
    } catch (error) {
        alert('Error al registrar resultado: ' + error.message);
    }
});

async function cargarResultados() {
    try {
        const partidos = await fetchGET('/partidos');
        const partidosFinalizados = partidos.filter(p => p.estado === 'FINALIZADO' && p.equipoLocalPuntos != null);

        const container = document.getElementById('resultadosLista');
        container.innerHTML = '';

        if (partidosFinalizados.length === 0) {
            container.innerHTML = '<p style="text-align:center;color:#666;">No hay resultados registrados</p>';
            return;
        }

        for (const partido of partidosFinalizados) {
            const equipoLocal = equipos.find(e => e.id === partido.equipoLocalId);
            const equipoVisitante = equipos.find(e => e.id === partido.equipoVisitanteId);

            const div = document.createElement('div');
            div.className = 'partido-card';
            div.innerHTML = `
                <div class="resultado-equipos">
                    ${equipoLocal ? equipoLocal.nombre : 'Equipo'} vs ${equipoVisitante ? equipoVisitante.nombre : 'Equipo'}
                </div>
                <div class="resultado-puntuacion">
                    ${partido.equipoLocalPuntos} - ${partido.equipoVisitantePuntos}
                </div>
                <p style="text-align: center; color: #666;">
                    ${formatearFecha(partido.fecha)} - Jornada ${partido.jornada}
                </p>
            `;
            container.appendChild(div);
        }
    } catch (error) {
        console.error('Error cargando resultados:', error);
    }
}

// ==================== CLASIFICACION ====================

async function cargarClasificacion() {
    try {
        const equiposClasif = await fetchGET('/equipos/clasificacion');
        const tbody = document.querySelector('#tablaClasificacion tbody');
        tbody.innerHTML = '';

        equiposClasif.forEach((equipo, index) => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${index + 1}</td>
                <td>${equipo.nombre}</td>
                <td>${(equipo.victorias || 0) + (equipo.derrotas || 0)}</td>
                <td>${equipo.victorias || 0}</td>
                <td>${equipo.derrotas || 0}</td>
                <td><strong>${equipo.puntos || 0}</strong></td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('Error cargando clasificacion:', error);
    }
}
