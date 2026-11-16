
package com.example.contabilidadpapeleria;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import javax.swing.*;
import javax.swing.border.*;

public class MenuContabilidadDiseno extends JFrame {

    // COLORES DEL DISEÑO
    private static final Color MORADO_OSCURO =
            new Color(76, 35, 115);

    private static final Color MORADO =
            new Color(111, 53, 159);

    private static final Color MORADO_PASTEL =
            new Color(235, 221, 246);

    private static final Color FONDO =
            new Color(250, 248, 253);

    private static final Color GRIS =
            new Color(100, 105, 115);

    private JPanel panelCentral;

    public MenuContabilidadDiseno() {

        setTitle("Sistema de Papelería - Contabilidad");
        setSize(1050, 650);
        setMinimumSize(new Dimension(950, 580));
        setLocationRelativeTo(null);

        // Cerrar este módulo no cierra SistemaPapeleria
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        // =====================================
        // ENCABEZADO SUPERIOR
        // =====================================

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(MORADO_PASTEL);
        encabezado.setPreferredSize(new Dimension(1050, 60));
        encabezado.setBorder(
                new EmptyBorder(0, 25, 0, 25)
        );

        JLabel tituloSistema = new JLabel(
                "Sistema de Papelería - Contabilidad"
        );

        tituloSistema.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );
        tituloSistema.setForeground(MORADO_OSCURO);

        JLabel responsable = new JLabel(
                "Bienvenido Bryan Jumbo"
        );

        responsable.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        responsable.setForeground(MORADO_OSCURO);

        encabezado.add(
                tituloSistema, BorderLayout.WEST
        );
        encabezado.add(
                responsable, BorderLayout.EAST
        );

        principal.add(encabezado, BorderLayout.NORTH);

        // =====================================
        // MENÚ LATERAL MORADO
        // =====================================

        JPanel lateral = new JPanel();
        lateral.setBackground(MORADO_OSCURO);
        lateral.setPreferredSize(new Dimension(210, 0));

        lateral.setLayout(
                new BoxLayout(lateral, BoxLayout.Y_AXIS)
        );

        lateral.setBorder(
                new EmptyBorder(25, 12, 20, 12)
        );

        JButton btnInicio =
                crearBotonLateral("Inicio");

        JButton btnTipos =
                crearBotonLateral("Tipos de cuenta");

        JButton btnCuentas =
                crearBotonLateral("Cuentas");

        JButton btnAsientos =
                crearBotonLateral("Asientos");

        JButton btnComprobantes =
                crearBotonLateral("Comprobantes");

        JButton btnReportes =
                crearBotonLateral("Reportes");

        JButton btnRegresar =
                crearBotonLateral("Regresar al menú");

        JButton[] botones = {
            btnInicio,
            btnTipos,
            btnCuentas,
            btnAsientos,
            btnComprobantes,
            btnReportes
        };

        for (JButton boton : botones) {
            lateral.add(boton);
            lateral.add(Box.createVerticalStrut(8));
        }

        lateral.add(Box.createVerticalGlue());
        lateral.add(new JSeparator());
        lateral.add(Box.createVerticalStrut(12));
        lateral.add(btnRegresar);

        principal.add(lateral, BorderLayout.WEST);

        // =====================================
        // ZONA CENTRAL
        // =====================================

        JPanel zonaDerecha =
                new JPanel(new BorderLayout());

        zonaDerecha.setBackground(FONDO);

        panelCentral =
                new JPanel(new BorderLayout());

        panelCentral.setBackground(FONDO);

        zonaDerecha.add(
                panelCentral, BorderLayout.CENTER
        );

        // =====================================
        // PIE DE PÁGINA
        // =====================================

