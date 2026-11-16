
package com.papeleria.presentacion;

import com.papeleria.persistencia.UsuarioDAO;

import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JCheckBox chkMostrar;

    private static final Color AZUL =
            new Color(22, 136, 232);

    private static final Color AZUL_OSCURO =
            new Color(23, 63, 115);

    private static final Color CELESTE =
            new Color(217, 240, 250);

    public VentanaLogin() {

        setTitle("Sistema de Papelería - Iniciar sesión");
        setSize(900, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        crearInterfaz();
    }

    private void crearInterfaz() {

        JPanel principal = new JPanel(
                new GridLayout(1, 2)
        );

        principal.setBackground(Color.WHITE);

        // PANEL IZQUIERDO
        JPanel izquierdo = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                GradientPaint degradado =
                        new GradientPaint(
                                0, 0,
                                new Color(204, 236, 251),
                                0, getHeight(),
                                new Color(156, 212, 242)
                        );

                g2.setPaint(degradado);

                g2.fillRect(
                        0, 0,
                        getWidth(), getHeight()
                );

                // CÍRCULOS DECORATIVOS
                g2.setColor(
                        new Color(255, 255, 255, 90)
                );

                g2.fillOval(-70, -60, 240, 240);
                g2.fillOval(270, 340, 250, 250);

                // CUADERNO
                g2.setColor(Color.WHITE);

                g2.fillRoundRect(
                        145, 110, 160, 190, 18, 18
                );

                g2.setColor(
                        new Color(70, 143, 199)
                );

                g2.setStroke(new BasicStroke(3));

                g2.drawRoundRect(
                        145, 110, 160, 190, 18, 18
                );

                // LÍNEAS DEL CUADERNO
                g2.setColor(
                        new Color(170, 211, 234)
                );

                for (int y = 150; y <= 265; y += 23) {

                    g2.drawLine(
                            165, y, 285, y
                    );
                }

                // LÁPIZ
                g2.rotate(
                        Math.toRadians(-25),
                        330, 210
                );

                g2.setColor(
                        new Color(255, 190, 76)
                );

                g2.fillRoundRect(
                        320, 125, 25, 170, 8, 8
                );

                g2.setColor(
                        new Color(232, 118, 135)
                );

                g2.fillRect(
                        320, 125, 25, 22
                );

                g2.setColor(
                        new Color(92, 67, 51)
                );

                Polygon punta = new Polygon();

                punta.addPoint(320, 295);
                punta.addPoint(345, 295);
                punta.addPoint(332, 320);

                g2.fillPolygon(punta);

                g2.dispose();
            }
        };

        izquierdo.setLayout(
                new BorderLayout()
        );

        izquierdo.setBackground(CELESTE);

        JPanel textos = new JPanel();

        textos.setOpaque(false);

        textos.setLayout(
                new BoxLayout(
                        textos,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo =
                new JLabel("PAPELERÍA");

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );

        lblTitulo.setForeground(AZUL_OSCURO);

        lblTitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        JLabel lblSubtitulo = new JLabel(
                "Sistema de Gestión de Papelería"
        );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
        );

        lblSubtitulo.setForeground(
                new Color(49, 98, 139)
        );

        lblSubtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        textos.add(lblTitulo);

        textos.add(
                Box.createVerticalStrut(8)
        );

        textos.add(lblSubtitulo);

        textos.setBorder(
                new EmptyBorder(0, 10, 65, 10)
        );

        izquierdo.add(
                textos,
                BorderLayout.SOUTH
        );

        // PANEL DERECHO
        JPanel derecho = new JPanel(
                new GridBagLayout()
        );

        derecho.setBackground(Color.WHITE);

        JPanel formulario = new JPanel();

        formulario.setOpaque(false);

        formulario.setLayout(
                new BoxLayout(
                        formulario,
                        BoxLayout.Y_AXIS
                )
        );

        formulario.setPreferredSize(
                new Dimension(320, 350)
        );

        // TÍTULO DEL LOGIN
        JLabel lblBienvenida =
                new JLabel("Iniciar sesión");

        lblBienvenida.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        lblBienvenida.setForeground(
                AZUL_OSCURO
        );

        lblBienvenida.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel lblDescripcion = new JLabel(
                "Ingresa tus credenciales para continuar"
        );

        lblDescripcion.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        lblDescripcion.setForeground(
                new Color(125, 135, 150)
        );

        lblDescripcion.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // CAMPO USUARIO
        JLabel lblUsuario =
                new JLabel("Usuario");

        lblUsuario.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        txtUsuario = new JTextField();

        txtUsuario.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        txtUsuario.setMaximumSize(
                new Dimension(320, 43)
        );

        txtUsuario.setPreferredSize(
                new Dimension(320, 43)
        );

        // CAMPO CONTRASEÑA
        JLabel lblClave =
                new JLabel("Contraseña");

        lblClave.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        txtContrasena =
                new JPasswordField();

        txtContrasena.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        txtContrasena.setMaximumSize(
                new Dimension(320, 43)
        );

        txtContrasena.setPreferredSize(
                new Dimension(320, 43)
        );

        // MOSTRAR CONTRASEÑA
        chkMostrar = new JCheckBox(
                "Mostrar contraseña"
        );

        chkMostrar.setOpaque(false);

        chkMostrar.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        char caracterOriginal =
                txtContrasena.getEchoChar();

        chkMostrar.addActionListener(e -> {

            if (chkMostrar.isSelected()) {

                txtContrasena.setEchoChar(
                        (char) 0
                );

            } else {

                txtContrasena.setEchoChar(
                        caracterOriginal
                );
            }
        });

        // BOTÓN INICIAR SESIÓN
        btnIngresar = new JButton(
                "INICIAR SESIÓN"
        );

        btnIngresar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        btnIngresar.setForeground(
                Color.WHITE
        );

        btnIngresar.setBackground(AZUL);

        btnIngresar.setFocusPainted(false);

        btnIngresar.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btnIngresar.setMaximumSize(
                new Dimension(320, 45)
        );

        btnIngresar.setPreferredSize(
                new Dimension(320, 45)
        );

        btnIngresar.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        // EVENTOS
        btnIngresar.addActionListener(
                e -> iniciarSesion()
        );

        txtContrasena.addActionListener(
                e -> iniciarSesion()
        );

        // ORGANIZACIÓN DEL FORMULARIO
        formulario.add(lblBienvenida);

        formulario.add(
                Box.createVerticalStrut(8)
        );

        formulario.add(lblDescripcion);

        formulario.add(
                Box.createVerticalStrut(32)
        );

        formulario.add(lblUsuario);

        formulario.add(
                Box.createVerticalStrut(8)
        );

        formulario.add(txtUsuario);

        formulario.add(
                Box.createVerticalStrut(18)
        );

        formulario.add(lblClave);

        formulario.add(
                Box.createVerticalStrut(8)
        );

        formulario.add(txtContrasena);

        formulario.add(
                Box.createVerticalStrut(10)
        );

        formulario.add(chkMostrar);

        formulario.add(
                Box.createVerticalStrut(25)
        );

        formulario.add(btnIngresar);

        derecho.add(formulario);

        principal.add(izquierdo);
        principal.add(derecho);

        setContentPane(principal);
    }

    // VALIDAR USUARIO Y CONTRASEÑA EN ORACLE
    private void iniciarSesion() {

        String usuario =
                txtUsuario.getText().trim();

        char[] contrasena =
                txtContrasena.getPassword();

        try {

            if (usuario.isEmpty()
                    || contrasena.length == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese su usuario y contraseña.",
                        "Campos obligatorios",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            btnIngresar.setEnabled(false);

            UsuarioDAO dao = new UsuarioDAO();

            boolean valido = dao.validarLogin(
                    usuario,
                    contrasena
            );

            if (valido) {

                // ABRIR EL MENÚ PRINCIPAL
                MenuPrincipal menu =
                        new MenuPrincipal(usuario);

                menu.setVisible(true);

                // CERRAR VENTANA LOGIN
                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Usuario o contraseña incorrectos.",
                        "Acceso denegado",
                        JOptionPane.ERROR_MESSAGE
                );

                txtContrasena.setText("");

                txtContrasena.requestFocus();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo validar el inicio de sesión.\n"
                    + e.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            Arrays.fill(contrasena, '\0');

            btnIngresar.setEnabled(true);
        }
    }

    // EJECUTAR LOGIN
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            VentanaLogin ventana =
                    new VentanaLogin();

            ventana.setVisible(true);

        });
    }
}
