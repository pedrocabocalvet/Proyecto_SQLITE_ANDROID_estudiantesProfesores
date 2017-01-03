package com.example.caboc.sqliteestudiantesprofesores;



import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by caboc on 26/12/2016.
 */
public class Usuario implements Serializable {
    String nombre;
    int id;
    int edad;
    String cargo;
    int curso;
    String ciclo;
    String extra;




    public Usuario(int id, String nombre, int edad,  String ciclo, int curso, String extra, String cargo){
        this.nombre = nombre;
        this.id = id;
        this.cargo = cargo;
        this.curso = curso;
        this.ciclo = ciclo;
        this.extra = extra;
        this.edad = edad;


    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getCurso() {
        return curso;
    }

    public void setCurso(int curso) {
        this.curso = curso;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

}
