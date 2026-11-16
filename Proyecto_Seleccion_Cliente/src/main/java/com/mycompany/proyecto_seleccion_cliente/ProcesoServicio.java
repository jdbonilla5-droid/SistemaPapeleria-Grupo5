
package com.mycompany.proyecto_seleccion_cliente;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.swing.*;

public class ProcesoServicio {

    private static final String HOST = "127.0.0.1";
    private static final int PUERTO = 5000;

    public static class Candidato {

        public final String cedula;
        public final String nombre;
        public final double puntaje;

        public Candidato(
                String cedula,
                String nombre,
                double puntaje) {

            this.cedula = cedula;
            this.nombre = nombre;
            this.puntaje = puntaje;
        }
    }

    public static class Contratado {

        public final String cedula;
        public final String nombres;
        public final String apellidos;
        public final String cargo;
        public final double sueldo;

        public Contratado(
                String cedula,
                String nombres,
                String apellidos,
                String cargo,
                double sueldo) {

            this.cedula = cedula;
            this.nombres = nombres;
            this.apellidos = apellidos;
            this.cargo = cargo;
            this.sueldo = sueldo;
        }
    }

    private static String codificar(String texto) {

        return Base64.getEncoder().encodeToString(
            texto.getBytes(StandardCharsets.UTF_8)
        );
    }

    public static java.util.List<Candidato>
            listarElegibles() throws IOException {

        java.util.List<Candidato> lista =
            new ArrayList<>();

        try (
            Socket socket = new Socket(HOST, PUERTO);

            PrintWriter salida = new PrintWriter(
                new OutputStreamWriter(
                    socket.getOutputStream(),
                    StandardCharsets.UTF_8
                ),
                true
            );

            BufferedReader entrada =
                new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                )
        ) {

            salida.println("LISTAR_ELEGIBLES");

            String respuesta;

            while ((respuesta = entrada.readLine())
                    != null) {

                if (respuesta.equals("FIN_LISTA")) {
                    return lista;
                }

                if (respuesta.startsWith(
                        "LISTA_ERROR|")) {

                    throw new IOException(
                        respuesta.substring(12)
                    );
                }

                String[] partes =
                    respuesta.split("\\|", -1);

                if (partes.length == 4
                        && partes[0].equals("CANDIDATO")) {

                    String nombre = new String(
                        Base64.getDecoder().decode(
                            partes[2]
                        ),
                        StandardCharsets.UTF_8
                    );

                    lista.add(
                        new Candidato(
                            partes[1],
                            nombre,
                            Double.parseDouble(
                                partes[3]
                            )
                        )
                    );
                }
            }

            throw new IOException(
                "El servidor cerró la conexión "
                + "sin finalizar la lista."
            );
        }
    }

    public static void contratarLote(
            java.util.List<Contratado> lista)
            throws IOException {

        if (lista.isEmpty()) {
            throw new IOException(
                "No hay candidatos seleccionados."
            );
        }

        StringBuilder peticion =
            new StringBuilder("CONTRATAR_LOTE|")
                .append(lista.size());

        for (Contratado contratado : lista) {

            peticion.append('|')
                    .append(contratado.cedula)
                    .append('|')
                    .append(codificar(
                        contratado.nombres
                    ))
                    .append('|')
                    .append(codificar(
                        contratado.apellidos
                    ))
                    .append('|')
                    .append(codificar(
                        contratado.cargo
                    ))
                    .append('|')
                    .append(
                        java.math.BigDecimal.valueOf(
                            contratado.sueldo
                        ).toPlainString()
                    );
        }

        try (
            Socket socket = new Socket(HOST, PUERTO);

            PrintWriter salida = new PrintWriter(
                new OutputStreamWriter(
                    socket.getOutputStream(),
                    StandardCharsets.UTF_8
                ),
                true
            );

            BufferedReader entrada =
                new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                )
        ) {

            salida.println(peticion);

            String respuesta = entrada.readLine();

            if (respuesta == null
                    || !respuesta.startsWith(
                        "CONTRATAR_OK|")) {

                throw new IOException(
                    respuesta == null
                        ? "Sin respuesta del servidor."
                        : respuesta.replace(
                            "CONTRATAR_ERROR|", ""
                        )
                );
            }
        }
    }

    public static void cargarComboCandidatos(
            JComboBox<String> combo) {

        combo.removeAllItems();

        try (
            Socket socket = new Socket(HOST, PUERTO);

            PrintWriter salida =
                new PrintWriter(
                    socket.getOutputStream(), true
                );

            BufferedReader entrada =
                new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()
                    )
                )
        ) {

            salida.println("LISTAR_CANDIDATOS");

            String respuesta;

            while ((respuesta = entrada.readLine())
                    != null
                    && !respuesta.equals("FIN_LISTA")) {

                String[] partes =
                    respuesta.split("\\|");

                if (partes.length >= 2) {

                    combo.addItem(
                        partes[0] + " - " + partes[1]
                    );
                }
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                null,
                "Error al cargar candidatos: "
                + ex.getMessage()
            );
        }
    }

    public static void cargarComboParametros(
            JComboBox<String> combo) {

        combo.removeAllItems();

        try (
            Socket socket = new Socket(HOST, PUERTO);

            PrintWriter salida =
                new PrintWriter(
                    socket.getOutputStream(), true
                );

            BufferedReader entrada =
                new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()
                    )
                )
        ) {

            salida.println("LISTAR_PARAMETROS");

            String respuesta;

            while ((respuesta = entrada.readLine())
                    != null
                    && !respuesta.equals("FIN_LISTA")) {

                String[] partes =
                    respuesta.split("\\|");

                if (partes.length >= 3) {

                    combo.addItem(
                        partes[0] + " - "
                        + partes[1]
                        + " (Max: "
                        + partes[2] + ")"
                    );
                }
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                null,
                "Error al cargar parámetros: "
                + ex.getMessage()
            );
        }
    }

    public static boolean registrarEvaluacion(
            String cedula,
            String detalles) {

        try (
            Socket socket = new Socket(HOST, PUERTO);

            PrintWriter salida =
                new PrintWriter(
                    socket.getOutputStream(), true
                );

            BufferedReader entrada =
                new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()
                    )
                )
        ) {

            salida.println(
                "GUARDAR_EVALUACION|"
                + cedula + "|" + detalles
            );

            String respuesta = entrada.readLine();

            if ("PROCESO_OK".equals(respuesta)) {
                return true;
            }

            JOptionPane.showMessageDialog(
                null,
                respuesta == null
                    ? "Sin respuesta."
                    : respuesta.replace(
                        "PROCESO_ERROR|", ""
                    )
            );

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                null,
                "Error al guardar evaluación: "
                + ex.getMessage()
            );
        }

        return false;
    }

    public static boolean ejecutarContratacion(
            int cantidad,
            double sueldo) {

        JOptionPane.showMessageDialog(
            null,
            "Utilice la nueva ventana "
            + "de contratación individual."
        );

        return false;
    }
}
