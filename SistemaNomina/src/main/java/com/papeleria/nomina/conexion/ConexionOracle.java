
package com.papeleria.nomina.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionOracle {

    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";

    private static final String USUARIO = "NOMINA_APP";

    private static final String CONTRASENA =
            System.getenv("NOMINA_DB_PASSWORD");

    public static Connection conectar() throws SQLException {

        if (CONTRASENA == null || CONTRASENA.isBlank()) {
            throw new SQLException(
                "Falta configurar NOMINA_DB_PASSWORD"
            );
        }

        return DriverManager.getConnection(
                URL, USUARIO, CONTRASENA
        );
    }
}
