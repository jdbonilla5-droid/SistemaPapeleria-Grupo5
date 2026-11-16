
package com.papeleria.nomina.negocio;

public class DetalleNomina {

    private int idDetalle;
    private int idNomina;
    private int idMotivo;
    private double valor;
    private String observacion;

    public DetalleNomina() {
    }

    public DetalleNomina(int idDetalle, int idNomina,
            int idMotivo, double valor, String observacion) {

        this.idDetalle = idDetalle;
        this.idNomina = idNomina;
        this.idMotivo = idMotivo;
        this.valor = valor;
        this.observacion = observacion;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(int idNomina) {
        this.idNomina = idNomina;
    }

    public int getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(int idMotivo) {
        this.idMotivo = idMotivo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
