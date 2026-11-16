
package com.papeleria.nomina.persistencia;

import com.papeleria.nomina.conexion.ConexionOracle;
import java.math.BigDecimal;
import java.sql.*;

public class IntegracionContabilidadDAO {

    public int generarAsiento(int idNomina) throws SQLException {

        try (Connection con = ConexionOracle.conectar()) {

            if (con == null) {
                throw new SQLException("No se pudo conectar a Oracle.");
            }

            boolean autoCommitAnterior = con.getAutoCommit();

            try {
                con.setAutoCommit(false);

                BigDecimal neto;

                String sqlNomina =
                    "SELECT SUELDO_NETO FROM NOMINAS "
                    + "WHERE ID_NOMINA = ? FOR UPDATE";

                try (PreparedStatement ps =
                        con.prepareStatement(sqlNomina)) {

                    ps.setInt(1, idNomina);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("La nómina no existe.");
                        }

                        neto = rs.getBigDecimal("SUELDO_NETO");
                    }
                }

                if (neto == null || neto.signum() <= 0) {
                    throw new SQLException(
                        "La nómina debe tener un sueldo neto mayor a cero."
                    );
                }

                String referencia = "NOMINA-ID-" + idNomina;

                String sqlExiste =
                    "SELECT COUNT(*) FROM "
                    + "CONTABILIDAD_APP.COMPROBANTE_CONTABLE "
                    + "WHERE OBSERVACIONES = ?";

                try (PreparedStatement ps =
                        con.prepareStatement(sqlExiste)) {

                    ps.setString(1, referencia);

                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();

                        if (rs.getInt(1) > 0) {
                            throw new SQLException(
                                "Esta nómina ya tiene un asiento contable."
                            );
                        }
                    }
                }

                int idComprobante;

                String sqlCabecera =
                    "INSERT INTO CONTABILIDAD_APP.COMPROBANTE_CONTABLE "
                    + "(FECHA, OBSERVACIONES, ESTADO) "
                    + "VALUES (SYSDATE, ?, 'BORRADOR') "
                    + "RETURNING ID_COMPROBANTE INTO ?";

                try (CallableStatement cs = con.prepareCall(
                        "BEGIN " + sqlCabecera + "; END;")) {

                    cs.setString(1, referencia);
                    cs.registerOutParameter(2, Types.NUMERIC);
                    cs.execute();

                    idComprobante = cs.getInt(2);
                }

                // Gastos de nómina: DEBE
                insertarDetalle(
                    con,
                    idComprobante,
                    21,
                    neto,
                    BigDecimal.ZERO
                );

                // Bancos: HABER
                insertarDetalle(
                    con,
                    idComprobante,
                    2,
                    BigDecimal.ZERO,
                    neto
                );

                con.commit();

                return idComprobante;

            } catch (SQLException | RuntimeException e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;

            } finally {
                con.setAutoCommit(autoCommitAnterior);
            }
        }
    }

    private void insertarDetalle(
            Connection con,
            int idComprobante,
            int idCuenta,
            BigDecimal debe,
            BigDecimal haber) throws SQLException {

        String sql =
            "INSERT INTO CONTABILIDAD_APP.DETALLE_CONTABLE "
            + "(ID_COMPROBANTE, ID_CUENTA, DEBE, HABER) "
            + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps =
                con.prepareStatement(sql)) {

            ps.setInt(1, idComprobante);
            ps.setInt(2, idCuenta);
            ps.setBigDecimal(3, debe);
            ps.setBigDecimal(4, haber);

            ps.executeUpdate();
        }
    }
}
