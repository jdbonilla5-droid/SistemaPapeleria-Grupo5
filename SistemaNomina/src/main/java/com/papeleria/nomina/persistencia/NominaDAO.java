
package com.papeleria.nomina.persistencia;

import com.papeleria.nomina.conexion.ConexionOracle;
import com.papeleria.nomina.negocio.Nomina;
import com.papeleria.nomina.negocio.DetalleNomina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NominaDAO {

    // REGISTRAR NOMINA CON DETALLES EN UNA SOLA TRANSACCION
    public int registrarNominaConDetalles(
            Nomina nomina, List<DetalleNomina> detalles)
            throws SQLException {

        if (nomina == null) {
            throw new IllegalArgumentException(
                    "La nómina no puede estar vacía.");
        }

        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe agregar al menos un detalle a la nómina.");
        }

        try (Connection con = ConexionOracle.conectar()) {

            con.setAutoCommit(false);

            try {

                double totalIngresos = 0;
                double totalEgresos = 0;

                // CALCULAR INGRESOS Y EGRESOS
                for (DetalleNomina detalle : detalles) {

                    if (detalle == null || detalle.getValor() <= 0) {
                        throw new IllegalArgumentException(
                                "Todos los detalles deben tener un valor mayor a cero.");
                    }

                    String sqlTipo =
                            "SELECT TIPO FROM MOTIVOS_NOMINA "
                            + "WHERE ID_MOTIVO = ? "
                            + "AND UPPER(ESTADO) = 'ACTIVO'";

                    try (PreparedStatement ps =
                            con.prepareStatement(sqlTipo)) {

                        ps.setInt(1, detalle.getIdMotivo());

                        try (ResultSet rs = ps.executeQuery()) {

                            if (!rs.next()) {
                                throw new SQLException(
                                        "No existe un motivo activo con ID: "
                                        + detalle.getIdMotivo());
                            }

                            String tipo = rs.getString("TIPO");

                            if ("INGRESO".equalsIgnoreCase(tipo)) {
                                totalIngresos += detalle.getValor();

                            } else if ("EGRESO".equalsIgnoreCase(tipo)) {
                                totalEgresos += detalle.getValor();

                            } else {
                                throw new SQLException(
                                        "Tipo de motivo no válido: " + tipo);
                            }
                        }
                    }
                }

                // ACTUALIZAR TOTALES
                nomina.setTotalIngresos(totalIngresos);
                nomina.setTotalEgresos(totalEgresos);
                nomina.setSueldoNeto(
                        totalIngresos - totalEgresos);

                if (nomina.getEstado() == null
                        || nomina.getEstado().trim().isEmpty()) {
                    nomina.setEstado("PENDIENTE");
                }

                // INSERTAR CABECERA DE NOMINA
                int idNomina = insertar(con, nomina);

                // INSERTAR DETALLES
                String sqlDetalle =
                        "INSERT INTO DETALLES_NOMINA "
                        + "(ID_NOMINA, ID_MOTIVO, VALOR, OBSERVACION) "
                        + "VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps =
                        con.prepareStatement(sqlDetalle)) {

                    for (DetalleNomina detalle : detalles) {

                        ps.setInt(1, idNomina);
                        ps.setInt(2, detalle.getIdMotivo());
                        ps.setDouble(3, detalle.getValor());

                        if (detalle.getObservacion() != null) {
                            ps.setString(
                                    4, detalle.getObservacion());
                        } else {
                            ps.setNull(4, Types.VARCHAR);
                        }

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                // CONFIRMAR TRANSACCION
                con.commit();

                return idNomina;

            } catch (SQLException | RuntimeException e) {

                con.rollback();

                throw e;
            }
        }
    }

    // INSERTAR NOMINA CON CONEXION EXISTENTE
    public int insertar(Connection con, Nomina nomina)
            throws SQLException {

        String sql = "INSERT INTO NOMINAS "
                + "(ID_EMPLEADO, FECHA_INICIO, FECHA_FIN, "
                + "FECHA_PAGO, TOTAL_INGRESOS, TOTAL_EGRESOS, "
                + "SUELDO_NETO, ESTADO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(
                sql, new String[]{"ID_NOMINA"})) {

            ps.setInt(1, nomina.getIdEmpleado());

            ps.setDate(2, new java.sql.Date(
                    nomina.getFechaInicio().getTime()));

            ps.setDate(3, new java.sql.Date(
                    nomina.getFechaFin().getTime()));

            if (nomina.getFechaPago() != null) {
                ps.setDate(4, new java.sql.Date(
                        nomina.getFechaPago().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }

            ps.setDouble(5, nomina.getTotalIngresos());
            ps.setDouble(6, nomina.getTotalEgresos());
            ps.setDouble(7, nomina.getSueldoNeto());
            ps.setString(8, nomina.getEstado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException(
                "No se pudo obtener el identificador de la nómina.");
    }

    // INSERTAR NOMINA SIN DETALLES
    public int insertar(Nomina nomina)
            throws SQLException {

        try (Connection con = ConexionOracle.conectar()) {

            con.setAutoCommit(false);

            try {

                int id = insertar(con, nomina);

                con.commit();

                return id;

            } catch (SQLException | RuntimeException e) {

                con.rollback();

                throw e;
            }
        }
    }

    // LISTAR TODAS LAS NOMINAS
    public List<Nomina> listar()
            throws SQLException {

        List<Nomina> lista = new ArrayList<>();

        String sql = "SELECT ID_NOMINA, ID_EMPLEADO, "
                + "FECHA_INICIO, FECHA_FIN, FECHA_PAGO, "
                + "TOTAL_INGRESOS, TOTAL_EGRESOS, "
                + "SUELDO_NETO, ESTADO "
                + "FROM NOMINAS ORDER BY ID_NOMINA";

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Nomina n = new Nomina();

                n.setIdNomina(rs.getInt("ID_NOMINA"));
                n.setIdEmpleado(rs.getInt("ID_EMPLEADO"));
                n.setFechaInicio(rs.getDate("FECHA_INICIO"));
                n.setFechaFin(rs.getDate("FECHA_FIN"));
                n.setFechaPago(rs.getDate("FECHA_PAGO"));

                n.setTotalIngresos(
                        rs.getDouble("TOTAL_INGRESOS"));

                n.setTotalEgresos(
                        rs.getDouble("TOTAL_EGRESOS"));

                n.setSueldoNeto(
                        rs.getDouble("SUELDO_NETO"));

                n.setEstado(rs.getString("ESTADO"));

                lista.add(n);
            }
        }

        return lista;
    }
}
