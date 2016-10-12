package ccv.checkhelzio.registrocucshbelenes.ui;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by check on 23/09/2016.
 */

public class Eventos implements Serializable{
    String horario;
    int fondo;
    String hora_inicial;
    String hora_final;
    String fecha_inicial;
    String fecha_final;
    String titulo_evento;
    String auditorio;
    String tipo_evento;
    String nombre_organizador;
    String numero_tel;
    String quien_registro;
    String cuando_registro;
    String notas;
    String repeticion;
    String id;
    String tag;

    public Eventos(String horario, int fondo, String hora_inicial, String hora_final, String fecha_inicial, String fecha_final, String titulo_evento, String auditorio, String tipo_evento, String nombre_organizador, String numero_tel, String quien_registro, String cuando_registro, String notas, String repeticion, String id, String tag) {
        this.horario = horario;
        this.fondo = fondo;
        this.hora_inicial = hora_inicial;
        this.hora_final = hora_final;
        this.fecha_inicial = fecha_inicial;
        this.fecha_final = fecha_final;
        this.titulo_evento = titulo_evento;
        this.auditorio = auditorio;
        this.tipo_evento = tipo_evento;
        this.nombre_organizador = nombre_organizador;
        this.numero_tel = numero_tel;
        this.quien_registro = quien_registro;
        this.cuando_registro = cuando_registro;
        this.notas = notas;
        this.repeticion = repeticion;
        this.id = id;
        this.tag = tag;
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

    public String getHora_inicial() {
        return hora_inicial;
    }

    public void setHora_inicial(String hora_inicial) {
        this.hora_inicial = hora_inicial;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public String getFecha_inicial() {
        return fecha_inicial;
    }

    public void setFecha_inicial(String fecha_inicial) {
        this.fecha_inicial = fecha_inicial;
    }

    public String getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(String fecha_final) {
        this.fecha_final = fecha_final;
    }

    public String getTitulo_evento() {
        return titulo_evento;
    }

    public void setTitulo_evento(String titulo_evento) {
        this.titulo_evento = titulo_evento;
    }

    public String getAuditorio() {
        return auditorio;
    }

    public void setAuditorio(String auditorio) {
        this.auditorio = auditorio;
    }

    public String getTipo_evento() {
        return tipo_evento;
    }

    public void setTipo_evento(String tipo_evento) {
        this.tipo_evento = tipo_evento;
    }

    public String getNombre_organizador() {
        return nombre_organizador;
    }

    public void setNombre_organizador(String nombre_organizador) {
        this.nombre_organizador = nombre_organizador;
    }

    public String getNumero_tel() {
        return numero_tel;
    }

    public void setNumero_tel(String numero_tel) {
        this.numero_tel = numero_tel;
    }

    public String getQuien_registro() {
        return quien_registro;
    }

    public void setQuien_registro(String quien_registro) {
        this.quien_registro = quien_registro;
    }

    public String getCuando_registro() {
        return cuando_registro;
    }

    public void setCuando_registro(String cuando_registro) {
        this.cuando_registro = cuando_registro;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getRepeticion() {
        return repeticion;
    }

    public void setRepeticion(String repeticion) {
        this.repeticion = repeticion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
