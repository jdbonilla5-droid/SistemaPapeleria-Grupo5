package com.mycompany.clientefacturacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class FrmCliente extends javax.swing.JFrame {

    // Paleta de colores
    private static final Color AZUL_OSCURO = new Color(9, 48, 89);
    private static final Color VERDE_CABECERA = new Color(33, 150, 83);
    private static final Color FONDO = new Color(248, 251, 255);
    private static final Color BLANCO = Color.WHITE;
    private static final Color GRIS = new Color(100, 115, 130);
    private static final Color AZUL_BOTON = new Color(20, 125, 235);
    private static final Color ROJO_BOTON = new Color(210, 55, 65);

    private JTextField txtBuscar;
    private JTable tblClientes;
    private DefaultTableModel modelo;

    public FrmCliente() {
        setTitle("Facturación - Clientes");
        setSize(1050, 650);
        setMinimumSize(new Dimension(950, 580));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        inicializarComponentes();
        cargarClientes();
    }

    private void inicializarComponentes() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        // ==========================================
        // 1. ENCABEZADO (Solo Título y Subtítulo)
        // ==========================================
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(FONDO);
        encabezado.setBorder(new EmptyBorder(20, 30, 10, 30));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Clientes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(VERDE_CABECERA);
        
        JLabel subtitulo = new JLabel("Registro y consulta de clientes.");
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

        // Contenedor de la barra de búsqueda
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

        JButton btnNuevo = new JButton("+ Nuevo Cliente");
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
        // AQUÍ ESTÁ EL CAMBIO DE NOMBRE DE LA COLUMNA
        modelo.addColumn("N°Documento"); 
        modelo.addColumn("Nombre");
        modelo.addColumn("Dirección");
        modelo.addColumn("Acciones");

        tblClientes = new JTable(modelo);
        tblClientes.setRowHeight(45);
        tblClientes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblClientes.setShowVerticalLines(false);
        tblClientes.setGridColor(new Color(232, 239, 247));
        
        // Estilo del encabezado
        tblClientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblClientes.getTableHeader().setBackground(new Color(240, 246, 252));
        tblClientes.getTableHeader().setForeground(AZUL_OSCURO);
        tblClientes.getTableHeader().setPreferredSize(new Dimension(0, 45));
        
        // Ancho de las columnas
        tblClientes.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblClientes.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblClientes.getColumnModel().getColumn(2).setPreferredWidth(250);
        tblClientes.getColumnModel().getColumn(3).setPreferredWidth(150);
        
        // Renderizador para la columna de acciones
        tblClientes.getColumnModel().getColumn(3).setCellRenderer(new RenderAcciones());

        JScrollPane scroll = new JScrollPane(tblClientes);
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
        btnRegresar.setContentAreaFilled(false); // Hace que el botón sea transparente
        btnRegresar.setBorderPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> dispose());
        
        panelPie.add(btnRegresar);
        principal.add(panelPie, BorderLayout.SOUTH);

        setContentPane(principal);

        // ==========================================
        // 5. LÓGICA Y EVENTOS
        // ==========================================
        
        // Filtrar mientras escribe
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cargarClientes(txtBuscar.getText());
            }
        });

        btnNuevo.addActionListener(e -> abrirFormulario(null));

        // Clic en la tabla para Editar o Eliminar
        tblClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblClientes.rowAtPoint(e.getPoint());
                int col = tblClientes.columnAtPoint(e.getPoint());

                if (fila >= 0 && col == 3) {
                    Rectangle cellRect = tblClientes.getCellRect(fila, col, true);
                    int xPos = e.getX() - cellRect.x;

                    String ruc = tblClientes.getValueAt(fila, 0).toString();
                    String nombre = tblClientes.getValueAt(fila, 1).toString();
                    String direccion = tblClientes.getValueAt(fila, 2).toString();

                    // Si el clic fue en la mitad izquierda (Editar) o derecha (Eliminar)
                    if (xPos < cellRect.width / 2) {
                        abrirFormulario(new String[]{ruc, nombre, direccion});
                    } else {
                        eliminarCliente(ruc);
                    }
                }
            }
        });
    }

    // ==========================================
    // 6. MÉTODOS DE BASE DE DATOS
    // ==========================================
    
    private void cargarClientes() { cargarClientes(""); }

    private void cargarClientes(String filtro) {
        try {
            java.sql.Connection con = Conexion.conectar();
            String sql = "SELECT ruc, nombre, direccion FROM jennifer.CLIENTE";
            if (!filtro.isEmpty()) sql += " WHERE ruc LIKE ? OR UPPER(nombre) LIKE ?";
            sql += " ORDER BY nombre";
            
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            if (!filtro.isEmpty()) {
                pst.setString(1, "%" + filtro + "%");
                pst.setString(2, "%" + filtro.toUpperCase() + "%");
            }
            
            java.sql.ResultSet rs = pst.executeQuery();
            modelo.setRowCount(0);

            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getString("ruc");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("direccion");
                fila[3] = "Editar | Eliminar"; // Texto que se reemplaza visualmente
                modelo.addRow(fila);
            }
            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar: " + e.getMessage());
        }
    }

    private void abrirFormulario(String[] datosCliente) {
        boolean esEdicion = (datosCliente != null);

        JDialog dialogo = new JDialog(this, esEdicion ? "Editar Cliente" : "Nuevo Cliente", true);
        dialogo.setSize(400, 280);
        dialogo.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 15));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(BLANCO);

        JTextField txtRuc = new JTextField();
        txtRuc.setEditable(false); // El RUC no se toca
        JTextField txtNombre = new JTextField();
        JTextField txtDir = new JTextField();

        if (esEdicion) {
            txtRuc.setText(datosCliente[0]);
            txtNombre.setText(datosCliente[1]);
            txtDir.setText(datosCliente[2]);
        }

        panel.add(new JLabel("N° Documento (Auto):"));
        panel.add(txtRuc);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDir);

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
            String dir = txtDir.getText().trim();

            if (nombre.isEmpty() || dir.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Llene todos los campos");
                return;
            }

            try {
                java.sql.Connection con = Conexion.conectar();
                if (esEdicion) {
                    String sql = "UPDATE jennifer.CLIENTE SET nombre=?, direccion=? WHERE ruc=?";
                    java.sql.PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, nombre);
                    pst.setString(2, dir);
                    pst.setString(3, txtRuc.getText());
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(dialogo, "Cliente actualizado");
                } else {
                    String sql = "INSERT INTO jennifer.CLIENTE (nombre, direccion) VALUES (?, ?)";
                    java.sql.PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, nombre);
                    pst.setString(2, dir);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(dialogo, "Cliente guardado");
                }
                con.close();
                dialogo.dispose();
                cargarClientes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error: " + ex.getMessage());
            }
        });

        dialogo.setLayout(new BorderLayout());
        dialogo.add(panel, BorderLayout.CENTER);
        dialogo.add(btnPanel, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    private void eliminarCliente(String ruc) {
        int resp = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            try {
                java.sql.Connection con = Conexion.conectar();
                String sql = "DELETE FROM jennifer.CLIENTE WHERE ruc = ?";
                java.sql.PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, ruc);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Cliente eliminado");
                cargarClientes();
                con.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    // Renderizador para pintar las palabras "Editar" y "Eliminar" con colores
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
            new FrmCliente().setVisible(true);
        });
    }
}