
package com.papeleria.conexion;

import java.sql.Connection;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) {

        try (Connection con =
                ConexionOracle.conectar()) {

            System.out.println(
                    "CONEXION EXITOSA CON ORACLE"
            );

            System.out.println(
                    "SISTEMA DE PAPELERIA CONECTADO"
            );

        } catch (SQLException e) {

            System.out.println(
                    "ERROR DE CONEXION: "
                    + e.getMessage()
            );
        }
    }
}