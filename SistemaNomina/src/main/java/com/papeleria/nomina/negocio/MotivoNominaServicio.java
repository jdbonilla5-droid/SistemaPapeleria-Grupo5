
package com.papeleria.nomina.negocio;

import com.papeleria.nomina.persistencia.MotivoNominaDAO;
import java.sql.SQLException;
import java.util.List;

public class MotivoNominaServicio {

    private final MotivoNominaDAO motivoDAO;

    public MotivoNominaServicio() {
        this.motivoDAO = new MotivoNominaDAO();
    }

    // 1. LISTAR TODOS LOS MOTIVOS
    public List<MotivoNomina> listarTodos()
            throws SQLException {

        return motivoDAO.listarTodos();
    }

    // 2. LISTAR MOTIVOS DE INGRESO
    public List<MotivoNomina> listarIngresos()
            throws SQLException {

        return motivoDAO.listarPorTipo("INGRESO");
    }

    // 3. LISTAR MOTIVOS DE EGRESO
    public List<MotivoNomina> listarEgresos()
            throws SQLException {

        return motivoDAO.listarPorTipo("EGRESO");
    }

    // 4. REGISTRAR UN NUEVO MOTIVO
    public boolean registrar(MotivoNomina motivo)
            throws SQLException {

        if (motivo == null) {
            throw new IllegalArgumentException(
                    "Debe ingresar los datos del motivo.");
        }

        if (motivo.getCodigo() == null
                || motivo.getCodigo().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El codigo del motivo es obligatorio.");
        }

        if (motivo.getNombre() == null
                || motivo.getNombre().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nombre del motivo es obligatorio.");
        }

        if (!"INGRESO".equalsIgnoreCase(motivo.getTipo())
                && !"EGRESO".equalsIgnoreCase(motivo.getTipo())) {

            throw new IllegalArgumentException(
                    "El tipo debe ser INGRESO o EGRESO.");
        }

        if (!"ACTIVO".equalsIgnoreCase(motivo.getEstado())
                && !"INACTIVO".equalsIgnoreCase(motivo.getEstado())) {

            throw new IllegalArgumentException(
                    "El estado debe ser ACTIVO o INACTIVO.");
        }

        return motivoDAO.registrar(motivo);
    }
}
