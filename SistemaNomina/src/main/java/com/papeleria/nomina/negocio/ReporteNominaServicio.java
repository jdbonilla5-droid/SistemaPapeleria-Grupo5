
package com.papeleria.nomina.negocio;

import com.papeleria.nomina.persistencia.ReporteNominaDAO;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class ReporteNominaServicio {

    private final ReporteNominaDAO reporteDAO;

    public ReporteNominaServicio() {
        this.reporteDAO = new ReporteNominaDAO();
    }

    // 1. CONSULTAR REPORTE GENERAL DE NOMINA
    public DefaultTableModel consultarReporteNomina()
            throws SQLException {

        return reporteDAO.consultarReporteNomina();
    }

    // 2. CONSULTAR REPORTE DE MOTIVOS
    public DefaultTableModel consultarReporteMotivos()
            throws SQLException {

        return reporteDAO.consultarReporteMotivos();
    }
}
