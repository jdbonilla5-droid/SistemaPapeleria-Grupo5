package com.mycompany.clientefacturacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {

private static final String URL = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";
private static final String USUARIO = "jennifer";
private static final String CLAVE = "Papeleria2026";

    public static Connection conectar() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");

            return DriverManager.getConnection(
                    URL, USUARIO, CLAVE
            );

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "No se encontró el driver de Oracle."
            );

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error de conexión a Oracle:\n" + e.getMessage()
            );
        }

        return null;
    }
}