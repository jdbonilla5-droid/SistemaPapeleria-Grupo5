
package com.mycompany.proyecto_seleccion_servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    // Conexión a Oracle
    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";

    // Usuario de la base de datos
    private static final String USER = "SELECCION_APP";

    // Contraseña directa para pruebas locales
    private static final String PASSWORD = "Se2026";

    public static Connection getConexion() throws SQLException {

        System.out.println(
                "[BASE DE DATOS] Iniciando conexión Oracle..."
        );

        try {
            // Cargar controlador JDBC
            Class.forName("oracle.jdbc.OracleDriver");

            // Establecer conexión
            Connection conexion = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println(
                    "[BASE DE DATOS] Conexión exitosa a Oracle."
            );

            return conexion;

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "No se encontró el controlador JDBC de Oracle.",
                    e
            );

        } catch (SQLException e) {

            System.err.println(
                    "[ERROR ORACLE] Código: " + e.getErrorCode()
            );

            System.err.println(
                    "[ERROR ORACLE] " + e.getMessage()
            );

            throw e;
        }
    }
}
