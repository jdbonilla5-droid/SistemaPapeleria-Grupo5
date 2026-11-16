
package com.papeleria.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionOracle {

    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";

    private static final String USUARIO = "NOMINA_APP";

    private static final String CLAVE =
            System.getenv("PAPELERIA_DB_PASSWORD");

    public static Connection conectar()
            throws SQLException {

        if (CLAVE == null || CLAVE.isBlank()) {
            throw new SQLException(
                    "No se encontró la variable "
                    + "PAPELERIA_DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(
                URL, USUARIO, CLAVE
        );
    }
}