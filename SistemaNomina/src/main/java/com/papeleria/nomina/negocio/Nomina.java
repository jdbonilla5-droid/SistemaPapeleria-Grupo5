package com.papeleria.nomina.negocio;

import java.util.Date;

public class Nomina {

    private int idNomina;
    private int idEmpleado;
    private Date fechaInicio;
    private Date fechaFin;
    private Date fechaPago;
    private double totalIngresos;
    private double totalEgresos;
    private double sueldoNeto;
    private String estado;

    public Nomina() {
    }

    public Nomina(int idNomina, int idEmpleado,
            Date fechaInicio, Date fechaFin,
            Date fechaPago, double totalIngresos,
            double totalEgresos, double sueldoNeto,
            String estado) {

        this.idNomina = idNomina;
        this.idEmpleado = idEmpleado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaPago = fechaPago;
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
        this.sueldoNeto = sueldoNeto;
        this.estado = estado;
    }

    public int getIdNomina() {
        return idNomina;
    }

    public void setIdNomina(int idNomina) {
        this.idNomina = idNomina;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(double totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public double getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(double totalEgresos) {
        this.totalEgresos = totalEgresos;
    }

    public double getSueldoNeto() {
        return sueldoNeto;
    }

    public void setSueldoNeto(double sueldoNeto) {
        this.sueldoNeto = sueldoNeto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}