
package com.papeleria.nomina.conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println(" SISTEMA DE NOMINA - PAPELERIA");
        System.out.println("================================");

        try (Connection conexion = ConexionOracle.conectar()) {

            System.out.println("CONEXION EXITOSA CON ORACLE");

            String sql = """
                SELECT USER AS USUARIO,
                       SYS_CONTEXT('USERENV', 'CON_NAME') AS SERVICIO
                FROM DUAL
                """;

            try (PreparedStatement ps =
                    conexion.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println(
                        "Usuario: " + rs.getString("USUARIO")
                    );

                    System.out.println(
                        "Servicio: " + rs.getString("SERVICIO")
                    );
                }
            }

            try (PreparedStatement ps =
                    conexion.prepareStatement(
                        "SELECT COUNT(*) FROM EMPLEADOS"
                    );
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println(
                        "Empleados registrados: " + rs.getInt(1)
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("ERROR DE CONEXION");
            System.out.println(e.getMessage());
        }
    }
}
