
package com.papeleria.seguridad;

import com.papeleria.conexion.ConexionOracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class RegistrarAdministrador {

    // Los cuatro administradores del sistema
    private static final String[][] ADMINISTRADORES = {
        {"admin_nomina", "Administrador de Nómina"},
        {"admin_contabilidad", "Administrador de Contabilidad"},
        {"admin_facturacion", "Administrador de Facturación"},
        {"admin_seleccion", "Administrador de Selección"}
    };

    public static void main(String[] args) {

        try (Connection conexion = ConexionOracle.conectar()) {

            for (String[] administrador : ADMINISTRADORES) {

                String usuario = administrador[0];
                String nombreCompleto = administrador[1];

                // Verificar si el usuario ya existe
                if (existeUsuario(conexion, usuario)) {

                    JOptionPane.showMessageDialog(
                        null,
                        "El usuario " + usuario
                        + " ya se encuentra registrado."
                    );

                    continue;
                }

                // Solicitar contraseña
                char[] contrasena = solicitarContrasena(usuario);

                if (contrasena == null) {

                    JOptionPane.showMessageDialog(
                        null,
                        "Registro cancelado."
                    );

                    return;
                }

                try {

                    // Proteger contraseña mediante hash
                    String hash = SeguridadClave.generarHash(
                        contrasena
                    );

                    // Registrar administrador en Oracle
                    String sql = """
                        INSERT INTO USUARIOS_SISTEMA
                        (
                            USUARIO,
                            NOMBRE_COMPLETO,
                            CLAVE_HASH,
                            ROL,
                            ESTADO
                        )
                        VALUES (?, ?, ?, ?, ?)
                        """;

                    try (PreparedStatement ps =
                            conexion.prepareStatement(sql)) {

                        ps.setString(1, usuario);
                        ps.setString(2, nombreCompleto);
                        ps.setString(3, hash);
                        ps.setString(4, "ADMINISTRADOR");
                        ps.setString(5, "ACTIVO");

                        int resultado = ps.executeUpdate();

                        if (resultado > 0) {

                            JOptionPane.showMessageDialog(
                                null,
                                "Administrador registrado correctamente:\n"
                                + usuario + "\n"
                                + nombreCompleto,
                                "Registro exitoso",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }

                } finally {

                    // Limpiar la contraseña de memoria
                    Arrays.fill(contrasena, '\0');
                }
            }

            JOptionPane.showMessageDialog(
                null,
                "Proceso de registro finalizado.\n"
                + "Verifica los administradores en Oracle.",
                "Sistema de Papelería",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                null,
                "Error al conectar o registrar en Oracle:\n"
                + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // Verificar si un usuario ya está registrado
    private static boolean existeUsuario(
            Connection conexion,
            String usuario) throws SQLException {

        String sql = """
            SELECT COUNT(*)
            FROM USUARIOS_SISTEMA
            WHERE USUARIO = ?
            """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    // Solicitar y confirmar la contraseña
    private static char[] solicitarContrasena(String usuario) {

        while (true) {

            JPasswordField campoContrasena =
                    new JPasswordField(20);

            int opcion = JOptionPane.showConfirmDialog(
                null,
                campoContrasena,
                "Ingrese contraseña para: " + usuario,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (opcion != JOptionPane.OK_OPTION) {
                return null;
            }

            char[] contrasena =
                    campoContrasena.getPassword();

            // Validar longitud
            if (contrasena.length < 12) {

                Arrays.fill(contrasena, '\0');

                JOptionPane.showMessageDialog(
                    null,
                    "La contraseña debe tener "
                    + "mínimo 12 caracteres.",
                    "Contraseña inválida",
                    JOptionPane.WARNING_MESSAGE
                );

                continue;
            }

            // Confirmar contraseña
            JPasswordField campoConfirmacion =
                    new JPasswordField(20);

            int confirmar = JOptionPane.showConfirmDialog(
                null,
                campoConfirmacion,
                "Confirme contraseña de: " + usuario,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (confirmar != JOptionPane.OK_OPTION) {

                Arrays.fill(contrasena, '\0');
                return null;
            }

            char[] confirmacion =
                    campoConfirmacion.getPassword();

            boolean coinciden =
                    Arrays.equals(contrasena, confirmacion);

            Arrays.fill(confirmacion, '\0');

            if (!coinciden) {

                Arrays.fill(contrasena, '\0');

                JOptionPane.showMessageDialog(
                    null,
                    "Las contraseñas no coinciden.\n"
                    + "Inténtelo nuevamente.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

                continue;
            }

            return contrasena;
        }
    }
}
