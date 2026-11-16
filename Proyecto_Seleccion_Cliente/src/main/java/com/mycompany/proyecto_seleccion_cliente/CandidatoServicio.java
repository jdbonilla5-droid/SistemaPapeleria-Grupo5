package com.mycompany.proyecto_seleccion_cliente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CandidatoServicio {

    private static final String HOST = "127.0.0.1";
    private static final int PUERTO = 5000;

    public static void cargarTablaCandidatos(JTable tabla) {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Cédula", "Nombre Completo", "Fecha Nacimiento"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PUERTO), 5000);
            socket.setSoTimeout(10000);

            try (PrintWriter salida = new PrintWriter(
                    socket.getOutputStream(), true, StandardCharsets.UTF_8);
                 BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

                salida.println("LISTAR_CANDIDATOS");
                String respuesta;
                boolean fin = false;

                while ((respuesta = entrada.readLine()) != null) {
                    if (respuesta.equals("FIN_LISTA")) {
                        fin = true;
                        break;
                    }
                    if (respuesta.startsWith("LISTA_ERROR|")) {
                        JOptionPane.showMessageDialog(tabla,
                            "Error al consultar candidatos: "
                            + respuesta.substring("LISTA_ERROR|".length()),
                            "Error de Oracle", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    String[] datos = respuesta.split("\\|", -1);
                    if (datos.length != 3) {
                        JOptionPane.showMessageDialog(tabla,
                            "Respuesta inesperada del servidor: " + respuesta,
                            "Error de comunicación", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    modelo.addRow(datos);
                }

                if (!fin) {
                    JOptionPane.showMessageDialog(tabla,
                        "El servidor cerró la conexión antes de terminar la lista.",
                        "Error de comunicación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                tabla.setModel(modelo);
                System.out.println("[CLIENTE] Candidatos recibidos: " + modelo.getRowCount());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(tabla,
                "No se pudo consultar la lista: " + e.getMessage(),
                "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean insertarCandidato(String cedula, String nombre, String fecha) {
        if (cedula == null || nombre == null || fecha == null
                || cedula.isBlank() || nombre.isBlank() || fecha.isBlank()
                || cedula.contains("|") || nombre.contains("|") || fecha.contains("|")) {
            JOptionPane.showMessageDialog(null,
                "Complete todos los datos y no utilice el carácter |.",
                "Datos inválidos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(HOST, PUERTO), 5000);
            socket.setSoTimeout(10000);

            try (PrintWriter salida = new PrintWriter(
                    socket.getOutputStream(), true, StandardCharsets.UTF_8);
                 BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

                salida.println("REGISTRAR_CANDIDATO|" + cedula.trim()
                    + "|" + nombre.trim() + "|" + fecha.trim());

                String respuesta = entrada.readLine();
                if ("REGISTRO_OK".equals(respuesta)) {
                    return true;
                }
                if (respuesta != null && respuesta.startsWith("REGISTRO_ERROR|")) {
                    JOptionPane.showMessageDialog(null,
                        "Error en Oracle: " + respuesta.substring("REGISTRO_ERROR|".length()),
                        "Error en Base de Datos", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Respuesta inesperada del servidor: " + respuesta,
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error de red al guardar: " + e.getMessage(),
                "Error de Red", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
}
