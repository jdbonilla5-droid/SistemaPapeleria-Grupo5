
package servidorjava;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexionOracle {

    private static final String URL =
            "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";

    private static final String USUARIO = "PROYECTO_JAVA";
    private static final String CLAVE = "Java12345";

    public static Connection conectar() throws SQLException {

        try {
            Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                "No se encuentra el controlador JDBC de Oracle. "
                + "Revisa que ojdbc11.jar este en Libraries.", e
            );
        }

        return DriverManager.getConnection(
                URL, USUARIO, CLAVE
        );
    }

    public static void guardarMensaje(
            String cliente, String mensaje) throws SQLException {

        String sql =
                "INSERT INTO MENSAJES (CLIENTE, MENSAJE) "
                + "VALUES (?, ?)";

        try (
            Connection conexion = conectar();

            PreparedStatement ps =
                    conexion.prepareStatement(sql)
        ) {

            ps.setString(1, cliente);
            ps.setString(2, mensaje);

            ps.executeUpdate();
        }
    }
}