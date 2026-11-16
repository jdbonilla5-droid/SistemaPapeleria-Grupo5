
package com.mycompany.proyecto_seleccion_servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ManejadorCliente extends Thread {

    private final Socket socket;

    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (
            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            PrintWriter salida = new PrintWriter(
                socket.getOutputStream(),
                true,
                StandardCharsets.UTF_8
            )
        ) {

            System.out.println(
                "[SERVIDOR] Cliente conectado desde: "
                + socket.getRemoteSocketAddress()
            );

            String peticion;

            while ((peticion = entrada.readLine()) != null) {

                System.out.println(
                    "[CLIENTE DICE]: " + peticion
                );

                if (peticion.startsWith("VALIDAR_LOGIN")) {

                    validarUsuario(peticion, salida);

                } else if (peticion.equals("LISTAR_CANDIDATOS")) {

                    enviarCandidatos(salida);

                } else if (peticion.startsWith("REGISTRAR_CANDIDATO")) {

                    registrarCandidato(peticion, salida);

                } else if (peticion.equals("LISTAR_PARAMETROS")) {

                    enviarParametros(salida);

                } else if (peticion.startsWith("REGISTRAR_PARAMETRO")) {

                    registrarParametro(peticion, salida);

                } else if (peticion.startsWith("GUARDAR_EVALUACION")) {

                    guardarEvaluacionCompleja(peticion, salida);

                } else if (peticion.equals("LISTAR_ELEGIBLES")) {

                    listarElegibles(salida);

                } else if (peticion.startsWith("CONTRATAR_LOTE|")) {

                    contratarLote(peticion, salida);

                } else if (peticion.startsWith("CONTRATAR_MEJORES")) {

                    ejecutarContratacion(peticion, salida);

                } else if (peticion.startsWith("REPORTE_RANKING")) {

                    enviarReporteRanking(peticion, salida);

                } else if (peticion.startsWith("REPORTE_MATRIZ")) {

                    enviarReporteMatriz(peticion, salida);

                } else {

                    salida.println("COMANDO_DESCONOCIDO");
                }
            }

        } catch (IOException e) {

            System.out.println(
                "[Manejador] Cliente desconectado: "
                + e.getMessage()
            );

        } finally {

            try {
                socket.close();

            } catch (IOException e) {

                System.out.println(
                    "[ERROR] No se pudo cerrar el socket."
                );
            }
        }
    }

    // ==========================================
    // 1. VALIDAR USUARIO
    // ==========================================

    private void validarUsuario(
            String peticion,
            PrintWriter salida) {

        String[] partes = peticion.split("\\|");

        if (partes.length < 3) {

            salida.println(
                "LOGIN_ERROR|Datos incompletos"
            );
            return;
        }

        String user = partes[1];
        String pass = partes[2];

        String sql =
            "SELECT id_usuario FROM usuarios "
            + "WHERE username = ? AND password = ?";

        try (
            Connection cn = ConexionDB.getConexion();

            PreparedStatement ps =
                cn.prepareStatement(sql)
        ) {

            ps.setString(1, user);
            ps.setString(2, pass);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    salida.println(
                        "LOGIN_OK|ADMINISTRADOR"
                    );

                } else {

                    salida.println(
                        "LOGIN_ERROR|Usuario o "
                        + "contraseña incorrectos"
                    );
                }
            }

        } catch (SQLException e) {

            salida.println(
                "LOGIN_ERROR|Error en BD: "
                + mensaje(e)
            );
        }
    }

    // ==========================================
    // 2. LISTAR CANDIDATOS
    // ==========================================

    private void enviarCandidatos(PrintWriter salida) {

        String sql =
            "SELECT CEDULA, NOMBRE, "
            + "TO_CHAR(FECHA_NACIMIENTO, 'YYYY-MM-DD') AS FECHA "
            + "FROM CANDIDATOS ORDER BY NOMBRE";

        try (
            Connection cn = ConexionDB.getConexion();
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            int total = 0;
            while (rs.next()) {
                salida.println(
                    rs.getString("CEDULA") + "|"
                    + rs.getString("NOMBRE") + "|"
                    + rs.getString("FECHA")
                );
                total++;
            }
            System.out.println("[SERVIDOR] Candidatos enviados: " + total);
            salida.println("FIN_LISTA");

        } catch (SQLException e) {
            System.err.println("[ERROR LISTAR CANDIDATOS] " + e.getMessage());
            salida.println("LISTA_ERROR|" + mensaje(e));
        }
    }

    // ==========================================
    // 3. REGISTRAR CANDIDATO
    // ==========================================

    private void registrarCandidato(
            String peticion,
            PrintWriter salida) {

        String[] partes = peticion.split("\\|");

        if (partes.length < 4) {

            salida.println(
                "REGISTRO_ERROR|Datos incompletos"
            );
            return;
        }

        String sql =
            "INSERT INTO candidatos "
            + "(cedula, nombre, fecha_nacimiento) "
            + "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'))";

        try (
            Connection cn = ConexionDB.getConexion();

            PreparedStatement ps =
                cn.prepareStatement(sql)
        ) {

            ps.setString(1, partes[1]);
            ps.setString(2, partes[2]);
            ps.setString(3, partes[3]);

            ps.executeUpdate();

            salida.println("REGISTRO_OK");

        } catch (SQLException e) {

            salida.println(
                "REGISTRO_ERROR|" + mensaje(e)
            );
        }
    }

    // ==========================================
    // 4. LISTAR PARAMETROS
    // ==========================================

    private void enviarParametros(
            PrintWriter salida) {

        String sql =
            "SELECT codigo_parametro, "
            + "nombre_parametro, puntaje_maximo "
            + "FROM parametros_evaluacion";

        try (
            Connection cn = ConexionDB.getConexion();

            PreparedStatement ps =
                cn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                salida.println(
                    rs.getInt("codigo_parametro") + "|"
                    + rs.getString("nombre_parametro") + "|"
                    + rs.getDouble("puntaje_maximo")
                );
            }

            salida.println("FIN_LISTA");

        } catch (SQLException e) {

            System.out.println(
                "[SERVIDOR ERROR] Listar parámetros: "
                + e.getMessage()
            );

            salida.println("FIN_LISTA");
        }
    }

    // ==========================================
    // 5. REGISTRAR PARAMETRO
    // ==========================================

    private void registrarParametro(
            String peticion,
            PrintWriter salida) {

        String[] partes = peticion.split("\\|");

        if (partes.length < 3) {

            salida.println(
                "REGISTRO_ERROR|Datos incompletos"
            );
            return;
        }

        String sql =
            "INSERT INTO parametros_evaluacion "
            + "(nombre_parametro, puntaje_maximo) "
            + "VALUES (?, ?)";

        try (
            Connection cn = ConexionDB.getConexion();

            PreparedStatement ps =
                cn.prepareStatement(sql)
        ) {

            ps.setString(1, partes[1]);

            ps.setDouble(
                2,
                Double.parseDouble(partes[2])
            );

            ps.executeUpdate();

            salida.println("REGISTRO_OK");

        } catch (SQLException
                 | NumberFormatException e) {

            salida.println(
                "REGISTRO_ERROR|" + mensaje(e)
            );
        }
    }

    // ==========================================
    // 6. GUARDAR EVALUACION
    // ==========================================

    private void guardarEvaluacionCompleja(
            String peticion,
            PrintWriter salida) {

        String[] partes = peticion.split("\\|");

        if (partes.length < 3) {

            salida.println(
                "PROCESO_ERROR|Datos incompletos"
            );
            return;
        }

        String cedula = partes[1];
        String detallesRaw = partes[2];

        Connection cn = null;

        try {

            cn = ConexionDB.getConexion();

            cn.setAutoCommit(false);

            String sqlCabecera =
                "INSERT INTO cabeceras_evaluacion "
                + "(cedula_candidato) VALUES (?)";

            String[] columnasGeneradas = {
                "NUMERO_EVALUACION"
            };

            int idCabecera = -1;

            try (
                PreparedStatement psCab =
                    cn.prepareStatement(
                        sqlCabecera,
                        columnasGeneradas
                    )
            ) {

                psCab.setString(1, cedula);
                psCab.executeUpdate();

                try (
                    ResultSet rs =
                        psCab.getGeneratedKeys()
                ) {

                    if (rs.next()) {

                        idCabecera = rs.getInt(1);
                    }
                }
            }

            if (idCabecera == -1) {

                throw new SQLException(
                    "No se generó el ID de evaluación."
                );
            }

            String sqlDetalle =
                "INSERT INTO detalles_evaluacion "
                + "(numero_evaluacion, "
                + "codigo_parametro, calificacion) "
                + "VALUES (?, ?, ?)";

            try (
                PreparedStatement psDet =
                    cn.prepareStatement(sqlDetalle)
            ) {

                String[] registros =
                    detallesRaw.split(";");

                for (String registro : registros) {

                    String[] datos =
                        registro.split(",");

                    if (datos.length != 2) {

                        throw new SQLException(
                            "Detalle de evaluación inválido."
                        );
                    }

                    psDet.setInt(
                        1,
                        idCabecera
                    );

                    psDet.setInt(
                        2,
                        Integer.parseInt(datos[0])
                    );

                    psDet.setDouble(
                        3,
                        Double.parseDouble(datos[1])
                    );

                    psDet.executeUpdate();
                }
            }

            cn.commit();

            salida.println("PROCESO_OK");

        } catch (SQLException
                 | NumberFormatException e) {

            if (cn != null) {

                try {
                    cn.rollback();

                } catch (SQLException ex) {

                    System.out.println(
                        "[ERROR ROLLBACK] "
                        + ex.getMessage()
                    );
                }
            }

            salida.println(
                "PROCESO_ERROR|" + mensaje(e)
            );

        } finally {

            if (cn != null) {

                try {
                    cn.close();

                } catch (SQLException e) {

                    System.out.println(
                        "[ERROR CERRAR CONEXION] "
                        + e.getMessage()
                    );
                }
            }
        }
    }

    // ==========================================
    // 7. LISTAR CANDIDATOS ELEGIBLES
    // ==========================================

    private void listarElegibles(
            PrintWriter salida) {

        String sql =
            "SELECT c.CEDULA, c.NOMBRE, "
            + "SUM(de.CALIFICACION) AS PUNTAJE "
            + "FROM CANDIDATOS c "
            + "JOIN CABECERAS_EVALUACION ce "
            + "ON ce.CEDULA_CANDIDATO = c.CEDULA "
            + "JOIN DETALLES_EVALUACION de "
            + "ON de.NUMERO_EVALUACION = "
            + "ce.NUMERO_EVALUACION "
            + "WHERE NOT EXISTS ("
            + "SELECT 1 FROM EMPLEADOS e "
            + "WHERE e.CEDULA = c.CEDULA) "
            + "AND NOT EXISTS ("
            + "SELECT 1 FROM NOMINA_APP.EMPLEADOS n "
            + "WHERE n.CEDULA = c.CEDULA) "
            + "GROUP BY c.CEDULA, c.NOMBRE "
            + "ORDER BY PUNTAJE DESC, c.CEDULA";

        try (
            Connection cn = ConexionDB.getConexion();

            PreparedStatement ps =
                cn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                String nombre =
                    rs.getString("NOMBRE");

                String nombreCodificado =
                    Base64.getEncoder().encodeToString(
                        nombre.getBytes(
                            StandardCharsets.UTF_8
                        )
                    );

                salida.println(
                    "CANDIDATO|"
                    + rs.getString("CEDULA")
                    + "|" + nombreCodificado
                    + "|" + rs.getDouble("PUNTAJE")
                );
            }

            salida.println("FIN_LISTA");

        } catch (SQLException e) {

            System.out.println(
                "[SERVIDOR ERROR] Elegibles: "
                + e.getMessage()
            );

            salida.println(
                "LISTA_ERROR|" + mensaje(e)
            );
        }
    }

    // ==========================================
    // 8. CONTRATACION INDIVIDUAL POR LOTES
    // ==========================================

    private void contratarLote(
            String peticion,
            PrintWriter salida) {

        String[] partes =
            peticion.split("\\|", -1);

        if (partes.length < 2) {

            salida.println(
                "CONTRATAR_ERROR|Solicitud vacía"
            );
            return;
        }

        int cantidad;

        try {

            cantidad = Integer.parseInt(partes[1]);

        } catch (NumberFormatException e) {

            salida.println(
                "CONTRATAR_ERROR|Cantidad inválida"
            );
            return;
        }

        if (cantidad < 1
                || partes.length != 2 + cantidad * 5) {

            salida.println(
                "CONTRATAR_ERROR|Datos incompletos"
            );
            return;
        }

        String[] cedulas =
            new String[cantidad];

        String[] nombres =
            new String[cantidad];

        String[] apellidos =
            new String[cantidad];

        String[] cargos =
            new String[cantidad];

        BigDecimal[] sueldos =
            new BigDecimal[cantidad];

        Set<String> cedulasUnicas =
            new HashSet<>();

        Set<String> cargosUnicos =
            new HashSet<>();

        try {

            for (int i = 0; i < cantidad; i++) {

                int base = 2 + i * 5;

                cedulas[i] =
                    partes[base].trim();

                nombres[i] =
                    decodificar(
                        partes[base + 1]
                    ).trim();

                apellidos[i] =
                    decodificar(
                        partes[base + 2]
                    ).trim();

                cargos[i] =
                    decodificar(
                        partes[base + 3]
                    ).trim();

                sueldos[i] =
                    new BigDecimal(
                        partes[base + 4]
                    );

                if (!cedulas[i].matches("[0-9]{10,13}")
                        || nombres[i].isEmpty()
                        || apellidos[i].isEmpty()
                        || cargos[i].isEmpty()
                        || sueldos[i].signum() <= 0
                        || !cedulasUnicas.add(
                            cedulas[i]
                        )
                        || !cargosUnicos.add(
                            cargos[i].toUpperCase(
                                Locale.ROOT
                            )
                        )) {

                    throw new IllegalArgumentException(
                        "Datos incorrectos, cédulas "
                        + "repetidas o cargos repetidos."
                    );
                }
            }

        } catch (IllegalArgumentException e) {

            salida.println(
                "CONTRATAR_ERROR|" + mensaje(e)
            );
            return;
        }

        String sqlRanking =
            "SELECT c.CEDULA "
            + "FROM CANDIDATOS c "
            + "JOIN CABECERAS_EVALUACION ce "
            + "ON ce.CEDULA_CANDIDATO = c.CEDULA "
            + "JOIN DETALLES_EVALUACION de "
            + "ON de.NUMERO_EVALUACION = "
            + "ce.NUMERO_EVALUACION "
            + "WHERE NOT EXISTS ("
            + "SELECT 1 FROM EMPLEADOS e "
            + "WHERE e.CEDULA = c.CEDULA) "
            + "AND NOT EXISTS ("
            + "SELECT 1 FROM NOMINA_APP.EMPLEADOS n "
            + "WHERE n.CEDULA = c.CEDULA) "
            + "GROUP BY c.CEDULA "
            + "ORDER BY SUM(de.CALIFICACION) DESC, "
            + "c.CEDULA";

        String sqlSeleccion =
            "INSERT INTO EMPLEADOS "
            + "(CEDULA, NOMBRE, FECHA_INGRESO, SUELDO) "
            + "VALUES (?, ?, SYSDATE, ?)";

        String sqlNomina =
            "INSERT INTO NOMINA_APP.EMPLEADOS "
            + "(CEDULA, NOMBRES, APELLIDOS, "
            + "FECHA_INGRESO, CARGO, SUELDO_BASE, "
            + "ESTADO) "
            + "VALUES (?, ?, ?, SYSDATE, ?, ?, "
            + "'ACTIVO')";

        try (
            Connection cn = ConexionDB.getConexion()
        ) {

            cn.setAutoCommit(false);

            try {

                // Verificar ranking actualizado.
                try (
                    PreparedStatement psRanking =
                        cn.prepareStatement(sqlRanking);

                    ResultSet rs =
                        psRanking.executeQuery()
                ) {

                    for (int i = 0; i < cantidad; i++) {

                        if (!rs.next()
                                || !cedulas[i].equals(
                                    rs.getString(1)
                                )) {

                            throw new SQLException(
                                "Los candidatos ya no "
                                + "coinciden con el ranking. "
                                + "Actualice la lista."
                            );
                        }
                    }
                }

                // Insertar en Selección y Nómina.
                try (
                    PreparedStatement psSeleccion =
                        cn.prepareStatement(
                            sqlSeleccion
                        );

                    PreparedStatement psNomina =
                        cn.prepareStatement(
                            sqlNomina
                        )
                ) {

                    for (int i = 0; i < cantidad; i++) {

                        psSeleccion.setString(
                            1,
                            cedulas[i]
                        );

                        psSeleccion.setString(
                            2,
                            nombres[i] + " "
                            + apellidos[i]
                        );

                        psSeleccion.setBigDecimal(
                            3,
                            sueldos[i]
                        );

                        psSeleccion.executeUpdate();

                        psNomina.setString(
                            1,
                            cedulas[i]
                        );

                        psNomina.setString(
                            2,
                            nombres[i]
                        );

                        psNomina.setString(
                            3,
                            apellidos[i]
                        );

                        psNomina.setString(
                            4,
                            cargos[i]
                        );

                        psNomina.setBigDecimal(
                            5,
                            sueldos[i]
                        );

                        psNomina.executeUpdate();
                    }
                }

                cn.commit();

                System.out.println(
                    "[CONTRATACION] "
                    + cantidad
                    + " empleados registrados "
                    + "en Selección y Nómina."
                );

                salida.println(
                    "CONTRATAR_OK|" + cantidad
                );

            } catch (Exception e) {

                cn.rollback();

                System.out.println(
                    "[ERROR CONTRATACION] "
                    + e.getMessage()
                );

                salida.println(
                    "CONTRATAR_ERROR|" + mensaje(e)
                );
            }

        } catch (SQLException e) {

            salida.println(
                "CONTRATAR_ERROR|" + mensaje(e)
            );
        }
    }

    // ==========================================
    // 9. DESACTIVAR CONTRATACION ANTIGUA
    // ==========================================

    private void ejecutarContratacion(
            String peticion,
            PrintWriter salida) {

        salida.println(
            "CONTRATAR_ERROR|Utilice la nueva "
            + "pantalla de contratación individual."
        );
    }

    // ==========================================
    // 10. REPORTE RANKING
    // ==========================================

    private void enviarReporteRanking(
            String peticion,
            PrintWriter salida) {

        String[] partes =
            peticion.split("\\|");

        if (partes.length < 3) {

            salida.println("FIN_LISTA");
            return;
        }

        String fechaIni = partes[1];
        String fechaFin = partes[2];

        String sql =
            "SELECT c.nombre, "
            + "SUM(de.calificacion) AS total "
            + "FROM candidatos c "
            + "JOIN cabeceras_evaluacion ce "
            + "ON c.cedula = ce.cedula_candidato "
            + "JOIN detalles_evaluacion de "
            + "ON ce.numero_evaluacion = "
            + "de.numero_evaluacion "
            + "WHERE TO_CHAR(ce.fecha, "
            + "'YYYY-MM-DD') BETWEEN ? AND ? "
            + "GROUP BY c.cedula, c.nombre "
            + "ORDER BY total DESC";

        try (
            Connection cn = ConexionDB.getConexion();

            PreparedStatement ps =
                cn.prepareStatement(sql)
        ) {

            ps.setString(1, fechaIni);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    salida.println(
                        rs.getString("nombre")
                        + "|"
                        + rs.getDouble("total")
                    );
                }
            }

            salida.println("FIN_LISTA");

        } catch (SQLException e) {

            System.out.println(
                "[SERVIDOR ERROR] Ranking: "
                + e.getMessage()
            );

            salida.println("FIN_LISTA");
        }
    }

    // ==========================================
    // 11. REPORTE MATRIZ
    // ==========================================

    private void enviarReporteMatriz(
            String peticion,
            PrintWriter salida) {

        String[] partes =
            peticion.split("\\|");

        if (partes.length < 3) {

            salida.println("FIN_LISTA");
            return;
        }

        String fechaIni = partes[1];
        String fechaFin = partes[2];

        String sql =
            "SELECT c.nombre AS candidato, "
            + "NVL(SUM(CASE "
            + "WHEN de.codigo_parametro = 1 "
            + "THEN de.calificacion END), 0) "
            + "AS tecnica, "
            + "NVL(SUM(CASE "
            + "WHEN de.codigo_parametro = 2 "
            + "THEN de.calificacion END), 0) "
            + "AS psicologica, "
            + "NVL(SUM(CASE "
            + "WHEN de.codigo_parametro = 3 "
            + "THEN de.calificacion END), 0) "
            + "AS experiencia "
            + "FROM candidatos c "
            + "JOIN cabeceras_evaluacion ce "
            + "ON c.cedula = ce.cedula_candidato "
            + "JOIN detalles_evaluacion de "
            + "ON ce.numero_evaluacion = "
            + "de.numero_evaluacion "
            + "WHERE TO_CHAR(ce.fecha, "
            + "'YYYY-MM-DD') BETWEEN ? AND ? "
            + "GROUP BY c.cedula, c.nombre "
            + "ORDER BY c.nombre";

        try (
            Connection cn = ConexionDB.getConexion();

            PreparedStatement ps =
                cn.prepareStatement(sql)
        ) {

            ps.setString(1, fechaIni);
            ps.setString(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String fila =
                        rs.getString("candidato")
                        + "|"
                        + rs.getDouble("tecnica")
                        + "|"
                        + rs.getDouble("psicologica")
                        + "|"
                        + rs.getDouble("experiencia");

                    salida.println(fila);
                }
            }

            salida.println("FIN_LISTA");

        } catch (SQLException e) {

            System.out.println(
                "[SERVIDOR ERROR] Matriz: "
                + e.getMessage()
            );

            salida.println("FIN_LISTA");
        }
    }

    // ==========================================
    // 12. METODOS AUXILIARES
    // ==========================================

    private String decodificar(String valor) {

        return new String(
            Base64.getDecoder().decode(valor),
            StandardCharsets.UTF_8
        );
    }

    private String mensaje(Exception e) {

        String texto = e.getMessage();

        if (texto == null) {
            return "Error inesperado";
        }

        return texto
            .replace('|', ' ')
            .replace('\n', ' ')
            .replace('\r', ' ');
    }
}
