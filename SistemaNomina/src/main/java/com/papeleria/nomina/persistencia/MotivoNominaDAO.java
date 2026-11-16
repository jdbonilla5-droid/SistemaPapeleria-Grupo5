
package com.papeleria.nomina.persistencia;

import com.papeleria.nomina.conexion.ConexionOracle;
import com.papeleria.nomina.negocio.MotivoNomina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MotivoNominaDAO {

    // 1. LISTAR TODOS LOS MOTIVOS
    public List<MotivoNomina> listarTodos()
            throws SQLException {

        List<MotivoNomina> lista = new ArrayList<>();

        String sql = """
            SELECT ID_MOTIVO, CODIGO, NOMBRE,
                   TIPO, DESCRIPCION, ESTADO
            FROM MOTIVOS_NOMINA
            ORDER BY ID_MOTIVO
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                MotivoNomina motivo = new MotivoNomina();

                motivo.setIdMotivo(rs.getInt("ID_MOTIVO"));
                motivo.setCodigo(rs.getString("CODIGO"));
                motivo.setNombre(rs.getString("NOMBRE"));
                motivo.setTipo(rs.getString("TIPO"));
                motivo.setDescripcion(
                        rs.getString("DESCRIPCION"));
                motivo.setEstado(rs.getString("ESTADO"));

                lista.add(motivo);
            }
        }

        return lista;
    }

    // 2. LISTAR MOTIVOS POR TIPO
    public List<MotivoNomina> listarPorTipo(String tipo)
            throws SQLException {

        List<MotivoNomina> lista = new ArrayList<>();

        String sql = """
            SELECT ID_MOTIVO, CODIGO, NOMBRE,
                   TIPO, DESCRIPCION, ESTADO
            FROM MOTIVOS_NOMINA
            WHERE UPPER(TIPO) = UPPER(?)
              AND UPPER(ESTADO) = 'ACTIVO'
            ORDER BY ID_MOTIVO
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    MotivoNomina motivo = new MotivoNomina();

                    motivo.setIdMotivo(
                            rs.getInt("ID_MOTIVO"));
                    motivo.setCodigo(
                            rs.getString("CODIGO"));
                    motivo.setNombre(
                            rs.getString("NOMBRE"));
                    motivo.setTipo(
                            rs.getString("TIPO"));
                    motivo.setDescripcion(
                            rs.getString("DESCRIPCION"));
                    motivo.setEstado(
                            rs.getString("ESTADO"));

                    lista.add(motivo);
                }
            }
        }

        return lista;
    }

    // 3. BUSCAR MOTIVO POR ID
    // UTILIZANDO UNA CONEXION EXISTENTE
    public MotivoNomina buscarPorId(
            Connection con, int idMotivo)
            throws SQLException {

        String sql = """
            SELECT ID_MOTIVO, CODIGO, NOMBRE,
                   TIPO, DESCRIPCION, ESTADO
            FROM MOTIVOS_NOMINA
            WHERE ID_MOTIVO = ?
            """;

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, idMotivo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    MotivoNomina motivo = new MotivoNomina();

                    motivo.setIdMotivo(
                            rs.getInt("ID_MOTIVO"));
                    motivo.setCodigo(
                            rs.getString("CODIGO"));
                    motivo.setNombre(
                            rs.getString("NOMBRE"));
                    motivo.setTipo(
                            rs.getString("TIPO"));
                    motivo.setDescripcion(
                            rs.getString("DESCRIPCION"));
                    motivo.setEstado(
                            rs.getString("ESTADO"));

                    return motivo;
                }
            }
        }

        return null;
    }

    // 4. REGISTRAR UN NUEVO MOTIVO DE NOMINA
    public boolean registrar(MotivoNomina motivo)
            throws SQLException {

        String sql = """
            INSERT INTO MOTIVOS_NOMINA
            (CODIGO, NOMBRE, TIPO, DESCRIPCION, ESTADO)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setString(1, motivo.getCodigo());
            ps.setString(2, motivo.getNombre());
            ps.setString(3, motivo.getTipo());
            ps.setString(4, motivo.getDescripcion());
            ps.setString(5, motivo.getEstado());

            return ps.executeUpdate() > 0;
        }
    }
}
