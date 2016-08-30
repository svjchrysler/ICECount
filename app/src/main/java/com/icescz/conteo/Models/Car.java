package com.icescz.conteo.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Car extends RealmObject{

    @PrimaryKey
    private int id;
    private String nombreEncuestador;
    private int particular;
    private int bicicleta;
    private int motocicleta;
    private int taxi;
    private int publico;
    private int repartidor;
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

    public int getParticular() {
        return particular;
    }

    public void setParticular(int particular) {
        this.particular = particular;
    }

    public int getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(int bicicleta) {
        this.bicicleta = bicicleta;
    }

    public int getMotocicleta() {
        return motocicleta;
    }

    public void setMotocicleta(int motocicleta) {
        this.motocicleta = motocicleta;
    }

    public int getTaxi() {
        return taxi;
    }

    public void setTaxi(int taxi) {
        this.taxi = taxi;
    }

    public int getPublico() {
        return publico;
    }

    public void setPublico(int publico) {
        this.publico = publico;
    }

    public int getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(int repartidor) {
        this.repartidor = repartidor;
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
