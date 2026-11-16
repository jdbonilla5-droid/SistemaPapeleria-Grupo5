
package com.mycompany.proyecto_seleccion_cliente;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class FrmMenuSeleccion extends JFrame {

    // COLORES
    private static final Color NARANJA_OSCURO =
            new Color(170, 75, 25);

    private static final Color NARANJA =
            new Color(195, 95, 40);

    private static final Color NARANJA_PASTEL =
            new Color(255, 229, 207);

    private static final Color FONDO =
            new Color(250, 249, 247);

    private static final Color GRIS =
            new Color(100, 105, 115);

    private JPanel panelCentral;

    public FrmMenuSeleccion() {

        setTitle("Sistema de Papelería - Selección");

        setSize(1050, 650);

        setMinimumSize(
                new Dimension(950, 580)
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        inicializarComponentes();
    }

    private void inicializarComponentes() {

        JPanel principal =
                new JPanel(new BorderLayout());

        principal.setBackground(FONDO);

        // ENCABEZADO SUPERIOR
        JPanel encabezado =
                new JPanel(new BorderLayout());

        encabezado.setBackground(NARANJA_PASTEL);

        encabezado.setPreferredSize(
                new Dimension(1050, 60)
        );

        encabezado.setBorder(
                new EmptyBorder(0, 25, 0, 25)
        );

        JLabel tituloSistema = new JLabel(
                "Sistema de Papelería - Selección"
        );

        tituloSistema.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        tituloSistema.setForeground(NARANJA_OSCURO);

        JLabel responsable = new JLabel(
                "Bienvenido Nelson Mera"
        );

        responsable.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        responsable.setForeground(NARANJA_OSCURO);

        encabezado.add(
                tituloSistema, BorderLayout.WEST
        );

        encabezado.add(
                responsable, BorderLayout.EAST
        );

        principal.add(
                encabezado, BorderLayout.NORTH
        );

        // MENÚ LATERAL
        JPanel lateral = new JPanel();

        lateral.setBackground(NARANJA_OSCURO);

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

        JButton btnInicio =
                crearBotonLateral("Inicio");

        JButton btnCandidatos =
                crearBotonLateral("Candidatos");

        JButton btnParametros =
                crearBotonLateral("Parámetros");

        JButton btnProcesos =
                crearBotonLateral("Procesos");

        JButton btnReportes =
                crearBotonLateral("Reportes");

        JButton btnRegresar =
                crearBotonLateral("Regresar al menú");

        JButton[] botones = {
            btnInicio,
            btnCandidatos,
            btnParametros,
            btnProcesos,
            btnReportes
        };

        for (JButton boton : botones) {

            lateral.add(boton);

            lateral.add(
                    Box.createVerticalStrut(8)
            );
        }

        lateral.add(Box.createVerticalGlue());

        lateral.add(new JSeparator());

        lateral.add(
                Box.createVerticalStrut(12)
        );

        lateral.add(btnRegresar);

        principal.add(
                lateral, BorderLayout.WEST
        );

        // CONTENIDO CENTRAL
        JPanel derecha =
                new JPanel(new BorderLayout());

        derecha.setBackground(FONDO);

        panelCentral =
                new JPanel(new BorderLayout());

        panelCentral.setBackground(FONDO);

        derecha.add(
                panelCentral, BorderLayout.CENTER
        );

        // BARRA INFERIOR
        JPanel pie = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT, 20, 10
                )
        );

        pie.setBackground(NARANJA_PASTEL);

        JLabel textoPie = new JLabel(
                "Sistema de Gestión de Papelería - Selección"
        );

        textoPie.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        textoPie.setForeground(NARANJA_OSCURO);

        pie.add(textoPie);

        derecha.add(
                pie, BorderLayout.SOUTH
        );

        principal.add(
                derecha, BorderLayout.CENTER
        );

        setContentPane(principal);

        // FUNCIONES DE LOS BOTONES
        btnInicio.addActionListener(
                e -> mostrarInicio()
        );

        btnCandidatos.addActionListener(
                e -> abrirVentana(new VistaCandidatos())
        );

        btnParametros.addActionListener(
                e -> abrirVentana(new VistaParametros())
        );

        btnProcesos.addActionListener(
                e -> abrirVentana(new VistaProceso())
        );

        btnReportes.addActionListener(
                e -> abrirVentana(new VistaReportes())
        );

        btnRegresar.addActionListener(
                e -> dispose()
        );

        mostrarInicio();
    }

    // BOTONES DEL MENÚ LATERAL
    private JButton crearBotonLateral(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        boton.setForeground(Color.WHITE);

        boton.setBackground(NARANJA_OSCURO);

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
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        boton.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        boton.setBackground(NARANJA);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        boton.setBackground(NARANJA_OSCURO);
                    }
                }
        );

        return boton;
    }

    // PANTALLA DE INICIO
    private void mostrarInicio() {

        panelCentral.removeAll();

        JPanel inicio =
                new JPanel(new BorderLayout(0, 25));

        inicio.setBackground(FONDO);

        inicio.setBorder(
                new EmptyBorder(35, 35, 35, 35)
        );

        JPanel cabecera = new JPanel();

        cabecera.setOpaque(false);

        cabecera.setLayout(
                new BoxLayout(
                        cabecera,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo =
                new JLabel("Selección de Personal");

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        titulo.setForeground(NARANJA_OSCURO);

        JLabel subtitulo = new JLabel(
                "Administra candidatos, evaluaciones, procesos y reportes."
        );

        subtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        subtitulo.setForeground(GRIS);

        cabecera.add(titulo);

        cabecera.add(
                Box.createVerticalStrut(8)
        );

        cabecera.add(subtitulo);

        inicio.add(
                cabecera, BorderLayout.NORTH
        );

        // CUATRO TARJETAS
        JPanel tarjetas = new JPanel(
                new GridLayout(2, 2, 18, 18)
        );

        tarjetas.setOpaque(false);

        tarjetas.add(
                crearTarjeta(
                        "Candidatos",
                        "Registrar, actualizar y",
                        "consultar postulantes.",
                        () -> abrirVentana(
                                new VistaCandidatos()
                        )
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Parámetros",
                        "Configurar criterios",
                        "de evaluación.",
                        () -> abrirVentana(
                                new VistaParametros()
                        )
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Procesos",
                        "Evaluar candidatos y",
                        "gestionar contrataciones.",
                        () -> abrirVentana(
                                new VistaProceso()
                        )
                )
        );

        tarjetas.add(
                crearTarjeta(
                        "Reportes",
                        "Consultar resultados",
                        "y reportes de selección.",
                        () -> abrirVentana(
                                new VistaReportes()
                        )
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

    // TARJETAS SIN BORDES NARANJAS AL PASAR EL MOUSE
    private JPanel crearTarjeta(
            String nombre,
            String descripcion1,
            String descripcion2,
            Runnable accion) {

        JPanel tarjeta = new JPanel();

        tarjeta.setBackground(Color.WHITE);

        tarjeta.setLayout(
                new BoxLayout(
                        tarjeta,
                        BoxLayout.Y_AXIS
                )
        );

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(230, 225, 220),
                                1,
                                true
                        ),
                        new EmptyBorder(20, 12, 20, 12)
                )
        );

        tarjeta.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        JLabel titulo = new JLabel(nombre);

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 18)
        );

        titulo.setForeground(NARANJA_OSCURO);

        titulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel linea1 =
                new JLabel(descripcion1);

        JLabel linea2 =
                new JLabel(descripcion2);

        for (JLabel linea :
                new JLabel[]{linea1, linea2}) {

            linea.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            12
                    )
            );

            linea.setForeground(GRIS);

            linea.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );
        }

        tarjeta.add(Box.createVerticalGlue());

        tarjeta.add(titulo);

        tarjeta.add(
                Box.createVerticalStrut(12)
        );

        tarjeta.add(linea1);

        tarjeta.add(linea2);

        tarjeta.add(Box.createVerticalGlue());

        // SOLO EJECUTA LA ACCIÓN.
        // NO CAMBIA COLORES NI BORDES.
        MouseAdapter evento =
                new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        accion.run();
                    }
                };

        tarjeta.addMouseListener(evento);

        for (Component componente :
                tarjeta.getComponents()) {

            componente.addMouseListener(evento);
        }

        return tarjeta;
    }

    // ABRIR FORMULARIOS ORIGINALES
    private void abrirVentana(JFrame ventana) {

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setLocationRelativeTo(this);

        ventana.setVisible(true);
    }

    // MÉTODO PRINCIPAL
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new FrmMenuSeleccion().setVisible(true);

        });
    }
}
