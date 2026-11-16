package clientejava;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class Cliente extends JFrame{
    private JTextField txtNombre;
    private JTextField txtIP;
    private JTextField txtMensaje;
    private JTextArea txtRespuesta;
    
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    
    public Cliente () {
        setTitle("Sistema Cliente-Servidor");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        txtNombre = new JTextField("Cliente 1");
        txtIP = new JTextField("127.0.0.1");
        txtMensaje = new JTextField();
        
        JButton btnConectar = new JButton("Conectar");
        JButton btnEnviar = new JButton("Enviar mensaje");
        
        panel.add(new JLabel("Nombre del cliente"));
        panel.add(txtNombre);
        
        panel.add(new JLabel("IP del servidor:"));
        panel.add(txtIP);
        
        panel.add(new JLabel("Mensaje:"));
        panel.add(txtMensaje);
        
        panel.add(btnConectar);
        panel.add(btnEnviar);
        
        txtRespuesta = new JTextArea();
        txtRespuesta.setEditable(false);
        
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(txtRespuesta), BorderLayout.CENTER);
        
        btnConectar.addActionListener(e -> conectar());
        btnEnviar.addActionListener(e -> enviarMensaje());
    }
    
    private void conectar  (){
        
        try {
            socket = new Socket(txtIP.getText().trim(),5000);
            entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(
                    socket.getOutputStream(), true);
            txtRespuesta.append("Conectado al servidor.\n");
        } catch (Exception e){
            txtRespuesta.append("Error de conexion:"+e.getMessage()+"\n");
            }
    }
        
    private void enviarMensaje(){
        if(socket == null || socket.isClosed()
                || !socket.isConnected()){
        txtRespuesta.append("Primero debes conectarte al servidor.\n");
        return;
        }
        
        String nombre = txtNombre.getText().trim();
        String mensaje = txtMensaje.getText().trim();
        
        if(nombre.isEmpty() || mensaje.isEmpty()){
            txtRespuesta.append("Ingresa tu nombre y un mensaje.\n");
            return;
        }
        
        new Thread (() -> {
            try {
                String texto = nombre + ": " + mensaje;
                salida.println (texto);
                
                if(salida.checkError()){
                    throw new Exception("No se pudo enviar el mensaje");
                }
                
                String respuesta = entrada.readLine();
                
                SwingUtilities.invokeLater(() -> {
                    if (respuesta != null){
                        txtRespuesta.append(respuesta + "\n");
                    } else {
                        txtRespuesta.append("El servidor cerro la conexion.\n");
                    }
                });
            } catch(Exception e){
                SwingUtilities.invokeLater(() ->
                        txtRespuesta.append("Error: " + e.getMessage() + "\n"));
            }
        }).start();
    }
    
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            Cliente ventana = new Cliente();
            ventana.setVisible(true);
        });
    }
    }