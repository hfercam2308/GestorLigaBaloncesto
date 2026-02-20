package com.universidad.ligabasket.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private String rol;
    private String nombre;
    private String apellidos;
    private LocalDateTime fechaRegistro;
    private Boolean activo;
    private String equipoId;

    // Campos especificos de jugador (solo se rellenan cuando rol == JUGADOR)
    private Integer dorsal;
    private String posicion;
    private Double altura;
    private Double peso;
    private LocalDate fechaNacimiento;
    private String nacionalidad;
    private Integer anoIngreso;

    public Usuario() {
    }

    public Usuario(String id, String username, String email, String password, String rol, String nombre, String apellidos,
            LocalDateTime fechaRegistro, Boolean activo, String equipoId, Integer dorsal, String posicion,
            Double altura, Double peso, LocalDate fechaNacimiento, String nacionalidad, Integer anoIngreso) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
        this.equipoId = equipoId;
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.altura = altura;
        this.peso = peso;
        this.fechaNacimiento = fechaNacimiento;
        this.nacionalidad = nacionalidad;
        this.anoIngreso = anoIngreso;
    }

    public Usuario(String username, String email, String password, String rol, String nombre, String apellidos) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(String equipoId) {
        this.equipoId = equipoId;
    }

    public Integer getDorsal() {
        return dorsal;
    }

    public void setDorsal(Integer dorsal) {
        this.dorsal = dorsal;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Integer getAnoIngreso() {
        return anoIngreso;
    }

    public void setAnoIngreso(Integer anoIngreso) {
        this.anoIngreso = anoIngreso;
    }
}
