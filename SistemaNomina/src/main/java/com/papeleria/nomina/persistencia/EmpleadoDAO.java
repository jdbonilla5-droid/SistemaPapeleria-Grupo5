
package com.papeleria.nomina.persistencia;

import com.papeleria.nomina.conexion.ConexionOracle;
import com.papeleria.nomina.negocio.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // 1. LISTAR TODOS LOS EMPLEADOS
    public List<Empleado> listar() throws SQLException {

        List<Empleado> empleados = new ArrayList<>();

        String sql = """
            SELECT ID_EMPLEADO, CEDULA, NOMBRES, APELLIDOS,
                   FECHA_INGRESO, CARGO, SUELDO_BASE, ESTADO
            FROM EMPLEADOS
            ORDER BY ID_EMPLEADO
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        }

        return empleados;
    }

    // 2. REGISTRAR UN EMPLEADO
    public void insertar(Empleado empleado) throws SQLException {

        String sql = """
            INSERT INTO EMPLEADOS
            (CEDULA, NOMBRES, APELLIDOS, FECHA_INGRESO,
             CARGO, SUELDO_BASE, ESTADO)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, empleado.getCedula());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());

            ps.setDate(4, new java.sql.Date(
                empleado.getFechaIngreso().getTime()
            ));

            ps.setString(5, empleado.getCargo());
            ps.setDouble(6, empleado.getSueldoBase());
            ps.setString(7, empleado.getEstado());

            ps.executeUpdate();
        }
    }

    // 3. MODIFICAR UN EMPLEADO
    public boolean actualizar(Empleado empleado) throws SQLException {

        String sql = """
            UPDATE EMPLEADOS
            SET CEDULA = ?,
                NOMBRES = ?,
                APELLIDOS = ?,
                FECHA_INGRESO = ?,
                CARGO = ?,
                SUELDO_BASE = ?,
                ESTADO = ?
            WHERE ID_EMPLEADO = ?
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, empleado.getCedula());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());

            ps.setDate(4, new java.sql.Date(
                empleado.getFechaIngreso().getTime()
            ));

            ps.setString(5, empleado.getCargo());
            ps.setDouble(6, empleado.getSueldoBase());
            ps.setString(7, empleado.getEstado());
            ps.setInt(8, empleado.getIdEmpleado());

            return ps.executeUpdate() > 0;
        }
    }

    // 4. ELIMINAR UN EMPLEADO
    public boolean eliminar(int idEmpleado) throws SQLException {

        String sql = """
            DELETE FROM EMPLEADOS
            WHERE ID_EMPLEADO = ?
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmpleado);

            return ps.executeUpdate() > 0;
        }
    }

    // 5. BUSCAR EMPLEADO POR ID
    public Empleado buscarPorId(int idEmpleado) throws SQLException {

        String sql = """
            SELECT ID_EMPLEADO, CEDULA, NOMBRES, APELLIDOS,
                   FECHA_INGRESO, CARGO, SUELDO_BASE, ESTADO
            FROM EMPLEADOS
            WHERE ID_EMPLEADO = ?
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEmpleado);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        }

        return null;
    }

    // 6. BUSCAR EMPLEADO POR CEDULA
    public Empleado buscarPorCedula(String cedula) throws SQLException {

        String sql = """
            SELECT ID_EMPLEADO, CEDULA, NOMBRES, APELLIDOS,
                   FECHA_INGRESO, CARGO, SUELDO_BASE, ESTADO
            FROM EMPLEADOS
            WHERE CEDULA = ?
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cedula);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }
        }

        return null;
    }

    // CONVERTIR RESULTADO ORACLE EN OBJETO EMPLEADO
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {

        return new Empleado(
            rs.getInt("ID_EMPLEADO"),
            rs.getString("CEDULA"),
            rs.getString("NOMBRES"),
            rs.getString("APELLIDOS"),
            rs.getDate("FECHA_INGRESO"),
            rs.getString("CARGO"),
            rs.getDouble("SUELDO_BASE"),
            rs.getString("ESTADO")
        );
    }
}
