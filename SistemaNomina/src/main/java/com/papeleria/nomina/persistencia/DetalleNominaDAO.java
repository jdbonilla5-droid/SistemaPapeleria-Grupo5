
package com.papeleria.nomina.persistencia;

import com.papeleria.nomina.conexion.ConexionOracle;
import com.papeleria.nomina.negocio.DetalleNomina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleNominaDAO {

    // Insertar detalle dentro de la misma transacción
    public void insertar(Connection con, DetalleNomina detalle)
            throws SQLException {

        String sql = """
            INSERT INTO DETALLES_NOMINA
            (ID_NOMINA, ID_MOTIVO, VALOR, OBSERVACION)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdNomina());
            ps.setInt(2, detalle.getIdMotivo());
            ps.setDouble(3, detalle.getValor());
            ps.setString(4, detalle.getObservacion());

            ps.executeUpdate();
        }
    }

    // Consultar detalles de una nómina
    public List<DetalleNomina> listarPorNomina(int idNomina)
            throws SQLException {

        List<DetalleNomina> lista = new ArrayList<>();

        String sql = """
            SELECT ID_DETALLE, ID_NOMINA, ID_MOTIVO,
                   VALOR, OBSERVACION
            FROM DETALLES_NOMINA
            WHERE ID_NOMINA = ?
            ORDER BY ID_DETALLE
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idNomina);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    DetalleNomina d = new DetalleNomina();

                    d.setIdDetalle(rs.getInt("ID_DETALLE"));
                    d.setIdNomina(rs.getInt("ID_NOMINA"));
                    d.setIdMotivo(rs.getInt("ID_MOTIVO"));
                    d.setValor(rs.getDouble("VALOR"));
                    d.setObservacion(rs.getString("OBSERVACION"));

                    lista.add(d);
                }
            }
        }

        return lista;
    }
}
