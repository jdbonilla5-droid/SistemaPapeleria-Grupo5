
package com.papeleria.nomina.presentacion;

import com.papeleria.nomina.negocio.ReporteNominaServicio;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class VentanaReportes extends JFrame {

    // ==========================================
    // COLORES DEL SISTEMA
    // ==========================================

    private static final Color AZUL_OSCURO =
            new Color(9, 48, 89);

    private static final Color AZUL =
            new Color(20, 125, 235);

    private static final Color CELESTE =
            new Color(234, 246, 255);

    private static final Color FONDO =
            new Color(248, 251, 255);

    private static final Color GRIS =
            new Color(100, 115, 130);

    private static final Color VERDE =
            new Color(34, 139, 84);

    private JTextField txtDesde;
    private JTextField txtHasta;
    private TableRowSorter<DefaultTableModel> filtroFechas;
    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;

    private JLabel lblEstado;
    private JLabel lblTotal;
    private JLabel lblReporteActual;

    private final ReporteNominaServicio servicio;

    private String nombreReporteActual = "";

    public VentanaReportes() {

        servicio = new ReporteNominaServicio();

        setTitle(
                "Sistema de Papelería - Reportes de Nómina"
        );

        setSize(1150, 700);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        inicializarComponentes();
    }

    // ==========================================
    // INTERFAZ PRINCIPAL
    // ==========================================

    private void inicializarComponentes() {

        JPanel principal = new JPanel(
                new BorderLayout()
        );

        principal.setBackground(FONDO);

        // ==========================================
        // ENCABEZADO SUPERIOR
        // ==========================================

        JPanel superior = new JPanel(
                new BorderLayout()
        );

        superior.setBackground(CELESTE);

        superior.setPreferredSize(
                new Dimension(1150, 58)
        );

        superior.setBorder(
                new EmptyBorder(0, 24, 0, 24)
        );

        JLabel lblSistema = new JLabel(
                "Sistema de Papelería - Nómina"
        );

        lblSistema.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        lblSistema.setForeground(AZUL_OSCURO);

        JLabel lblSeccion = new JLabel(
                "Gestión de reportes"
        );

        lblSeccion.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        lblSeccion.setForeground(AZUL_OSCURO);

        superior.add(
                lblSistema, BorderLayout.WEST
        );

        superior.add(
                lblSeccion, BorderLayout.EAST
        );

        principal.add(
                superior, BorderLayout.NORTH
        );

        // ==========================================
        // MENÚ LATERAL
        // ==========================================

        JPanel lateral = new JPanel();

        lateral.setBackground(AZUL_OSCURO);

        lateral.setPreferredSize(
                new Dimension(200, 0)
        );

        lateral.setLayout(
                new BoxLayout(
                        lateral, BoxLayout.Y_AXIS
                )
        );

        lateral.setBorder(
                new EmptyBorder(22, 10, 18, 10)
        );

        JButton btnInicio =
                crearBotonLateral("Inicio");

        JButton btnEmpleados =
                crearBotonLateral("Empleados");

        JButton btnNomina =
                crearBotonLateral("Nómina");

        JButton btnMotivos =
                crearBotonLateral("Motivos");

        JButton btnReportes =
                crearBotonLateral("Reportes");

        JButton btnRegresar =
                crearBotonLateral("Regresar al menú");

        lateral.add(btnInicio);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnEmpleados);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnNomina);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnMotivos);
        lateral.add(Box.createVerticalStrut(7));

        lateral.add(btnReportes);

        lateral.add(Box.createVerticalGlue());

        lateral.add(new JSeparator());
        lateral.add(Box.createVerticalStrut(12));

        lateral.add(btnRegresar);

        principal.add(
                lateral, BorderLayout.WEST
        );

        // ==========================================
        // CONTENIDO PRINCIPAL
        // ==========================================

        JPanel centro = new JPanel(
                new BorderLayout(0, 18)
        );

        centro.setBackground(FONDO);

        centro.setBorder(
                new EmptyBorder(25, 30, 20, 30)
        );

        // TÍTULO
        JPanel panelTitulo = new JPanel();

        panelTitulo.setOpaque(false);

        panelTitulo.setLayout(
                new BoxLayout(
                        panelTitulo, BoxLayout.Y_AXIS
                )
        );

        JLabel titulo = new JLabel(
                "Reportes"
        );

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        titulo.setForeground(AZUL_OSCURO);

        JLabel descripcion = new JLabel(
                "Consulta y exporta información del sistema de nómina."
        );

        descripcion.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        descripcion.setForeground(GRIS);

        panelTitulo.add(titulo);
        panelTitulo.add(Box.createVerticalStrut(5));
        panelTitulo.add(descripcion);

        // ==========================================
        // TARJETAS DE REPORTES
        // ==========================================

        JPanel tarjetas = new JPanel(
                new GridLayout(1, 2, 18, 0)
        );

        tarjetas.setOpaque(false);

        tarjetas.setPreferredSize(
                new Dimension(0, 125)
        );

        JPanel tarjetaNomina =
                crearTarjeta(
                        "Reporte de Nómina",
                        "Consulta los registros generales "
                        + "de nómina."
                );

        JPanel tarjetaMotivos =
                crearTarjeta(
                        "Reporte de Motivos",
                        "Consulta los motivos de ingresos "
                        + "y egresos."
                );

        tarjetas.add(tarjetaNomina);
        tarjetas.add(tarjetaMotivos);

        JPanel panelSuperior = new JPanel(
                new BorderLayout(0, 18)
        );

        panelSuperior.setOpaque(false);

        panelSuperior.add(
                panelTitulo, BorderLayout.NORTH
        );

        panelSuperior.add(
                tarjetas, BorderLayout.CENTER
        );

        centro.add(
                panelSuperior, BorderLayout.NORTH
        );

        // ==========================================
        // TABLA DE RESULTADOS
        // ==========================================

        JPanel panelResultados = new JPanel(
                new BorderLayout(0, 12)
        );

        panelResultados.setBackground(Color.WHITE);

        panelResultados.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(214, 230, 244),
                                1,
                                true
                        ),
                        new EmptyBorder(16, 16, 16, 16)
                )
        );

        JPanel barraResultados = new JPanel(
                new BorderLayout()
        );

        barraResultados.setOpaque(false);

        lblReporteActual = new JLabel(
                "Resultados del reporte"
        );

        lblReporteActual.setFont(
                new Font("Segoe UI", Font.BOLD, 17)
        );

        lblReporteActual.setForeground(AZUL_OSCURO);

        JButton btnExportar =
                crearBotonAccion(
                        "Exportar a Excel (CSV)",
                        VERDE
                );

        barraResultados.add(
                lblReporteActual, BorderLayout.WEST
        );

        JButton btnImprimir = crearBotonAccion("Imprimir", AZUL);
        JPanel botonesReporte = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesReporte.setOpaque(false);
        botonesReporte.add(btnImprimir);
        botonesReporte.add(btnExportar);
        barraResultados.add(botonesReporte, BorderLayout.EAST);
        btnImprimir.addActionListener(e -> imprimirReporte());

        JPanel encabezadoResultados = new JPanel(new BorderLayout(0, 10));
        encabezadoResultados.setOpaque(false);
        encabezadoResultados.add(barraResultados, BorderLayout.NORTH);
        JPanel panelFechas = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelFechas.setOpaque(false);
        txtDesde = new JTextField(10);
        txtHasta = new JTextField(10);
        txtDesde.setToolTipText("AAAA-MM-DD");
        txtHasta.setToolTipText("AAAA-MM-DD");
        JButton btnFiltrar = crearBotonAccion("Filtrar fechas", AZUL);
        JButton btnQuitarFiltro = crearBotonAccion("Quitar filtro", AZUL_OSCURO);
        panelFechas.add(new JLabel("Desde (AAAA-MM-DD):"));
        panelFechas.add(txtDesde);
        panelFechas.add(new JLabel("Hasta (AAAA-MM-DD):"));
        panelFechas.add(txtHasta);
        panelFechas.add(btnFiltrar);
        panelFechas.add(btnQuitarFiltro);
        btnFiltrar.addActionListener(e -> aplicarFiltroFechas());
        btnQuitarFiltro.addActionListener(e -> {
            txtDesde.setText("");
            txtHasta.setText("");
            if (filtroFechas != null) filtroFechas.setRowFilter(null);
            actualizarConteo();
        });
        encabezadoResultados.add(panelFechas, BorderLayout.SOUTH);
        panelResultados.add(encabezadoResultados, BorderLayout.NORTH);

        modeloTabla = crearModeloVacio();

        tablaReportes = new JTable(modeloTabla);

        tablaReportes.setRowHeight(36);

        tablaReportes.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        tablaReportes.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        tablaReportes.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaReportes.setGridColor(
                new Color(232, 239, 247)
        );

        tablaReportes.setShowVerticalLines(false);

        tablaReportes.setSelectionBackground(
                new Color(225, 240, 255)
        );

        tablaReportes.setSelectionForeground(
                AZUL_OSCURO
        );

        JTableHeader encabezado =
                tablaReportes.getTableHeader();

        encabezado.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        encabezado.setBackground(CELESTE);
        encabezado.setForeground(AZUL_OSCURO);

        encabezado.setPreferredSize(
                new Dimension(0, 42)
        );

        JScrollPane scroll =
                new JScrollPane(tablaReportes);

        scroll.setBorder(
                new LineBorder(
                        new Color(225, 235, 245)
                )
        );

        scroll.getViewport().setBackground(
                Color.WHITE
        );

        panelResultados.add(
                scroll, BorderLayout.CENTER
        );

        // ==========================================
        // INFORMACIÓN INFERIOR
        // ==========================================

        JPanel informacion = new JPanel(
                new BorderLayout()
        );

        informacion.setOpaque(false);

        lblEstado = new JLabel(
                "Seleccione un reporte para consultar."
        );

        lblEstado.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        lblEstado.setForeground(GRIS);

        lblTotal = new JLabel(
                "Total de registros: 0"
        );

        lblTotal.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        lblTotal.setForeground(AZUL_OSCURO);

        informacion.add(
                lblEstado, BorderLayout.WEST
        );

        informacion.add(
                lblTotal, BorderLayout.EAST
        );

        panelResultados.add(
                informacion, BorderLayout.SOUTH
        );

        centro.add(
                panelResultados, BorderLayout.CENTER
        );

        // ==========================================
        // BOTÓN LIMPIAR
        // ==========================================

        JPanel accionesInferiores = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT, 0, 0
                )
        );

        accionesInferiores.setOpaque(false);

        JButton btnLimpiar =
                crearBotonAccion(
                        "Limpiar resultados",
                        AZUL_OSCURO
                );

        accionesInferiores.add(btnLimpiar);

        centro.add(
                accionesInferiores, BorderLayout.SOUTH
        );

        principal.add(
                centro, BorderLayout.CENTER
        );

        // ==========================================
        // PIE DE PÁGINA
        // ==========================================

        JPanel pie = new JPanel(
                new BorderLayout()
        );

        pie.setBackground(CELESTE);

        pie.setPreferredSize(
                new Dimension(0, 35)
        );

        JLabel lblPie = new JLabel(
                "   Sistema de Gestión de Papelería - Reportes"
        );

        lblPie.setForeground(AZUL_OSCURO);

        pie.add(lblPie, BorderLayout.WEST);

        principal.add(
                pie, BorderLayout.SOUTH
        );

        setContentPane(principal);

        // ==========================================
        // EVENTOS
        // ==========================================

        configurarTarjeta(
                tarjetaNomina,
                () -> cargarReporteNomina()
        );

        configurarTarjeta(
                tarjetaMotivos,
                () -> cargarReporteMotivos()
        );

        btnExportar.addActionListener(
                e -> exportarCSV()
        );

        btnLimpiar.addActionListener(
                e -> limpiarTabla()
        );

        btnInicio.addActionListener(
                e -> dispose()
        );

        btnEmpleados.addActionListener(e -> {
            new VentanaEmpleados().setVisible(true);
        });

        btnNomina.addActionListener(e -> {
            new VentanaNomina().setVisible(true);
        });

        btnMotivos.addActionListener(e -> {
            new VentanaMotivos().setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            limpiarTabla();
        });

        btnRegresar.addActionListener(
                e -> dispose()
        );
    }

    // ==========================================
    // TARJETAS
    // ==========================================

    private JPanel crearTarjeta(
            String titulo,
            String descripcion) {

        JPanel tarjeta = new JPanel(
                new BorderLayout(0, 12)
        );

        tarjeta.setBackground(Color.WHITE);

        tarjeta.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(215, 230, 245),
                                1,
                                true
                        ),
                        new EmptyBorder(18, 20, 18, 20)
                )
        );

        JLabel lblTitulo = new JLabel(titulo);

        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 17)
        );

        lblTitulo.setForeground(AZUL_OSCURO);

        JLabel lblDescripcion = new JLabel(
                "<html><body style='width:220px'>"
                + descripcion
                + "</body></html>"
        );

        lblDescripcion.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        lblDescripcion.setForeground(GRIS);

        tarjeta.add(
                lblTitulo, BorderLayout.NORTH
        );

        tarjeta.add(
                lblDescripcion, BorderLayout.CENTER
        );

        tarjeta.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return tarjeta;
    }

    // ==========================================
    // HACER TARJETAS CLICABLES
    // ==========================================

    private void configurarTarjeta(
            JPanel tarjeta,
            Runnable accion) {

        MouseAdapter evento = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(CELESTE);
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

            componente.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );
        }
    }

    // ==========================================
    // BOTONES DEL MENÚ LATERAL
    // ==========================================

    private JButton crearBotonLateral(
            String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL_OSCURO);

        boton.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.setMaximumSize(
                new Dimension(180, 44)
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

    // ==========================================
    // BOTONES DE ACCIÓN
    // ==========================================

    private JButton crearBotonAccion(
            String texto, Color color) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(color);

        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.setBorder(
                new EmptyBorder(11, 16, 11, 16)
        );

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    // ==========================================
    // MODELO VACÍO
    // ==========================================

    private DefaultTableModel crearModeloVacio() {

        return new DefaultTableModel() {

            @Override
            public boolean isCellEditable(
                    int fila, int columna) {

                return false;
            }
        };
    }

    // ==========================================
    // REPORTE DE NÓMINA
    // ==========================================

    private void cargarReporteNomina() {

        try {

            DefaultTableModel modelo =
                    servicio.consultarReporteNomina();

            mostrarReporte(
                    modelo,
                    "Reporte general de Nómina",
                    "reporte_nomina"
            );

        } catch (SQLException e) {

            mostrarError(
                    "Error al consultar nómina: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // REPORTE DE MOTIVOS
    // ==========================================

    private void cargarReporteMotivos() {

        try {

            DefaultTableModel modelo =
                    servicio.consultarReporteMotivos();

            mostrarReporte(
                    modelo,
                    "Reporte de Motivos",
                    "reporte_motivos"
            );

        } catch (SQLException e) {

            mostrarError(
                    "Error al consultar motivos: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // MOSTRAR REPORTE EN TABLA
    // ==========================================

    private void mostrarReporte(
            DefaultTableModel modelo,
            String nombreReporte,
            String nombreArchivo) {

        modeloTabla = modelo;

        tablaReportes.setModel(modeloTabla);
        filtroFechas = new TableRowSorter<>(modeloTabla);
        tablaReportes.setRowSorter(filtroFechas);
        if (!txtDesde.getText().trim().isEmpty() || !txtHasta.getText().trim().isEmpty()) {
            aplicarFiltroFechas();
        }

        nombreReporteActual = nombreArchivo;

        for (int i = 0;
                i < tablaReportes.getColumnCount();
                i++) {

            tablaReportes.getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(160);
        }

        lblReporteActual.setText(nombreReporte);

        lblEstado.setText(
                "Mostrando: " + nombreReporte
        );

        lblTotal.setText(
                "Total de registros: "
                + tablaReportes.getRowCount()
        );

        if (modeloTabla.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "La consulta no devolvió registros.",
                    "Sin resultados",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // ==========================================
    // EXPORTAR A CSV COMPATIBLE CON EXCEL
    // ==========================================

    private void exportarCSV() {

        if (modeloTabla.getColumnCount() == 0
                || tablaReportes.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes consultar un reporte "
                    + "que contenga registros.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser selector = new JFileChooser();

        selector.setDialogTitle(
                "Guardar reporte de Nómina"
        );

        String nombreArchivo =
                nombreReporteActual.isEmpty()
                ? "reporte_nomina"
                : nombreReporteActual;

        selector.setSelectedFile(
                new File(nombreArchivo + ".csv")
        );

        int resultado =
                selector.showSaveDialog(this);

        if (resultado !=
                JFileChooser.APPROVE_OPTION) {

            return;
        }

        File archivo = selector.getSelectedFile();

        if (!archivo.getName().toLowerCase()
                .endsWith(".csv")) {

            archivo = new File(
                    archivo.getAbsolutePath() + ".csv"
            );
        }

        if (archivo.exists()) {

            int confirmar =
                    JOptionPane.showConfirmDialog(
                            this,
                            "El archivo ya existe.\n"
                            + "¿Deseas reemplazarlo?",
                            "Confirmar reemplazo",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (confirmar !=
                    JOptionPane.YES_OPTION) {

                return;
            }
        }

        try (
                BufferedWriter escritor =
                        new BufferedWriter(
                                new OutputStreamWriter(
                                        new FileOutputStream(
                                                archivo
                                        ),
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            // BOM UTF-8 PARA EXCEL
            escritor.write('\uFEFF');

            // ENCABEZADOS
            for (int columna = 0;
                    columna < modeloTabla.getColumnCount();
                    columna++) {

                if (columna > 0) {
                    escritor.write(";");
                }

                escritor.write(
                        escaparCSV(
                                modeloTabla.getColumnName(
                                        columna
                                )
                        )
                );
            }

            escritor.newLine();

            // REGISTROS
            for (int fila = 0;
                    fila < tablaReportes.getRowCount();
                    fila++) {

                for (int columna = 0;
                        columna < modeloTabla.getColumnCount();
                        columna++) {

                    if (columna > 0) {
                        escritor.write(";");
                    }

                    Object valor =
                            modeloTabla.getValueAt(
                                    fila, columna
                            );

                    escritor.write(
                            escaparCSV(
                                    valor == null
                                    ? ""
                                    : valor.toString()
                            )
                    );
                }

                escritor.newLine();
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Reporte exportado correctamente.\n\n"
                    + "Archivo: "
                    + archivo.getAbsolutePath(),
                    "Exportación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            mostrarError(
                    "No se pudo exportar el reporte: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // ESCAPAR VALORES CSV
    // ==========================================

    private String escaparCSV(String valor) {

        if (valor == null) {
            return "";
        }

        if (!valor.isEmpty()
                && "=+-@\t\r".indexOf(
                        valor.charAt(0)
                ) >= 0) {

            valor = "'" + valor;
        }

        String texto =
                valor.replace("\"", "\"\"");

        if (texto.contains(";")
                || texto.contains("\"")
                || texto.contains("\n")
                || texto.contains("\r")) {

            texto = "\"" + texto + "\"";
        }

        return texto;
    }

    // ==========================================
    // LIMPIAR RESULTADOS
    // ==========================================

    private void limpiarTabla() {

        modeloTabla = crearModeloVacio();

        tablaReportes.setModel(modeloTabla);
        tablaReportes.setRowSorter(null);
        filtroFechas = null;
        txtDesde.setText("");
        txtHasta.setText("");

        nombreReporteActual = "";

        lblReporteActual.setText(
                "Resultados del reporte"
        );

        lblEstado.setText(
                "Seleccione un reporte para consultar."
        );

        lblTotal.setText(
                "Total de registros: 0"
        );
    }

    // ==========================================
    // FILTROS POR FECHA E IMPRESIÓN
    // ==========================================

    private void actualizarConteo() {
        lblTotal.setText("Total de registros: " + tablaReportes.getRowCount());
    }

    private LocalDate convertirFecha(Object valor) {
        if (valor == null) return null;
        if (valor instanceof java.sql.Date) return ((java.sql.Date) valor).toLocalDate();
        if (valor instanceof java.sql.Timestamp) return ((java.sql.Timestamp) valor).toLocalDateTime().toLocalDate();
        if (valor instanceof Date) {
            return new java.sql.Date(((Date) valor).getTime()).toLocalDate();
        }
        String t = valor.toString().trim();
        if (t.length() >= 10) {
            try { return LocalDate.parse(t.substring(0, 10)); }
            catch (DateTimeParseException ex) { /* intentar otro formato */ }
        }
        for (String patron : new String[]{"dd/MM/yyyy", "dd-MM-yyyy", "dd/MM/yy"}) {
            try { return LocalDate.parse(t, DateTimeFormatter.ofPattern(patron)); }
            catch (DateTimeParseException ex) { /* siguiente formato */ }
        }
        return null;
    }

    private void aplicarFiltroFechas() {
        if (filtroFechas == null || modeloTabla.getColumnCount() == 0) {
            JOptionPane.showMessageDialog(this, "Primero seleccione un reporte.");
            return;
        }
        int columnaFecha = -1;
        for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
            String nombre = modeloTabla.getColumnName(i).toLowerCase();
            if (nombre.contains("fecha") || nombre.contains("período") || nombre.contains("periodo")) {
                columnaFecha = i;
                break;
            }
        }
        if (columnaFecha < 0) {
            JOptionPane.showMessageDialog(this,
                "Este reporte no contiene una columna de fecha. No se puede filtrar por período.");
            return;
        }
        try {
            String desdeTexto = txtDesde.getText().trim();
            String hastaTexto = txtHasta.getText().trim();
            final LocalDate desde = desdeTexto.isEmpty() ? null : LocalDate.parse(desdeTexto);
            final LocalDate hasta = hastaTexto.isEmpty() ? null : LocalDate.parse(hastaTexto);
            if (desde != null && hasta != null && desde.isAfter(hasta)) {
                JOptionPane.showMessageDialog(this, "La fecha inicial no puede superar la final.");
                return;
            }
            final int indice = columnaFecha;
            filtroFechas.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
                @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                    LocalDate fecha = convertirFecha(entry.getValue(indice));
                    if (fecha == null) return false;
                    return (desde == null || !fecha.isBefore(desde))
                        && (hasta == null || !fecha.isAfter(hasta));
                }
            });
            actualizarConteo();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Use fechas válidas con formato AAAA-MM-DD.");
        }
    }

    private void imprimirReporte() {
        if (tablaReportes.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Primero consulte un reporte con registros.");
            return;
        }
        try {
            tablaReportes.print(JTable.PrintMode.FIT_WIDTH,
                new java.text.MessageFormat(lblReporteActual.getText()),
                new java.text.MessageFormat("Página {0}"));
        } catch (java.awt.print.PrinterException e) {
            mostrarError("No se pudo imprimir: " + e.getMessage());
        }
    }

    // ==========================================
    // MOSTRAR ERRORES
    // ==========================================

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Sistema de Nómina",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ==========================================
    // EJECUTAR VENTANA
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new VentanaReportes().setVisible(true);

        });
    }
}