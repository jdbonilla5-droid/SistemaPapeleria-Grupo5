
package com.mycompany.clientefacturacion;

import java.math.BigDecimal;
import java.sql.*;

public class IntegracionFacturacionContabilidadDAO {

    public int generarAsiento(String numeroFactura)
            throws SQLException {

        try (Connection con = Conexion.conectar()) {

            if (con == null) {
                throw new SQLException(
                    "No se pudo conectar a Oracle."
                );
            }

            boolean autoCommitAnterior = con.getAutoCommit();

            try {
                con.setAutoCommit(false);

                BigDecimal total;

                String sqlFactura =
                    "SELECT TOTAL FROM FACTURA_CAB "
                    + "WHERE NUMERO = ? FOR UPDATE";

                try (PreparedStatement ps =
                        con.prepareStatement(sqlFactura)) {

                    ps.setString(1, numeroFactura);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                "La factura no existe."
                            );
                        }

                        total = rs.getBigDecimal("TOTAL");
                    }
                }

                if (total == null || total.signum() <= 0) {
                    throw new SQLException(
                        "El total de la factura debe ser mayor a cero."
                    );
                }

                String referencia =
                    "FACTURA-" + numeroFactura;

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
                                "Esta factura ya tiene un asiento contable."
                            );
                        }
                    }
                }

                int idComprobante;

                String sqlCabecera =
                    "INSERT INTO "
                    + "CONTABILIDAD_APP.COMPROBANTE_CONTABLE "
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

                // Caja: DEBE
                insertarDetalle(
                    con, idComprobante, 1,
                    total, BigDecimal.ZERO
                );

                // Ventas de papelería: HABER
                insertarDetalle(
                    con, idComprobante, 6,
                    BigDecimal.ZERO, total
                );

                con.commit();

                return idComprobante;

            } catch (SQLException | RuntimeException e) {

                try {
                    con.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
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
