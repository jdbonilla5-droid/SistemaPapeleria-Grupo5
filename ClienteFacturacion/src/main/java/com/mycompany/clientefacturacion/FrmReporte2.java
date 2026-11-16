package com.mycompany.clientefacturacion;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class FrmReporte2 extends javax.swing.JFrame {

    // Paleta de colores
    private static final Color AZUL_OSCURO = new Color(9, 48, 89);
    private static final Color VERDE_CABECERA = new Color(33, 150, 83);
    private static final Color FONDO = new Color(248, 251, 255);
    private static final Color BLANCO = Color.WHITE;
    private static final Color GRIS = new Color(100, 115, 130);
    private static final Color AZUL_BOTON = new Color(20, 125, 235);

    private JTextField txtDesde;
    private JTextField txtHasta;
    private JTable tblReporte;
    private DefaultTableModel modelo;

    public FrmReporte2() {
        setTitle("Facturación - Reporte Cruzado");
        setSize(1050, 650);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        // ==========================================
        // 1. ENCABEZADO (Título)
        // ==========================================
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(FONDO);
        encabezado.setBorder(new EmptyBorder(20, 30, 10, 30));

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Reporte Cruzado: Artículos vs Clientes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(VERDE_CABECERA);
        
        JLabel subtitulo = new JLabel("Consulta los dólares vendidos por artículo y cliente en un rango de fechas.");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(GRIS);
        subtitulo.setBorder(new EmptyBorder(5, 0, 0, 0));

        panelTitulos.add(titulo);
        panelTitulos.add(subtitulo);
        encabezado.add(panelTitulos, BorderLayout.WEST);
        principal.add(encabezado, BorderLayout.NORTH);

        // ==========================================
        // 2. PANEL DE FILTROS Y BOTONES
        // ==========================================
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFiltros.setBackground(FONDO);
        panelFiltros.setBorder(new EmptyBorder(10, 30, 20, 30));

        // Etiqueta y campo "Desde"
        JLabel lblDesde = new JLabel("Desde:");
        lblDesde.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDesde.setForeground(AZUL_OSCURO);
        
        txtDesde = new JTextField(10);
        txtDesde.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDesde.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(214, 230, 244), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        // Etiqueta y campo "Hasta"
        JLabel lblHasta = new JLabel("Hasta:");
        lblHasta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHasta.setForeground(AZUL_OSCURO);
        
        txtHasta = new JTextField(10);
        txtHasta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtHasta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(214, 230, 244), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));

        // Botón Generar
        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGenerar.setForeground(BLANCO);
        btnGenerar.setBackground(AZUL_BOTON);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setBorder(new EmptyBorder(9, 20, 9, 20));
        btnGenerar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelFiltros.add(lblDesde);
        panelFiltros.add(txtDesde);
        panelFiltros.add(lblHasta);
        panelFiltros.add(txtHasta);
        panelFiltros.add(Box.createHorizontalStrut(20));
        panelFiltros.add(btnGenerar);

        JPanel contenedorCentro = new JPanel(new BorderLayout());
        contenedorCentro.setBackground(FONDO);
        contenedorCentro.add(panelFiltros, BorderLayout.NORTH);

        // ==========================================
        // 3. TABLA DE RESULTADOS (MATRIZ)
        // ==========================================
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(FONDO);
        panelTabla.setBorder(new EmptyBorder(0, 30, 20, 30));

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblReporte = new JTable(modelo);
        tblReporte.setRowHeight(40);
        tblReporte.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblReporte.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblReporte.setShowVerticalLines(true); // Mostramos líneas para que se vea como matriz
        tblReporte.setGridColor(new Color(232, 239, 247));
        
        // Estilo del encabezado
        tblReporte.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblReporte.getTableHeader().setBackground(new Color(240, 246, 252));
        tblReporte.getTableHeader().setForeground(AZUL_OSCURO);
        tblReporte.getTableHeader().setPreferredSize(new Dimension(0, 45));

        JScrollPane scroll = new JScrollPane(tblReporte);
        scroll.setBorder(new LineBorder(new Color(214, 230, 244), 1, true));
        scroll.getViewport().setBackground(BLANCO);

        panelTabla.add(scroll, BorderLayout.CENTER);
        contenedorCentro.add(panelTabla, BorderLayout.CENTER);

        principal.add(contenedorCentro, BorderLayout.CENTER);

        // ==========================================
        // 4. PIE DE PÁGINA (Regresar e Imprimir)
        // ==========================================
        JPanel panelPie = new JPanel(new BorderLayout());
        panelPie.setBackground(FONDO);
        panelPie.setBorder(new EmptyBorder(10, 30, 20, 30));

        // Botón Regresar (Izquierda)
        JButton btnRegresar = new JButton("<< Regresar al menú");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegresar.setForeground(AZUL_OSCURO);
        btnRegresar.setContentAreaFilled(false);
        btnRegresar.setBorderPainted(false);
        btnRegresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegresar.addActionListener(e -> dispose());

        // Botón Imprimir (Derecha)
        JButton btnImprimir = new JButton("Imprimir Reporte");
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnImprimir.setForeground(AZUL_OSCURO);
        btnImprimir.setBackground(BLANCO);
        btnImprimir.setFocusPainted(false);
        btnImprimir.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(AZUL_OSCURO, 1, true),
                new EmptyBorder(8, 20, 8, 20)
        ));
        btnImprimir.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelPie.add(btnRegresar, BorderLayout.WEST);
        panelPie.add(btnImprimir, BorderLayout.EAST);
        principal.add(panelPie, BorderLayout.SOUTH);

        setContentPane(principal);

        // ==========================================
        // 5. EVENTOS
        // ==========================================
        btnGenerar.addActionListener(e -> generarReporte());
        btnImprimir.addActionListener(e -> imprimirReporte());
    }

    // ==========================================
    // 6. LÓGICA DEL REPORTE CRUZADO
    // ==========================================

    private void generarReporte() {
        if (txtDesde.getText().isEmpty() || txtHasta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el rango de fechas (dd/MM/yyyy).");
            return;
        }

        try {
            java.sql.Connection con = Conexion.conectar();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            java.sql.Date sqlDesde = new java.sql.Date(sdf.parse(txtDesde.getText()).getTime());
            java.sql.Date sqlHasta = new java.sql.Date(sdf.parse(txtHasta.getText()).getTime());

            // 1. Limpiar modelo anterior
            modelo.setColumnCount(0);
            modelo.setRowCount(0);

            // 2. Agregar columna principal
            modelo.addColumn("Artículo");

            // 3. Obtener clientes (para las columnas)
            java.sql.Statement st = con.createStatement();
            java.sql.ResultSet rsClientes = st.executeQuery("SELECT ruc, nombre FROM jennifer.CLIENTE ORDER BY nombre");
            
            java.util.List<String> listaRucs = new java.util.ArrayList<>();
            while (rsClientes.next()) {
                modelo.addColumn(rsClientes.getString("nombre")); 
                listaRucs.add(rsClientes.getString("ruc")); 
            }

            // 4. Obtener artículos (para las filas)
            java.sql.ResultSet rsArticulos = st.executeQuery("SELECT codigo, nombre FROM jennifer.ARTICULO ORDER BY nombre");
            java.util.List<String[]> listaArticulos = new java.util.ArrayList<>();
            while (rsArticulos.next()) {
                listaArticulos.add(new String[]{rsArticulos.getString("codigo"), rsArticulos.getString("nombre")});
            }

            // 5. Cruzar datos
            for (String[] articulo : listaArticulos) {
                Object[] fila = new Object[listaRucs.size() + 1];
                fila[0] = articulo[1]; // Nombre del artículo

                for (int i = 0; i < listaRucs.size(); i++) {
                    String rucCliente = listaRucs.get(i);
                    
                    String sqlVenta = "SELECT SUM(d.cantidad * d.precio) AS total " +
                                      "FROM jennifer.FACTURA_DET d " +
                                      "JOIN jennifer.FACTURA_CAB c ON d.factura_numero = c.numero " +
                                      "WHERE c.cliente_ruc = ? AND d.articulo_codigo = ? AND c.fecha BETWEEN ? AND ?";
                    
                    java.sql.PreparedStatement pst = con.prepareStatement(sqlVenta);
                    pst.setString(1, rucCliente);
                    pst.setString(2, articulo[0]);
                    pst.setDate(3, sqlDesde);
                    pst.setDate(4, sqlHasta);
                    
                    java.sql.ResultSet rsVenta = pst.executeQuery();
                    if (rsVenta.next()) {
                        double total = rsVenta.getDouble("total");
                        fila[i + 1] = total > 0 ? "$ " + total : "$0.00";
                    } else {
                        fila[i + 1] = "$0.00";
                    }
                    pst.close();
                }
                modelo.addRow(fila);
            }

            con.close();
            
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error: Use el formato de fecha dd/MM/yyyy (ejemplo: 10/11/2026)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en reporte cruzado: " + e.getMessage());
        }
    }

    private void imprimirReporte() {
        try {
            tblReporte.print();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al imprimir: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new FrmReporte2().setVisible(true);
        });
    }
}