        JPanel pie = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT, 20, 10
                )
        );

        pie.setBackground(MORADO_PASTEL);

        JLabel textoPie = new JLabel(
                "Sistema de Gestión de Papelería - Contabilidad"
        );

        textoPie.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        textoPie.setForeground(MORADO_OSCURO);

        pie.add(textoPie);

        zonaDerecha.add(pie, BorderLayout.SOUTH);

        principal.add(
                zonaDerecha, BorderLayout.CENTER
        );

        setContentPane(principal);

        // =====================================
        // EVENTOS DEL MENÚ LATERAL
        // =====================================

        btnInicio.addActionListener(
                e -> mostrarInicio()
        );

        btnTipos.addActionListener(
                e -> abrirFuncionOriginal(1)
        );

        btnCuentas.addActionListener(
                e -> abrirCuentas()
        );

        btnAsientos.addActionListener(
                e -> abrirAsientos()
        );

        btnComprobantes.addActionListener(
                e -> abrirFuncionOriginal(3)
        );

        btnReportes.addActionListener(
                e -> abrirFuncionOriginal(4)
        );

        btnRegresar.addActionListener(
                e -> dispose()
        );

        mostrarInicio();
    }

    // =====================================
    // CREAR BOTONES LATERALES
    // =====================================

    private JButton crearBotonLateral(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(MORADO_OSCURO);

        boton.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.setMaximumSize(
                new Dimension(185, 43)
        );

        boton.setPreferredSize(
                new Dimension(185, 43)
        );

        boton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        boton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(MORADO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(MORADO_OSCURO);
            }
        });

        return boton;
    }

    // =====================================
    // PANTALLA DE INICIO
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

        JPanel cabecera = new JPanel();
        cabecera.setOpaque(false);

        cabecera.setLayout(
                new BoxLayout(
                        cabecera, BoxLayout.Y_AXIS
                )
        );

        JLabel titulo =
                new JLabel("Contabilidad");

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        titulo.setForeground(MORADO_OSCURO);

        JLabel subtitulo = new JLabel(
                "Consulta y administra la información contable."
        );

        subtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        subtitulo.setForeground(GRIS);

        cabecera.add(titulo);
        cabecera.add(Box.createVerticalStrut(8));
        cabecera.add(subtitulo);

        inicio.add(
                cabecera, BorderLayout.NORTH
        );

        // =====================================
        // CUATRO TARJETAS CENTRALES
        // =====================================

        JPanel tarjetas = new JPanel(
                new GridLayout(2, 2, 18, 18)
        );

        tarjetas.setOpaque(false);

        tarjetas.add(
                crearTarjeta(
                        "Tipos de cuenta",
                        "Consultar la clasificación",
                        "de cuentas contables.",
                        () -> abrirFuncionOriginal(1)
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Cuentas contables",
                        "Administrar el plan",
                        "de cuentas.",
                        () -> abrirCuentas()
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Comprobantes",
                        "Consultar comprobantes",
                        "y registrar asientos.",
                        () -> abrirFuncionOriginal(3)
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Reportes",
                        "Consultar balances y",
                        "estados de resultados.",
                        () -> abrirFuncionOriginal(4)
                )
        );

        JPanel contenedor =
                new JPanel(new BorderLayout());

        contenedor.setOpaque(false);

        contenedor.add(
                tarjetas, BorderLayout.NORTH
        );

        inicio.add(
                contenedor, BorderLayout.CENTER
        );

        panelCentral.add(
                inicio, BorderLayout.CENTER
        );

        panelCentral.revalidate();
        panelCentral.repaint();
    }

    // =====================================
    // CREAR TARJETAS
    // =====================================

    private JPanel crearTarjeta(
            String nombre,
            String descripcion1,
            String descripcion2,
            Runnable accion) {

        JPanel tarjeta = new JPanel();

        tarjeta.setBackground(Color.WHITE);

        tarjeta.setLayout(
                new BoxLayout(
                        tarjeta, BoxLayout.Y_AXIS
                )
        );

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(222, 207, 235),
                                1,
                                true
                        ),
                        new EmptyBorder(20, 12, 20, 12)
                )
        );

        tarjeta.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        JLabel lblNombre = new JLabel(nombre);

        lblNombre.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        lblNombre.setForeground(MORADO_OSCURO);

        lblNombre.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel lblDescripcion1 =
                new JLabel(descripcion1);

        JLabel lblDescripcion2 =
                new JLabel(descripcion2);

        JLabel[] descripciones = {
            lblDescripcion1,
            lblDescripcion2
        };

        for (JLabel etiqueta : descripciones) {

            etiqueta.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            12
                    )
            );

            etiqueta.setForeground(GRIS);

            etiqueta.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );
        }

        tarjeta.add(Box.createVerticalGlue());
        tarjeta.add(lblNombre);

        tarjeta.add(
                Box.createVerticalStrut(12)
        );

        tarjeta.add(lblDescripcion1);
        tarjeta.add(lblDescripcion2);
        tarjeta.add(Box.createVerticalGlue());

        // Clic en cualquier parte de la tarjeta
        MouseAdapter evento = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(
                        new Color(247, 239, 252)
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(Color.WHITE);
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
    // ABRIR FORMULARIO DE CUENTAS
    // =====================================

    private void abrirCuentas() {

        CuentaContableForm ventana =
                new CuentaContableForm();

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    // =====================================
    // ABRIR FORMULARIO DE ASIENTOS
    // =====================================

    private void abrirAsientos() {

        ComprobanteContableForm ventana =
                new ComprobanteContableForm();

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setLocationRelativeTo(this);
        ventana.setVisible(true);
    }

    // =====================================
    // REUTILIZAR FUNCIONES ORIGINALES
    // SIN MOSTRAR EL MENÚ ANTIGUO
    // =====================================

    private void abrirFuncionOriginal(int numeroBoton) {

        // Crear el menú original en segundo plano
        MenuContabilidad original =
                new MenuContabilidad();

        original.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        try {

            Field campo =
                    MenuContabilidad.class
                            .getDeclaredField(
                                    "jButton" + numeroBoton
                            );

            campo.setAccessible(true);

            JButton boton =
                    (JButton) campo.get(original);

            // Ejecutar la función directamente
            // sin mostrar el menú original
            boton.doClick();

        } catch (ReflectiveOperationException
                | SecurityException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo abrir la función: "
                    + ex.getMessage(),
                    "Contabilidad",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            // Cerrar la ventana original oculta
            original.dispose();
        }
    }

    // =====================================
    // MÉTODO PRINCIPAL
    // =====================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new MenuContabilidadDiseno()
                    .setVisible(true);

        });
    }
}
