
package com.papeleria.presentacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuPrincipal extends JFrame {

    private final String usuario;

    private final Color AZUL_OSCURO = new Color(8, 45, 85);
    private final Color AZUL_LATERAL = new Color(9, 49, 91);
    private final Color CELESTE = new Color(232, 245, 253);
    private final Color FONDO = new Color(248, 250, 253);

    private JPanel contenidoCentral;
    private JLabel lblUsuario;

    public MenuPrincipal(String usuario) {

        this.usuario = usuario;

        setTitle("Sistema de Papelería - Menú principal");
        setSize(1050, 620);
        setMinimumSize(new Dimension(950, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        crearInterfaz();
    }

    private void crearInterfaz() {

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        // ENCABEZADO SUPERIOR
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(CELESTE);
        encabezado.setPreferredSize(new Dimension(1050, 58));
        encabezado.setBorder(new EmptyBorder(0, 22, 0, 25));

        JLabel tituloSistema = new JLabel("Sistema de Papelería");

        tituloSistema.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        tituloSistema.setForeground(AZUL_OSCURO);

        lblUsuario = new JLabel("Bienvenido/a: " + usuario);

        lblUsuario.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        lblUsuario.setForeground(AZUL_OSCURO);

        encabezado.add(tituloSistema, BorderLayout.WEST);
        encabezado.add(lblUsuario, BorderLayout.EAST);

        principal.add(encabezado, BorderLayout.NORTH);

        // MENU LATERAL
        JPanel lateral = new JPanel();

        lateral.setBackground(AZUL_LATERAL);
        lateral.setPreferredSize(new Dimension(205, 0));

        lateral.setLayout(
                new BoxLayout(lateral, BoxLayout.Y_AXIS)
        );

        lateral.setBorder(
                new EmptyBorder(22, 10, 15, 10)
        );

        JButton btnInicio = crearBoton("Inicio");
        JButton btnNomina = crearBoton("Nómina");
        JButton btnFacturacion = crearBoton("Facturación");
        JButton btnContabilidad = crearBoton("Contabilidad");
        JButton btnSeleccion = crearBoton("Selección");

        JButton btnUsuarios = crearBoton("Usuarios");
        JButton btnCerrar = crearBoton("Cerrar sesión");

        // MODULOS PRINCIPALES
        lateral.add(btnInicio);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnNomina);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnFacturacion);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnContabilidad);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnSeleccion);

        // ESPACIO PARA SEPARAR LOS MODULOS
        // DE LAS OPCIONES DE USUARIO
        lateral.add(Box.createVerticalGlue());

        // SEPARADOR
        JSeparator separador = new JSeparator();
        separador.setForeground(Color.WHITE);
        separador.setMaximumSize(
                new Dimension(185, 1)
        );

        lateral.add(separador);
        lateral.add(Box.createVerticalStrut(12));

        // USUARIOS ENCIMA DE CERRAR SESION
        lateral.add(btnUsuarios);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnCerrar);

        principal.add(lateral, BorderLayout.WEST);

        // ZONA CENTRAL
        JPanel zonaDerecha = new JPanel(new BorderLayout());
        zonaDerecha.setBackground(FONDO);

        contenidoCentral = new JPanel(new BorderLayout());
        contenidoCentral.setBackground(FONDO);

        zonaDerecha.add(
                contenidoCentral,
                BorderLayout.CENTER
        );

        // PIE DE PAGINA
        JPanel pie = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 20, 10)
        );

        pie.setBackground(CELESTE);

        JLabel lblPie = new JLabel(
                "Sistema de Gestión de Papelería"
        );

        lblPie.setForeground(AZUL_OSCURO);
        pie.add(lblPie);

        zonaDerecha.add(pie, BorderLayout.SOUTH);

        principal.add(zonaDerecha, BorderLayout.CENTER);

        setContentPane(principal);

        // EVENTOS DE LOS BOTONES

        btnInicio.addActionListener(
                e -> mostrarInicio()
        );

        btnNomina.addActionListener(
                e -> abrirModulo("Nómina")
        );

        btnFacturacion.addActionListener(
                e -> abrirModulo("Facturación")
        );

        btnContabilidad.addActionListener(
                e -> abrirModulo("Contabilidad")
        );

        btnSeleccion.addActionListener(
                e -> abrirModulo("Selección")
        );

        // ABRIR REGISTRO DE USUARIOS
