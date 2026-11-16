
package servidorjava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {

        int puerto = 5000;

        try (ServerSocket servidor = new ServerSocket(puerto)) {

            System.out.println("SERVIDOR JAVA INICIADO");
            System.out.println("Esperando clientes en el puerto " + puerto);

            while (true) {

                Socket cliente = servidor.accept();

                System.out.println(
                    "Nuevo cliente conectado: "
                    + cliente.getInetAddress()
                );

                Thread hilo = new Thread(() -> atenderCliente(cliente));
                hilo.start();
            }

        } catch (Exception e) {
            System.out.println(
                "Error del servidor: " + e.getMessage()
            );
        }
    }

    public static void atenderCliente(Socket cliente) {

        try (
            Socket conexion = cliente;

            BufferedReader entrada = new BufferedReader(
                new InputStreamReader(conexion.getInputStream())
            );

            PrintWriter salida = new PrintWriter(
                conexion.getOutputStream(), true
            )
        ) {

            String mensaje;

            while ((mensaje = entrada.readLine()) != null) {

                System.out.println("Mensaje recibido: " + mensaje);

                try {

                    String[] partes = mensaje.split(": ", 2);

                    String nombreCliente = partes.length == 2
                            ? partes[0] : "Cliente";

                    String contenido = partes.length == 2
                            ? partes[1] : mensaje;

                    ConexionOracle.guardarMensaje(
                            nombreCliente, contenido
                    );

                    System.out.println(
                        "Mensaje guardado en Oracle."
                    );

                    salida.println(
                        "Mensaje recibido y guardado en Oracle."
                    );

                } catch (Exception e) {

                    System.out.println(
                        "Error al guardar: " + e.getMessage()
                    );

                    e.printStackTrace();

                    salida.println(
                        "Error: no se pudo guardar el mensaje."
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                "Cliente desconectado: " + e.getMessage()
            );
        }
    }
}
