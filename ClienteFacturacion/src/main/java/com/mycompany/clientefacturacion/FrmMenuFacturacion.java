package com.mycompany.clientefacturacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;

public class FrmMenuFacturacion extends javax.swing.JFrame {

    // Paleta verde y verde pastel
    private static final Color VERDE_LATERAL = new Color(20, 112, 68);
    private static final Color VERDE_CABECERA = new Color(220, 243, 225);
    private static final Color VERDE_TEXTO = new Color(19, 91, 55);
    private static final Color FONDO = new Color(248, 251, 255);
    private static final Color BLANCO = Color.WHITE;
    private static final Color GRIS = new Color(100, 115, 130);
    private static final Color VERDE_BOTON = new Color(33, 150, 83);

    private JPanel panelCentral;

    public FrmMenuFacturacion() {
        setTitle("Sistema de Papelería - Facturación");
        setSize(1050, 650);
        setMinimumSize(new Dimension(950, 580));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        // ==========================================
        // 1. ENCABEZADO (Barra superior verde pastel)
        // ==========================================
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(VERDE_CABECERA);
        encabezado.setPreferredSize(new Dimension(1050, 60));
        encabezado.setBorder(new EmptyBorder(0, 25, 0, 25));

        JLabel tituloSistema = new JLabel("Facturación");
        tituloSistema.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tituloSistema.setForeground(VERDE_TEXTO);

        JLabel usuario = new JLabel("Bienvenida Jeniffer Yanez");
        usuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usuario.setForeground(VERDE_TEXTO);

        encabezado.add(tituloSistema, BorderLayout.WEST);
        encabezado.add(usuario, BorderLayout.EAST);
        principal.add(encabezado, BorderLayout.NORTH);

        // ==========================================
        // 2. MENÚ LATERAL (Barra verde)
        // ==========================================
        JPanel lateral = new JPanel();
        lateral.setBackground(VERDE_LATERAL);
        lateral.setPreferredSize(new Dimension(210, 0));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(new EmptyBorder(25, 12, 20, 12));

        JButton btnInicio = crearBotonLateral("Inicio");
        JButton btnClientes = crearBotonLateral("Clientes");
        JButton btnCiudades = crearBotonLateral("Ciudades");
        JButton btnFacturar = crearBotonLateral("Facturar");
        JButton btnReportes = crearBotonLateral("Reportes");
        JButton btnRegresar = crearBotonLateral("Regresar al menú");

        lateral.add(btnInicio);
        lateral.add(Box.createVerticalStrut(8));
        lateral.add(btnClientes);
        lateral.add(Box.createVerticalStrut(8));
        lateral.add(btnCiudades);
        lateral.add(Box.createVerticalStrut(8));
        lateral.add(btnFacturar);
        lateral.add(Box.createVerticalStrut(8));
        lateral.add(btnReportes);
        lateral.add(Box.createVerticalGlue());
        lateral.add(new JSeparator());
        lateral.add(Box.createVerticalStrut(12));
        lateral.add(btnRegresar);
        principal.add(lateral, BorderLayout.WEST);

        // ==========================================
        // 3. ZONA DERECHA (Contenido central)
        // ==========================================
        JPanel zonaDerecha = new JPanel(new BorderLayout());
        zonaDerecha.setBackground(FONDO);
        panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(FONDO);
        zonaDerecha.add(panelCentral, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        pie.setBackground(VERDE_CABECERA);
        JLabel textoPie = new JLabel("Sistema de Gestión de Papelería - Facturación");
        textoPie.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textoPie.setForeground(VERDE_TEXTO);
        pie.add(textoPie);
        zonaDerecha.add(pie, BorderLayout.SOUTH);

        principal.add(zonaDerecha, BorderLayout.CENTER);
        setContentPane(principal);

        // ==========================================
        // 4. ACCIONES DE LOS BOTONES
        // ==========================================
        btnInicio.addActionListener(e -> mostrarInicio());
        btnClientes.addActionListener(e -> abrirClientes());
        btnCiudades.addActionListener(e -> abrirCiudades());
        btnFacturar.addActionListener(e -> abrirFacturar());
        btnReportes.addActionListener(e -> abrirReportes());
        btnRegresar.addActionListener(e -> dispose());

        mostrarInicio();
    }

    private JButton crearBotonLateral(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        boton.setForeground(BLANCO);
        boton.setBackground(VERDE_LATERAL);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setMaximumSize(new Dimension(185, 45));
        boton.setPreferredSize(new Dimension(185, 45));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(33, 150, 83));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(VERDE_LATERAL);
            }
        });
        return boton;
    }

    private void mostrarInicio() {
        panelCentral.removeAll();

        JPanel inicio = new JPanel(new BorderLayout(0, 25));
        inicio.setBackground(FONDO);
        inicio.setBorder(new EmptyBorder(35, 35, 35, 35));

        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Facturación");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(0, 100, 50));

        JLabel subtitulo = new JLabel("Registra ventas y gestiona tus clientes.");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(GRIS);

        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(8));
        encabezado.add(subtitulo);
        inicio.add(encabezado, BorderLayout.NORTH);

        JPanel tarjetas = new JPanel(new GridLayout(2, 2, 20, 20));
        tarjetas.setOpaque(false);

        tarjetas.add(crearTarjeta("Clientes", "Registrar, modificar", "y consultar clientes.", "👥", () -> abrirClientes()));
        tarjetas.add(crearTarjeta("Ciudades", "Gestionar ciudades", "de entrega.", "🏙", () -> abrirCiudades()));
        tarjetas.add(crearTarjeta("Facturar", "Emitir y consultar", "facturas.", "🛒", () -> abrirFacturar()));
        tarjetas.add(crearTarjeta("Reportes", "Ventas y", "estadísticas.", "📊", () -> abrirReportes()));

        JPanel contenedorTarjetas = new JPanel(new BorderLayout());
        contenedorTarjetas.setOpaque(false);
        contenedorTarjetas.add(tarjetas, BorderLayout.NORTH);

        inicio.add(contenedorTarjetas, BorderLayout.CENTER);
        panelCentral.add(inicio, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private JPanel crearTarjeta(String nombre, String desc1, String desc2, String icono, Runnable accion) {
        JPanel tarjeta = new JPanel();
        tarjeta.setBackground(BLANCO);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(214, 230, 244), 1, true),
                new EmptyBorder(18, 12, 18, 12)
        ));
        tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI Symbol", Font.BOLD, 36));
        lblIcono.setForeground(VERDE_BOTON);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNombre.setForeground(VERDE_TEXTO);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc1 = new JLabel(desc1);
        JLabel lblDesc2 = new JLabel(desc2);
        lblDesc1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc1.setForeground(GRIS);
        lblDesc2.setForeground(GRIS);
        lblDesc1.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc2.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(lblIcono);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblDesc1);
        tarjeta.add(lblDesc2);
        tarjeta.add(Box.createVerticalGlue());

        MouseAdapter evento = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { accion.run(); }
            @Override
            public void mouseEntered(MouseEvent e) { tarjeta.setBackground(new Color(235, 249, 238)); }
            @Override
            public void mouseExited(MouseEvent e) { tarjeta.setBackground(BLANCO); }
        };
        
        tarjeta.addMouseListener(evento);
        for (Component c : tarjeta.getComponents()) { c.addMouseListener(evento); }

        return tarjeta;
    }

    private void abrirClientes() {
        FrmCliente ventana = new FrmCliente();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void abrirCiudades() {
        FrmCiudad ventana = new FrmCiudad();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void abrirFacturar() {
        FrmFactura ventana = new FrmFactura();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void abrirReportes() {
        FrmReporte1 ventana = new FrmReporte1();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMenuFacturacion().setVisible(true);
            }
        });
    }
}