
package com.papeleria.nomina.presentacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;

public class MenuPrincipalNomina extends JFrame {

    // COLORES DEL DISEÑO
    private static final Color AZUL_OSCURO =
            new Color(9, 48, 89);

    private static final Color AZUL =
            new Color(20, 125, 235);

    private static final Color CELESTE =
            new Color(234, 246, 255);

    private static final Color FONDO =
            new Color(248, 251, 255);

    private static final Color BLANCO = Color.WHITE;

    private static final Color GRIS =
            new Color(100, 115, 130);

    private JPanel panelCentral;

    public MenuPrincipalNomina() {

        setTitle("Sistema de Papelería - Nómina");

        setSize(1050, 650);

        setMinimumSize(
                new Dimension(950, 580)
        );

        setLocationRelativeTo(null);

        // NO CERRAR EL SISTEMA PRINCIPAL
        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel principal = new JPanel(
                new BorderLayout()
        );

        principal.setBackground(FONDO);

        // =====================================
        // ENCABEZADO SUPERIOR
        // =====================================

        JPanel encabezado = new JPanel(
                new BorderLayout()
        );

        encabezado.setBackground(CELESTE);

        encabezado.setPreferredSize(
                new Dimension(1050, 60)
        );

        encabezado.setBorder(
                new EmptyBorder(0, 25, 0, 25)
        );

        JLabel tituloSistema = new JLabel(
                "Sistema de Papelería - Nómina"
        );

        tituloSistema.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        tituloSistema.setForeground(AZUL_OSCURO);

        JLabel usuario = new JLabel(
                "Bienvenida Denisse Bonilla"
        );

        usuario.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        usuario.setForeground(AZUL_OSCURO);

        encabezado.add(
                tituloSistema,
                BorderLayout.WEST
        );

        encabezado.add(
                usuario,
                BorderLayout.EAST
        );

        principal.add(
                encabezado,
                BorderLayout.NORTH
        );

        // =====================================
        // MENÚ LATERAL
        // =====================================

        JPanel lateral = new JPanel();

        lateral.setBackground(AZUL_OSCURO);

        lateral.setPreferredSize(
                new Dimension(210, 0)
        );

        lateral.setLayout(
                new BoxLayout(
                        lateral,
                        BoxLayout.Y_AXIS
                )
        );

        lateral.setBorder(
                new EmptyBorder(25, 12, 20, 12)
        );

        JButton btnInicio = crearBotonLateral(
                "Inicio"
        );

        JButton btnEmpleados = crearBotonLateral(
                "Empleados"
        );

        JButton btnNomina = crearBotonLateral(
                "Nómina"
        );

        JButton btnMotivos = crearBotonLateral(
                "Motivos"
        );

        JButton btnReportes = crearBotonLateral(
                "Reportes"
        );

        JButton btnRegresar = crearBotonLateral(
                "Regresar al menú"
        );

        lateral.add(btnInicio);
        lateral.add(Box.createVerticalStrut(8));

        lateral.add(btnEmpleados);
        lateral.add(Box.createVerticalStrut(8));

        lateral.add(btnNomina);
        lateral.add(Box.createVerticalStrut(8));

        lateral.add(btnMotivos);
        lateral.add(Box.createVerticalStrut(8));

        lateral.add(btnReportes);

        lateral.add(Box.createVerticalGlue());

        lateral.add(new JSeparator());
        lateral.add(Box.createVerticalStrut(12));

        lateral.add(btnRegresar);

        principal.add(
                lateral,
                BorderLayout.WEST
        );

        // =====================================
        // CONTENIDO CENTRAL
        // =====================================

        JPanel zonaDerecha = new JPanel(
                new BorderLayout()
        );

        zonaDerecha.setBackground(FONDO);

        panelCentral = new JPanel(
                new BorderLayout()
        );

        panelCentral.setBackground(FONDO);

        zonaDerecha.add(
                panelCentral,
                BorderLayout.CENTER
        );

        // =====================================
        // PIE DE PÁGINA
        // =====================================

        JPanel pie = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        20,
                        10
                )
        );

        pie.setBackground(CELESTE);

        JLabel textoPie = new JLabel(
                "Sistema de Gestión de Papelería - Nómina"
        );

        textoPie.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        textoPie.setForeground(AZUL_OSCURO);

        pie.add(textoPie);

        zonaDerecha.add(
                pie,
                BorderLayout.SOUTH
        );

        principal.add(
                zonaDerecha,
                BorderLayout.CENTER
        );

        setContentPane(principal);

        // =====================================
        // EVENTOS DE BOTONES LATERALES
        // =====================================

        btnInicio.addActionListener(
                e -> mostrarInicio()
        );

        btnEmpleados.addActionListener(
                e -> abrirEmpleados()
        );

        btnNomina.addActionListener(
                e -> abrirNomina()
        );

        btnMotivos.addActionListener(
                e -> abrirMotivos()
        );

        btnReportes.addActionListener(
                e -> abrirReportes()
        );

        btnRegresar.addActionListener(
                e -> regresarMenu()
        );

        mostrarInicio();
    }

    // =====================================
    // CREAR BOTONES LATERALES
    // =====================================

    private JButton crearBotonLateral(
            String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        boton.setForeground(BLANCO);

        boton.setBackground(AZUL_OSCURO);

        boton.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        boton.setFocusPainted(false);

        boton.setBorderPainted(false);

        boton.setOpaque(true);

        boton.setMaximumSize(
                new Dimension(185, 45)
        );

        boton.setPreferredSize(
                new Dimension(185, 45)
        );

        boton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        boton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        boton.setBackground(
                                new Color(22, 83, 145)
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        boton.setBackground(
                                AZUL_OSCURO
                        );
                    }
                }
        );

        return boton;
    }

    // =====================================
    // PANTALLA PRINCIPAL DE NÓMINA
    // =====================================

    private void mostrarInicio() {

        panelCentral.removeAll();

        JPanel inicio = new JPanel(
                new BorderLayout(0, 25)
        );

        inicio.setBackground(FONDO);

        inicio.setBorder(
                new EmptyBorder(35, 35, 35, 35)
        );

        // ENCABEZADO CENTRAL
        JPanel encabezado = new JPanel();

        encabezado.setOpaque(false);

        encabezado.setLayout(
                new BoxLayout(
                        encabezado,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo = new JLabel(
                "Nómina"
        );

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        titulo.setForeground(AZUL_OSCURO);

        JLabel subtitulo = new JLabel(
                "Selecciona una función del módulo de nómina."
        );

        subtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        subtitulo.setForeground(GRIS);

        encabezado.add(titulo);

        encabezado.add(
                Box.createVerticalStrut(8)
        );

        encabezado.add(subtitulo);

        inicio.add(
                encabezado,
                BorderLayout.NORTH
        );

        // =====================================
        // CUATRO TARJETAS
        // =====================================

        JPanel tarjetas = new JPanel(
                new GridLayout(2, 2, 18, 18)
        );

        tarjetas.setOpaque(false);

        tarjetas.setPreferredSize(
                new Dimension(650, 300)
        );

        tarjetas.add(
                crearTarjeta(
                        "Empleados",
                        "Registrar, modificar,",
                        "buscar y eliminar empleados.",
                        "",
                        () -> abrirEmpleados()
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Nómina",
                        "Registrar y administrar",
                        "pagos de nómina.",
                        "",
                        () -> abrirNomina()
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Motivos",
                        "Gestionar motivos de",
                        "ingresos y egresos.",
                        "",
                        () -> abrirMotivos()
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Reportes",
                        "Consultar y generar",
                        "reportes de nómina.",
                        "",
                        () -> abrirReportes()
                )
        );

        JPanel contenedorTarjetas =
                new JPanel(new BorderLayout());

        contenedorTarjetas.setOpaque(false);

        contenedorTarjetas.add(
                tarjetas,
                BorderLayout.NORTH
        );

        inicio.add(
                contenedorTarjetas,
                BorderLayout.CENTER
        );

        panelCentral.add(
                inicio,
                BorderLayout.CENTER
        );

        panelCentral.revalidate();
        panelCentral.repaint();
    }

    // =====================================
    // CREAR TARJETAS BLANCAS
    // =====================================

    private JPanel crearTarjeta(
            String nombre,
            String descripcion1,
            String descripcion2,
            String icono,
            Runnable accion) {

        JPanel tarjeta = new JPanel();

        tarjeta.setBackground(BLANCO);

        tarjeta.setLayout(
                new BoxLayout(
                        tarjeta,
                        BoxLayout.Y_AXIS
                )
        );

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(214, 230, 244),
                                1,
                                true
                        ),
                        new EmptyBorder(18, 12, 18, 12)
                )
        );

        tarjeta.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        JLabel lblIcono = new JLabel(icono);

        lblIcono.setFont(
                new Font(
                        "Segoe UI Symbol",
                        Font.BOLD,
                        32
                )
        );

        lblIcono.setForeground(AZUL);

        lblIcono.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel lblNombre = new JLabel(nombre);

        lblNombre.setFont(
                new Font("Segoe UI", Font.BOLD, 17)
        );

        lblNombre.setForeground(AZUL_OSCURO);

        lblNombre.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel lblDescripcion1 =
                new JLabel(descripcion1);

        JLabel lblDescripcion2 =
                new JLabel(descripcion2);

        lblDescripcion1.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        lblDescripcion2.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        lblDescripcion1.setForeground(GRIS);
        lblDescripcion2.setForeground(GRIS);

        lblDescripcion1.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        lblDescripcion2.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        tarjeta.add(Box.createVerticalGlue());

        if (!icono.isEmpty()) tarjeta.add(lblIcono);

        if (!icono.isEmpty()) tarjeta.add(Box.createVerticalStrut(8));

        tarjeta.add(lblNombre);

        tarjeta.add(
                Box.createVerticalStrut(8)
        );

        tarjeta.add(lblDescripcion1);
        tarjeta.add(lblDescripcion2);

        tarjeta.add(Box.createVerticalGlue());

        // CLIC EN TODA LA TARJETA
        MouseAdapter evento =
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            MouseEvent e) {

                        accion.run();
                    }

                    @Override
                    public void mouseEntered(
                            MouseEvent e) {

                        tarjeta.setBackground(
                                new Color(239, 248, 255)
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e) {

                        tarjeta.setBackground(BLANCO);
                    }
                };

        tarjeta.addMouseListener(evento);

        for (Component componente :
                tarjeta.getComponents()) {

            componente.addMouseListener(evento);
        }

        return tarjeta;
    }

    // =====================================
    // ABRIR VENTANAS REALES
    // =====================================

    private void abrirEmpleados() {

        VentanaEmpleados ventana =
                new VentanaEmpleados();

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void abrirNomina() {

        VentanaNomina ventana =
                new VentanaNomina();

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void abrirMotivos() {

        VentanaMotivos ventana =
                new VentanaMotivos();

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    private void abrirReportes() {

        VentanaReportes ventana =
                new VentanaReportes();

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    // =====================================
    // REGRESAR AL MENÚ DE PAPELERÍA
    // =====================================

    private void regresarMenu() {

        dispose();
    }

    // =====================================
    // MÉTODO PRINCIPAL
    // =====================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new MenuPrincipalNomina().setVisible(true);

        });
    }
}