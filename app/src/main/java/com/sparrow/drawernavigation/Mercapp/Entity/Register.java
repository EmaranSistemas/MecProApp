package com.sparrow.drawernavigation.Mercapp.Entity;

public class Register {
    String id,fecha,local,motivo;

    public Register() {
    }

    public Register(String id, String fecha, String local,String motivo) {
        this.id = id;
        this.fecha = fecha;
        this.local = local;
        this.motivo = motivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}