package com.mycompany.clientefacturacion;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class FrmReporte1 extends javax.swing.JFrame {

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

    public FrmReporte1() {
        setTitle("Facturación - Reporte de Ventas");
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
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

        JLabel titulo = new JLabel("Reporte de Ventas por Ciudad");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(VERDE_CABECERA);
        
        JLabel subtitulo = new JLabel("Consulta los ingresos generados por cada ciudad en un rango de fechas.");
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
        panelFiltros.add(Box.createHorizontalStrut(20)); // Espacio extra
        panelFiltros.add(btnGenerar);

        JPanel contenedorCentro = new JPanel(new BorderLayout());
        contenedorCentro.setBackground(FONDO);
        contenedorCentro.add(panelFiltros, BorderLayout.NORTH);

        // ==========================================
        // 3. TABLA DE RESULTADOS
        // ==========================================
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(FONDO);
        panelTabla.setBorder(new EmptyBorder(0, 30, 20, 30));

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modelo.addColumn("Ciudad");
        modelo.addColumn("Dólares Vendidos");

        tblReporte = new JTable(modelo);
        tblReporte.setRowHeight(40);
        tblReporte.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblReporte.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblReporte.setShowVerticalLines(false);
        tblReporte.setGridColor(new Color(232, 239, 247));
        
        // Estilo del encabezado
        tblReporte.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblReporte.getTableHeader().setBackground(new Color(240, 246, 252));
        tblReporte.getTableHeader().setForeground(AZUL_OSCURO);
        tblReporte.getTableHeader().setPreferredSize(new Dimension(0, 45));
        
        // Ancho de columnas
        tblReporte.getColumnModel().getColumn(0).setPreferredWidth(300);
        tblReporte.getColumnModel().getColumn(1).setPreferredWidth(200);

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
    // 6. MÉTODOS DE LÓGICA
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

            String sql = "SELECT c.nombre AS ciudad, SUM(f.total) AS total_vendido " +
                         "FROM jennifer.FACTURA_CAB f " +
                         "JOIN jennifer.CIUDAD c ON f.ciudad_codigo = c.codigo " +
                         "WHERE f.fecha BETWEEN ? AND ? " +
                         "GROUP BY c.nombre ORDER BY c.nombre";
                         
            java.sql.PreparedStatement pst = con.prepareStatement(sql);
            pst.setDate(1, sqlDesde);
            pst.setDate(2, sqlHasta);
            java.sql.ResultSet rs = pst.executeQuery();

            modelo.setRowCount(0); // Limpiar tabla

            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("ciudad");
                fila[1] = "$ " + rs.getDouble("total_vendido"); // Agregamos el signo $ para que se vea mejor
                modelo.addRow(fila);
            }
            con.close();
            
        } catch (java.text.ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error: Use el formato de fecha dd/MM/yyyy (ejemplo: 10/11/2026)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en el reporte: " + e.getMessage());
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
            new FrmReporte1().setVisible(true);
        });
    }
}