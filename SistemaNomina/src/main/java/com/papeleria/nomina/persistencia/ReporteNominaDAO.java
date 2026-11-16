
package com.papeleria.nomina.persistencia;

import com.papeleria.nomina.conexion.ConexionOracle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class ReporteNominaDAO {

    public DefaultTableModel consultarReporteNomina()
            throws SQLException {

        String sql = "SELECT * FROM VW_REPORTE_NOMINA";

        return ejecutarConsulta(sql);
    }

    public DefaultTableModel consultarReporteMotivos()
            throws SQLException {

        String sql = "SELECT * FROM VW_REPORTE_MOTIVOS";

        return ejecutarConsulta(sql);
    }

    private DefaultTableModel ejecutarConsulta(String sql)
            throws SQLException {

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnas = meta.getColumnCount();

            DefaultTableModel modelo = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(
                        int fila, int columna) {
                    return false;
                }
            };

            for (int i = 1; i <= columnas; i++) {
                modelo.addColumn(meta.getColumnLabel(i));
            }

            while (rs.next()) {

                Object[] fila = new Object[columnas];

                for (int i = 1; i <= columnas; i++) {
                    fila[i - 1] = rs.getObject(i);
                }

                modelo.addRow(fila);
            }

            return modelo;
        }
    }
}
