package com.mycompany.clientefacturacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FrmCiudad extends javax.swing.JFrame {

    // Paleta de colores del sistema
    private static final Color AZUL_OSCURO = new Color(9, 48, 89);
    private static final Color VERDE_CABECERA = new Color(33, 150, 83);
    private static final Color FONDO = new Color(248, 251, 255);
    private static final Color BLANCO = Color.WHITE;
    private static final Color GRIS = new Color(100, 115, 130);
    private static final Color AZUL_BOTON = new Color(20, 125, 235);
    private static final Color ROJO_BOTON = new Color(210, 55, 65);

    private JTextField txtBuscar;
    private JTable tblCiudades;
    private DefaultTableModel modelo;

    public FrmCiudad() {
        setTitle("Facturación - Ciudades");
        setSize(1000, 600);
        setMinimumSize(new Dimension(900, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        inicializarComponentes();
        cargarCiudades();
    }

    private void inicializarComponentes() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        // ==========================================
        // 1. ENCABEZADO (Título y Subtítulo)
        // ==========================================
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(FONDO);
        encabezado.setBorder(new EmptyBorder(20, 30, 10, 30));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Gestión de Ciudades");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(VERDE_CABECERA);
        
        JLabel subtitulo = new JLabel("Gestiona las ciudades de entrega.");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(GRIS);
        subtitulo.setBorder(new EmptyBorder(5, 0, 0, 0));

        panelTitulos.add(titulo);
        panelTitulos.add(subtitulo);

        encabezado.add(panelTitulos, BorderLayout.WEST);
        principal.add(encabezado, BorderLayout.NORTH);

        // ==========================================
        // 2. PANEL DE BÚSQUEDA Y BOTÓN NUEVO
        // ==========================================
        JPanel panelAcciones = new JPanel(new BorderLayout(15, 0));
        panelAcciones.setBackground(FONDO);
        panelAcciones.setBorder(new EmptyBorder(10, 30, 20, 30));

        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setOpaque(false);
        
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBuscar.setForeground(AZUL_OSCURO);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(214, 230, 244), 1, true),
                new EmptyBorder(10, 15, 10, 15)
        ));

        panelBusqueda.add(lblBuscar, BorderLayout.WEST);
        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

        JButton btnNuevo = new JButton("+ Nueva Ciudad");
        btnNuevo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNuevo.setForeground(BLANCO);
        btnNuevo.setBackground(AZUL_BOTON);
        btnNuevo.setFocusPainted(false);
        btnNuevo.setBorder(new EmptyBorder(10, 20, 10, 20));
        btnNuevo.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelAcciones.add(panelBusqueda, BorderLayout.CENTER);
        panelAcciones.add(btnNuevo, BorderLayout.EAST);

        // ==========================================
        // 3. TABLA DE DATOS
        // ==========================================
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(FONDO);
        panelTabla.setBorder(new EmptyBorder(0, 30, 20, 30));

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelo.addColumn("Código"); 
        modelo.addColumn("Ciudad");
        modelo.addColumn("Acciones");

        tblCiudades = new JTable(modelo);
        tblCiudades.setRowHeight(45);
        tblCiudades.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblCiudades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblCiudades.setShowVerticalLines(false);
        tblCiudades.setGridColor(new Color(232, 239, 247));
        
        // Estilo del encabezado
        tblCiudades.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblCiudades.getTableHeader().setBackground(new Color(240, 246, 252));
        tblCiudades.getTableHeader().setForeground(AZUL_OSCURO);
        tblCiudades.getTableHeader().setPreferredSize(new Dimension(0, 45));
        
        // Ancho de las columnas
        tblCiudades.getColumnModel().getColumn(0).setPreferredWidth(100);
        tblCiudades.getColumnModel().getColumn(1).setPreferredWidth(350);
        tblCiudades.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        // Renderizador para los botones de acción
        tblCiudades.getColumnModel().getColumn(2).setCellRenderer(new RenderAcciones());

        JScrollPane scroll = new JScrollPane(tblCiudades);
        scroll.setBorder(new LineBorder(new Color(214, 230, 244), 1, true));
        scroll.getViewport().setBackground(BLANCO);

        panelTabla.add(scroll, BorderLayout.CENTER);
        
        JPanel contenedorCentro = new JPanel(new BorderLayout());
        contenedorCentro.setBackground(FONDO);
        contenedorCentro.add(panelAcciones, BorderLayout.NORTH);
        contenedorCentro.add(panelTabla, BorderLayout.CENTER);
        principal.add(contenedorCentro, BorderLayout.CENTER);

        // ==========================================
        // 4. PIE DE PÁGINA (Botón Regresar)
        // ==========================================
        JPanel panelPie = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
        panelPie.setBackground(FONDO);
        
        JButton btnRegresar = new JButton("<< Regresar al menú");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegresar.setForeground(AZUL_OSCURO);
        btnRegresar.setContentAreaFilled(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> dispose());
        
        panelPie.add(btnRegresar);
        principal.add(panelPie, BorderLayout.SOUTH);

        setContentPane(principal);

        // ==========================================
        // 5. LÓGICA Y EVENTOS
        // ==========================================
        
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cargarCiudades(txtBuscar.getText());
            }
        });

        btnNuevo.addActionListener(e -> abrirFormulario(null));

        tblCiudades.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblCiudades.rowAtPoint(e.getPoint());
                int col = tblCiudades.columnAtPoint(e.getPoint());

                if (fila >= 0 && col == 2) {
                    Rectangle cellRect = tblCiudades.getCellRect(fila, col, true);
                    int xPos = e.getX() - cellRect.x;

                    String codigo = tblCiudades.getValueAt(fila, 0).toString();
                    String nombre = tblCiudades.getValueAt(fila, 1).toString();

                    if (xPos < cellRect.width / 2) {
                        abrirFormulario(new String[]{codigo, nombre});
                    } else {
                        eliminarCiudad(codigo);
                    }
                }
            }
        });
    }

    // ==========================================
    // 6. MÉTODOS DE BASE DE DATOS
    // ==========================================
    
    private void cargarCiudades() { cargarCiudades(""); }

    private void cargarCiudades(String filtro) {
        try {
            java.sql.Connection con = Conexion.conectar();
            String sql = "SELECT codigo, nombre FROM jennifer.CIUDAD";
            if (!filtro.isEmpty()) sql += " WHERE codigo LIKE ? OR UPPER(nombre) LIKE ?";
            sql += " ORDER BY nombre";
            
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            if (!filtro.isEmpty()) {
                pst.setString(1, "%" + filtro + "%");
                pst.setString(2, "%" + filtro.toUpperCase() + "%");
            }
            
            java.sql.ResultSet rs = pst.executeQuery();
            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString("codigo");
                fila[1] = rs.getString("nombre");
                fila[2] = "Editar | Eliminar"; 
                modelo.addRow(fila);
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + e.getMessage());
        }
    }

    private void abrirFormulario(String[] datosCiudad) {
        boolean esEdicion = (datosCiudad != null);

        JDialog dialogo = new JDialog(this, esEdicion ? "Editar Ciudad" : "Nueva Ciudad", true);
        dialogo.setSize(400, 220);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(BLANCO);

        JTextField txtCodigo = new JTextField();
        txtCodigo.setEditable(false); 
        JTextField txtNombre = new JTextField();

        if (esEdicion) {
            txtCodigo.setText(datosCiudad[0]);
            txtNombre.setText(datosCiudad[1]);
        }

        panel.add(new JLabel("Código (Auto):"));
        panel.add(txtCodigo);
        panel.add(new JLabel("Nombre Ciudad:"));
        panel.add(txtNombre);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(BLANCO);
        
        JButton btnGuardar = new JButton(esEdicion ? "Actualizar" : "Guardar");
        btnGuardar.setBackground(AZUL_BOTON);
        btnGuardar.setForeground(BLANCO);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setBorder(new EmptyBorder(8, 15, 8, 15));
        btnGuardar.setFocusPainted(false);
        
        btnPanel.add(btnGuardar);

        btnGuardar.addActionListener(ev -> {
            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Escriba el nombre de la ciudad");
                return;
            }

            try {
                java.sql.Connection con = Conexion.conectar();
                if (esEdicion) {
                    String sql = "UPDATE jennifer.CIUDAD SET nombre=? WHERE codigo=?";
                    java.sql.PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, nombre);
                    pst.setString(2, txtCodigo.getText());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(dialogo, "Ciudad actualizada");
                } else {
                    String sql = "INSERT INTO jennifer.CIUDAD (nombre) VALUES (?)";
                    java.sql.PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, nombre);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(dialogo, "Ciudad guardada");
                }
                con.close();
                dialogo.dispose();
                cargarCiudades();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error: " + ex.getMessage());
            }
        });

        dialogo.setLayout(new BorderLayout());
        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(btnPanel, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    private void eliminarCiudad(String codigo) {
        int resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar esta ciudad?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                java.sql.Connection con = Conexion.conectar();
                String sql = "DELETE FROM jennifer.CIUDAD WHERE codigo = ?";
                java.sql.PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, codigo);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Ciudad eliminada");
                cargarCiudades();
                con.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar (puede que esté en uso en una factura): " + e.getMessage());
            }
        }
    }

    private class RenderAcciones extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            panel.setBackground(isSelected ? table.getSelectionBackground() : BLANCO);
            
            JLabel edit = new JLabel("Editar");
            edit.setForeground(AZUL_BOTON);
            edit.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JLabel del = new JLabel("Eliminar");
            del.setForeground(ROJO_BOTON);
            del.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            panel.add(edit);
            panel.add(del);
            return panel;
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new FrmCiudad().setVisible(true);
        });
    }
}