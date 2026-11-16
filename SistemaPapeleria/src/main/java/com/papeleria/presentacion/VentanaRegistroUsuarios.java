
package com.papeleria.presentacion;

import com.papeleria.persistencia.UsuarioDAO;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class VentanaRegistroUsuarios extends JDialog {
    private final JTextField txtNombre = new JTextField(24);
    private final JTextField txtUsuario = new JTextField(24);
    private final JPasswordField txtClave = new JPasswordField(24);
    private final JPasswordField txtConfirmar = new JPasswordField(24);
    private final JComboBox<String> cmbRol =
            new JComboBox<>(new String[]{"ADMINISTRADOR"});
    private final JComboBox<String> cmbEstado =
            new JComboBox<>(new String[]{"ACTIVO", "INACTIVO"});
    private final UsuarioDAO dao = new UsuarioDAO();

    public VentanaRegistroUsuarios(Frame propietario) {
        super(propietario,
                "Sistema de Papelería - Registro de usuarios", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(570, 480);
        setMinimumSize(new Dimension(520, 440));
        setLocationRelativeTo(propietario);
        construir();
    }

    public VentanaRegistroUsuarios() {
        this((Frame) null);
    }

    private void construir() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(248, 250, 253));
        panel.setBorder(new EmptyBorder(20, 28, 20, 28));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 5, 7, 5);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;

        JLabel titulo = new JLabel(
                "Registro de usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 23));
        titulo.setForeground(new Color(8, 45, 85));
        panel.add(titulo, g);

        g.gridwidth = 1;
        agregar(panel, g, 1, "Nombre completo:", txtNombre);
        agregar(panel, g, 2, "Usuario:", txtUsuario);
        agregar(panel, g, 3, "Contraseña:", txtClave);
        agregar(panel, g, 4, "Confirmar contraseña:", txtConfirmar);
        agregar(panel, g, 5, "Rol:", cmbRol);
        agregar(panel, g, 6, "Estado:", cmbEstado);

        JPanel botones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));
        botones.setOpaque(false);

        JButton guardar = new JButton("Registrar usuario");
        guardar.setBackground(new Color(20, 112, 218));
        guardar.setForeground(Color.WHITE);
        guardar.addActionListener(e -> registrar());

        JButton cancelar = new JButton("Cerrar");
        cancelar.addActionListener(e -> dispose());

        botones.add(cancelar);
        botones.add(guardar);

        g.gridx = 0;
        g.gridy = 7;
        g.gridwidth = 2;
        panel.add(botones, g);

        setContentPane(panel);
        getRootPane().setDefaultButton(guardar);
    }

    private void agregar(
            JPanel panel,
            GridBagConstraints g,
            int fila,
            String etiqueta,
            JComponent campo) {

        g.gridy = fila;
        g.gridx = 0;
        g.weightx = 0;
        panel.add(new JLabel(etiqueta), g);

        g.gridx = 1;
        g.weightx = 1;
        panel.add(campo, g);
    }

    private void registrar() {
        char[] clave = txtClave.getPassword();
        char[] confirmar = txtConfirmar.getPassword();

        try {
            String usuario = txtUsuario.getText().trim();

            if (!Arrays.equals(clave, confirmar)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Las contraseñas no coinciden.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (dao.existeUsuario(usuario)) {
                JOptionPane.showMessageDialog(
                        this,
                        "El usuario ya existe. Elige otro nombre.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            dao.registrarUsuario(
                    usuario,
                    txtNombre.getText(),
                    clave,
                    (String) cmbRol.getSelectedItem(),
                    (String) cmbEstado.getSelectedItem());

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario registrado correctamente en Oracle.");

            txtNombre.setText("");
            txtUsuario.setText("");
            txtClave.setText("");
            txtConfirmar.setText("");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar en Oracle: "
                            + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);

        } finally {
            Arrays.fill(clave, '\0');
            Arrays.fill(confirmar, '\0');
        }
    }
}
