
package com.papeleria.nomina.negocio;

import com.papeleria.nomina.conexion.ConexionOracle;
import com.papeleria.nomina.persistencia.NominaDAO;
import com.papeleria.nomina.persistencia.DetalleNominaDAO;
import com.papeleria.nomina.persistencia.MotivoNominaDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NominaServicio {

    private final NominaDAO nominaDAO;
    private final DetalleNominaDAO detalleDAO;
    private final MotivoNominaDAO motivoDAO;

    public NominaServicio() {
        this.nominaDAO = new NominaDAO();
        this.detalleDAO = new DetalleNominaDAO();
        this.motivoDAO = new MotivoNominaDAO();
    }

    // REGISTRAR NOMINA CON TODOS SUS DETALLES
    public int registrarNominaConDetalles(
            Nomina nomina,
            List<DetalleNomina> detalles)
            throws SQLException {

        validarNomina(nomina);

        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe agregar al menos un concepto.");
        }

        // Consultar los motivos existentes
        List<MotivoNomina> motivos = motivoDAO.listarTodos();

        Map<Integer, MotivoNomina> mapaMotivos = new HashMap<>();

        for (MotivoNomina motivo : motivos) {
            mapaMotivos.put(motivo.getIdMotivo(), motivo);
        }

        double ingresos = 0;
        double egresos = 0;

        // Calcular ingresos y egresos
        for (DetalleNomina detalle : detalles) {

            if (detalle.getValor() <= 0) {
                throw new IllegalArgumentException(
                        "Los valores deben ser mayores que cero.");
            }

            MotivoNomina motivo =
                    mapaMotivos.get(detalle.getIdMotivo());

            if (motivo == null ||
                    !"ACTIVO".equalsIgnoreCase(motivo.getEstado())) {

                throw new IllegalArgumentException(
                        "El concepto seleccionado no existe o está inactivo.");
            }

            if ("INGRESO".equalsIgnoreCase(motivo.getTipo())) {

                ingresos += detalle.getValor();

            } else if ("EGRESO".equalsIgnoreCase(motivo.getTipo())) {

                egresos += detalle.getValor();

            } else {

                throw new IllegalArgumentException(
                        "Tipo de concepto no válido: " + motivo.getTipo());
            }
        }

        // Calcular totales
        nomina.setTotalIngresos(ingresos);
        nomina.setTotalEgresos(egresos);
        nomina.setSueldoNeto(ingresos - egresos);

        if (nomina.getEstado() == null ||
                nomina.getEstado().trim().isEmpty()) {

            nomina.setEstado("PENDIENTE");
        }

        // Guardar nómina y detalles en una sola transacción
        try (Connection con = ConexionOracle.conectar()) {

            con.setAutoCommit(false);

            try {

                // Insertar cabecera de nómina
                int idNomina = nominaDAO.insertar(con, nomina);

                // Insertar cada detalle
                for (DetalleNomina detalle : detalles) {

                    detalle.setIdNomina(idNomina);

                    detalleDAO.insertar(con, detalle);
                }

                // Confirmar transacción
                con.commit();

                return idNomina;

            } catch (SQLException | RuntimeException e) {

                con.rollback();

                throw e;
            }
        }
    }

    // REGISTRAR NOMINA SIN DETALLES
    public int registrarNomina(Nomina nomina)
            throws SQLException {

        validarNomina(nomina);

        nomina.setSueldoNeto(
                nomina.getTotalIngresos()
                - nomina.getTotalEgresos());

        if (nomina.getEstado() == null ||
                nomina.getEstado().trim().isEmpty()) {

            nomina.setEstado("PENDIENTE");
        }

        return nominaDAO.insertar(nomina);
    }

    // CONSULTAR TODAS LAS NOMINAS
    public List<Nomina> listarNominas()
            throws SQLException {

        return nominaDAO.listar();
    }

    // VALIDACIONES
    private void validarNomina(Nomina nomina) {

        if (nomina == null) {
            throw new IllegalArgumentException(
                    "La nómina no puede estar vacía.");
        }

        if (nomina.getIdEmpleado() <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un empleado.");
        }

        if (nomina.getFechaInicio() == null ||
                nomina.getFechaFin() == null) {

            throw new IllegalArgumentException(
                    "Debe ingresar las fechas de la nómina.");
        }

        if (nomina.getFechaInicio().after(nomina.getFechaFin())) {

            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la final.");
        }
    }
}
