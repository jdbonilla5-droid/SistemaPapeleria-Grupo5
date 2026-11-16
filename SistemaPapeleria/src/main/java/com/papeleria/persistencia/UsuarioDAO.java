package com.papeleria.persistencia;

import com.papeleria.conexion.ConexionOracle;
import com.papeleria.seguridad.SeguridadClave;
import java.sql.*;
import java.util.Arrays;

public class UsuarioDAO {
    public boolean validarLogin(String usuario, char[] contrasena) throws SQLException {
        String sql = "SELECT CLAVE_HASH FROM USUARIOS_SISTEMA WHERE USUARIO = ? AND ROL = 'ADMINISTRADOR' AND ESTADO = 'ACTIVO'";
        try (Connection c = ConexionOracle.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuario.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && SeguridadClave.verificar(contrasena, rs.getString(1));
            }
        }
    }

    public boolean existeUsuario(String usuario) throws SQLException {
        String sql = "SELECT 1 FROM USUARIOS_SISTEMA WHERE UPPER(USUARIO) = UPPER(?)";
        try (Connection c = ConexionOracle.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuario.trim());
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public void registrarUsuario(String usuario, String nombreCompleto, char[] clave,
                                String rol, String estado) throws SQLException {
        if (usuario == null || !usuario.trim().matches("[A-Za-z0-9_.-]{3,40}"))
            throw new IllegalArgumentException("Usuario: 3 a 40 caracteres, letras, números, punto, guion o guion bajo.");
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty())
            throw new IllegalArgumentException("Ingresa el nombre completo.");
        if (clave == null || clave.length < 8)
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        if (!"ADMINISTRADOR".equals(rol))
            throw new IllegalArgumentException("El login actual solo admite el rol ADMINISTRADOR.");
        if (!"ACTIVO".equals(estado) && !"INACTIVO".equals(estado))
            throw new IllegalArgumentException("Estado no válido.");
        String sql = "INSERT INTO USUARIOS_SISTEMA (USUARIO, NOMBRE_COMPLETO, CLAVE_HASH, ROL, ESTADO) VALUES (?, ?, ?, ?, ?)";
        String hash = SeguridadClave.generarHash(clave);
        try (Connection c = ConexionOracle.conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuario.trim());
            ps.setString(2, nombreCompleto.trim());
            ps.setString(3, hash);
            ps.setString(4, rol);
            ps.setString(5, estado);
            ps.executeUpdate();
        }
    }
}