btnUsuarios.addActionListener(e -> {
    VentanaRegistroUsuarios ventana = new VentanaRegistroUsuarios();
    ventana.setVisible(true);
});

        // CERRAR SESION
        btnCerrar.addActionListener(
                e -> cerrarSesion()
        );

        mostrarInicio();
    }

    // CREAR BOTONES LATERALES
    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL_LATERAL);

        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.setMaximumSize(new Dimension(185, 45));
        boton.setPreferredSize(new Dimension(185, 45));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        boton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                boton.setBackground(
                        new Color(23, 80, 136)
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                boton.setBackground(AZUL_LATERAL);
            }
        });

        return boton;
    }

    // MOSTRAR PANTALLA PRINCIPAL
    private void mostrarInicio() {

        contenidoCentral.removeAll();

        JPanel inicio = new JPanel(new BorderLayout(0, 25));
        inicio.setBackground(FONDO);

        inicio.setBorder(
                new EmptyBorder(35, 35, 35, 35)
        );

        JPanel encabezado = new JPanel();

        encabezado.setOpaque(false);

        encabezado.setLayout(
                new BoxLayout(
                        encabezado,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titulo = new JLabel("Menú principal");

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 27)
        );

        titulo.setForeground(AZUL_OSCURO);

        JLabel subtitulo = new JLabel(
                "Selecciona un módulo al que deseas ingresar"
        );

        subtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        subtitulo.setForeground(
                new Color(95, 110, 130)
        );

        encabezado.add(titulo);
        encabezado.add(Box.createVerticalStrut(7));
        encabezado.add(subtitulo);

        inicio.add(encabezado, BorderLayout.NORTH);

        // TARJETAS DE LOS CUATRO MODULOS
        JPanel tarjetas = new JPanel(
                new GridLayout(1, 4, 15, 0)
        );

        tarjetas.setOpaque(false);

        tarjetas.setPreferredSize(
                new Dimension(700, 170)
        );

        tarjetas.add(crearTarjeta(
                "Nómina",
                "Gestión de empleados",
                "y pagos",
                "♟",
                new Color(20, 125, 235)
        ));

        tarjetas.add(crearTarjeta(
                "Facturación",
                "Gestiona ventas",
                "y facturas",
                "$",
                new Color(15, 174, 98)
        ));

        tarjetas.add(crearTarjeta(
                "Contabilidad",
                "Control de ingresos",
                "y egresos",
                "▦",
                new Color(119, 65, 230)
        ));

        tarjetas.add(crearTarjeta(
                "Selección",
                "Candidatos",
                "y evaluaciones",
                "⌕",
                new Color(255, 126, 24)
        ));

        JPanel contenedor = new JPanel(new BorderLayout());

        contenedor.setOpaque(false);

        contenedor.add(tarjetas, BorderLayout.NORTH);

        inicio.add(contenedor, BorderLayout.CENTER);

        contenidoCentral.add(inicio, BorderLayout.CENTER);

        contenidoCentral.revalidate();
        contenidoCentral.repaint();
    }

    // CREAR TARJETAS INTERACTIVAS
    private JPanel crearTarjeta(
            String nombre,
            String descripcion1,
            String descripcion2,
            String icono,
            Color color) {

        JPanel tarjeta = new JPanel();

        tarjeta.setBackground(color);

        tarjeta.setLayout(
                new BoxLayout(tarjeta, BoxLayout.Y_AXIS)
        );

        tarjeta.setBorder(
                new EmptyBorder(17, 5, 15, 5)
        );

        tarjeta.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        JLabel lblIcono = new JLabel(icono);

        lblIcono.setFont(
                new Font("Segoe UI Symbol", Font.BOLD, 34)
        );

        JLabel lblNombre = new JLabel(nombre);

        lblNombre.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        JLabel lblDescripcion1 = new JLabel(descripcion1);
        JLabel lblDescripcion2 = new JLabel(descripcion2);

        lblDescripcion1.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );

        lblDescripcion2.setFont(
                new Font("Segoe UI", Font.PLAIN, 11)
        );

        JLabel[] etiquetas = {
            lblIcono,
            lblNombre,
            lblDescripcion1,
            lblDescripcion2
        };

        for (JLabel etiqueta : etiquetas) {

            etiqueta.setForeground(Color.WHITE);

            etiqueta.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );
        }

        tarjeta.add(lblIcono);
        tarjeta.add(Box.createVerticalStrut(10));

        tarjeta.add(lblNombre);
        tarjeta.add(Box.createVerticalStrut(10));

        tarjeta.add(lblDescripcion1);
        tarjeta.add(lblDescripcion2);

        MouseAdapter evento = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                abrirModulo(nombre);
            }
        };

        tarjeta.addMouseListener(evento);

        for (Component componente :
                tarjeta.getComponents()) {

            componente.addMouseListener(evento);
        }

        return tarjeta;
    }

    // NAVEGACION ENTRE LOS MODULOS
    private void abrirModulo(String modulo) {

        switch (modulo) {

            case "Nómina":

                abrirVentanaExterna(
                        "com.papeleria.nomina.presentacion.MenuPrincipalNomina",
                        "Nómina"
                );

                break;

            case "Facturación":

                abrirVentanaExterna(
                        "com.mycompany.clientefacturacion.FrmMenuFacturacion",
                        "Facturación"
                );

                break;

            case "Selección":

                abrirVentanaExterna(
                        "com.mycompany.proyecto_seleccion_cliente.FrmMenuSeleccion",
                        "Selección"
                );

                break;

            case "Contabilidad":

                abrirVentanaExterna(
                        "com.example.contabilidadpapeleria.MenuContabilidadDiseno",
                        "Contabilidad"
                );

                break;

            default:

                JOptionPane.showMessageDialog(
                        this,
                        "Módulo no disponible: " + modulo,
                        "Sistema de Papelería",
                        JOptionPane.WARNING_MESSAGE
                );

                break;
        }
    }

    // ABRIR VENTANAS EXTERNAS
    private void abrirVentanaExterna(
            String nombreClase,
            String nombreModulo) {

        try {

            Class<?> clase = Class.forName(nombreClase);

            Object objeto;

            try {

                Constructor<?> constructor =
                        clase.getConstructor();

                objeto = constructor.newInstance();

            } catch (NoSuchMethodException ex) {

                Constructor<?> constructor =
                        clase.getConstructor(String.class);

                objeto = constructor.newInstance(usuario);
            }

            if (!(objeto instanceof JFrame)) {

                JOptionPane.showMessageDialog(
                        this,
                        "La ventana principal de "
                        + nombreModulo
                        + " no extiende JFrame.",
                        "Error de integración",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            JFrame ventana = (JFrame) objeto;

            ventana.setDefaultCloseOperation(
                    JFrame.DISPOSE_ON_CLOSE
            );

            ventana.setLocationRelativeTo(this);
            ventana.setVisible(true);

        } catch (Exception | LinkageError e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo abrir el módulo de "
                    + nombreModulo
                    + ".\n\nError: "
                    + e.getClass().getSimpleName()
                    + "\nDetalle: "
                    + e.getMessage()
                    + "\n\nVerifica la dependencia Maven "
                    + "y la clase principal del módulo.",
                    "Error de integración",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // CERRAR SESION
    private void cerrarSesion() {

        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {

            new VentanaLogin().setVisible(true);

            dispose();
        }
    }

    // EJECUTAR MENU PRINCIPAL
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new MenuPrincipal("admin_nomina").setVisible(true);

        });
    }
}
