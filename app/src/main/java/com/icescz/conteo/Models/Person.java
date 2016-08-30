package com.icescz.conteo.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Person extends RealmObject {

    @PrimaryKey
    private int id;
    private String nombreEncuestador;
    private int hombre;
    private int ninia;
    private int mujer;
    private int anciano;
    private String calleRelevamiento;
    private String calleLateralA;
    private String calleLateralB;
    private int temperatura;
    private String condiciones;
    private String horaInicio;
    private String horaFin;
    private String fecha;
    private int fijoCantidad;
    private String fijoNota;
    private int ambulanteCantidad;
    private String ambulanteNota;
    private int IndigenaHombre;
    private int IndigenaMujer;
    private String ImagenMapeo;
    private String ImagenAcera;
    private String cometarioAcera;
    private String nota;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreEncuestador() {
        return nombreEncuestador;
    }

    public void setNombreEncuestador(String nombreEncuestador) {
        this.nombreEncuestador = nombreEncuestador;
    }

    public int getHombre() {
        return hombre;
    }

    public void setHombre(int hombre) {
        this.hombre = hombre;
    }

    public int getNinia() {
        return ninia;
    }

    public void setNinia(int ninia) {
        this.ninia = ninia;
    }

    public int getMujer() {
        return mujer;
    }

    public void setMujer(int mujer) {
        this.mujer = mujer;
    }

    public int getAnciano() {
        return anciano;
    }

    public void setAnciano(int anciano) {
        this.anciano = anciano;
    }

    public String getCalleRelevamiento() {
        return calleRelevamiento;
    }

    public void setCalleRelevamiento(String calleRelevamiento) {
        this.calleRelevamiento = calleRelevamiento;
    }

    public String getCalleLateralA() {
        return calleLateralA;
    }

    public void setCalleLateralA(String calleLateralA) {
        this.calleLateralA = calleLateralA;
    }

    public String getCalleLateralB() {
        return calleLateralB;
    }

    public void setCalleLateralB(String calleLateralB) {
        this.calleLateralB = calleLateralB;
    }

    public int getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(int temperatura) {
        this.temperatura = temperatura;
    }

    public String getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(String condiciones) {
        this.condiciones = condiciones;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getFijoCantidad() {
        return fijoCantidad;
    }

    public void setFijoCantidad(int fijoCantidad) {
        this.fijoCantidad = fijoCantidad;
    }

    public String getFijoNota() {
        return fijoNota;
    }

    public void setFijoNota(String fijoNota) {
        this.fijoNota = fijoNota;
    }

    public int getAmbulanteCantidad() {
        return ambulanteCantidad;
    }

    public void setAmbulanteCantidad(int ambulanteCantidad) {
        this.ambulanteCantidad = ambulanteCantidad;
    }

    public String getAmbulanteNota() {
        return ambulanteNota;
    }

    public void setAmbulanteNota(String ambulanteNota) {
        this.ambulanteNota = ambulanteNota;
    }

    public int getIndigenaHombre() {
        return IndigenaHombre;
    }

    public void setIndigenaHombre(int indigenaHombre) {
        IndigenaHombre = indigenaHombre;
    }

    public int getIndigenaMujer() {
        return IndigenaMujer;
    }

    public void setIndigenaMujer(int indigenaMujer) {
        IndigenaMujer = indigenaMujer;
    }

    public String getImagenMapeo() {
        return ImagenMapeo;
    }

    public void setImagenMapeo(String imagenMapeo) {
        ImagenMapeo = imagenMapeo;
    }

    public String getImagenAcera() {
        return ImagenAcera;
    }

    public void setImagenAcera(String imagenAcera) {
        ImagenAcera = imagenAcera;
    }

    public String getCometarioAcera() {
        return cometarioAcera;
    }

    public void setCometarioAcera(String cometarioAcera) {
        this.cometarioAcera = cometarioAcera;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
