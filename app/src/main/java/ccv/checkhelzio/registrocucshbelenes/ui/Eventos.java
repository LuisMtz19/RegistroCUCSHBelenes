package ccv.checkhelzio.registrocucshbelenes.ui;

import android.graphics.drawable.Drawable;

/**
 * Created by check on 23/09/2016.
 */

public class Eventos {
    String titulo_evento;
    String nombre_organizador;
    String auditorio;
    String horario;
    int fondo;

    public Eventos(String titulo_evento, String nombre_organizador, String auditorio, String horario, int fondo) {
        this.titulo_evento = titulo_evento;
        this.nombre_organizador = nombre_organizador;
        this.auditorio = auditorio;
        this.horario = horario;
        this.fondo = fondo;
    }

    public String getTitulo_evento() {
        return titulo_evento;
    }

    public void setTitulo_evento(String titulo_evento) {
        this.titulo_evento = titulo_evento;
    }

    public String getNombre_organizador() {
        return nombre_organizador;
    }

    public void setNombre_organizador(String nombre_organizador) {
        this.nombre_organizador = nombre_organizador;
    }

    public String getAuditorio() {
        return auditorio;
    }

    public void setAuditorio(String auditorio) {
        this.auditorio = auditorio;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getFondo() {
        return fondo;
    }

    public void setFondo(int fondo) {
        this.fondo = fondo;
    }
}
