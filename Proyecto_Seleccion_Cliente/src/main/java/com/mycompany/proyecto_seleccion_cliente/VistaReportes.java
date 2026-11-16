
package com.mycompany.proyecto_seleccion_cliente;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.table.JTableHeader;

public class VistaReportes extends JFrame {

    private final Color NARANJA_PRINCIPAL = new Color(255, 107, 0);
    private final Color NARANJA_DEGRADADO = new Color(255, 145, 56);
    private final Color AZUL_OSCURO = new Color(15, 23, 42);
    private final Color FONDO_APP = new Color(244, 246, 249);
    private final Color TEXTO_OSCURO = new Color(30, 41, 59);
    private final Color VERDE = new Color(34, 139, 84);

    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTable tblReportes;
    private JLabel lblResultados;

    private String reporteActual = "";

    public VistaReportes() {
        setTitle("ERP Papelería | Reportes Analíticos de Selección");
        setSize(1100, 650);
        setMinimumSize(new Dimension(950, 580));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        construirInterfaz();
    }

    private void construirInterfaz() {

        // ENCABEZADO
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setPaint(new GradientPaint(
                        0, 0, NARANJA_PRINCIPAL,
                        getWidth(), 0, NARANJA_DEGRADADO
                ));

                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        header.setPreferredSize(new Dimension(1100, 58));
        header.setBorder(
                BorderFactory.createEmptyBorder(0, 25, 0, 25)
        );

        JLabel lblTitulo = new JLabel(
                "SELECCIÓN  |  REPORTES Y ESTADÍSTICAS"
        );

        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        JButton btnCerrar = new JButton("Volver al Menú");
        btnCerrar.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setBackground(AZUL_OSCURO);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(
                BorderFactory.createEmptyBorder(7, 14, 7, 14)
        );
        btnCerrar.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
        btnCerrar.addActionListener(e -> dispose());

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnCerrar, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // CONTENEDOR PRINCIPAL
        JPanel contenedor = new JPanel(
                new BorderLayout(0, 20)
        );

        contenedor.setBackground(FONDO_APP);
        contenedor.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 30, 30)
        );

        // FILTROS
        JPanel cardFiltros = crearTarjetaBlanca();
        cardFiltros.setLayout(
                new GridLayout(2, 1, 0, 12)
        );

        JPanel filaFechas = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 15, 0)
        );
        filaFechas.setOpaque(false);

        filaFechas.add(
                crearEtiqueta("Desde (AAAA-MM-DD):")
        );

        txtFechaInicio = crearCampoTexto("2026-01-01");
        filaFechas.add(txtFechaInicio);

        filaFechas.add(
                Box.createHorizontalStrut(15)
        );

        filaFechas.add(
                crearEtiqueta("Hasta (AAAA-MM-DD):")
        );

        txtFechaFin = crearCampoTexto("2026-12-31");
        filaFechas.add(txtFechaFin);

        // BOTONES
        JPanel filaBotones = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 0)
        );
        filaBotones.setOpaque(false);

        JButton btnRanking = crearBoton(
                "Generar Ranking de Evaluados",
                NARANJA_PRINCIPAL
        );

        btnRanking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRanking.setBackground(NARANJA_DEGRADADO);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnRanking.setBackground(NARANJA_PRINCIPAL);
            }
        });

        btnRanking.addActionListener(e -> cargarRanking());

        JButton btnMatriz = crearBoton(
                "Generar Matriz Cruzada (PIVOT)",
                AZUL_OSCURO
        );

        btnMatriz.addActionListener(e -> cargarMatriz());

        // NUEVO BOTÓN IMPRIMIR
        JButton btnImprimir = crearBoton(
                "Imprimir reporte",
                VERDE
        );

        btnImprimir.addActionListener(e -> imprimirReporte());

        filaBotones.add(btnRanking);
        filaBotones.add(btnMatriz);
        filaBotones.add(btnImprimir);

        cardFiltros.add(filaFechas);
        cardFiltros.add(filaBotones);

        contenedor.add(cardFiltros, BorderLayout.NORTH);

        // RESULTADOS
        JPanel cardTabla = crearTarjetaBlanca();
        cardTabla.setLayout(
                new BorderLayout(0, 10)
        );

        lblResultados = new JLabel(
                "Resultados de la Consulta Analítica"
        );

        lblResultados.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );
        lblResultados.setForeground(TEXTO_OSCURO);

        cardTabla.add(
                lblResultados,
                BorderLayout.NORTH
        );

        tblReportes = new JTable();
        tblReportes.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        tblReportes.setRowHeight(30);
        tblReportes.setGridColor(
                new Color(241, 245, 249)
        );
        tblReportes.setSelectionBackground(
                new Color(255, 237, 213)
        );
        tblReportes.setAutoResizeMode(
                JTable.AUTO_RESIZE_OFF
        );

        JTableHeader th = tblReportes.getTableHeader();
        th.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        th.setBackground(AZUL_OSCURO);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(
                new Dimension(0, 34)
        );

        JScrollPane scroll = new JScrollPane(tblReportes);
        scroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(226, 232, 240)
                )
        );
        scroll.getViewport().setBackground(Color.WHITE);

        cardTabla.add(scroll, BorderLayout.CENTER);

        JLabel lblPie = new JLabel(
                "Seleccione un reporte para visualizar los resultados."
        );
        lblPie.setForeground(TEXTO_OSCURO);
        lblPie.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        cardTabla.add(lblPie, BorderLayout.SOUTH);

        contenedor.add(cardTabla, BorderLayout.CENTER);
        add(contenedor, BorderLayout.CENTER);
    }

    // VALIDACIÓN DE FECHAS
    private boolean validarFechas() {

        String inicio = txtFechaInicio.getText().trim();
        String fin = txtFechaFin.getText().trim();

        if (inicio.isEmpty() || fin.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debes ingresar ambas fechas.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        try {
            LocalDate fechaInicio = LocalDate.parse(inicio);
            LocalDate fechaFin = LocalDate.parse(fin);

            if (fechaInicio.isAfter(fechaFin)) {
                JOptionPane.showMessageDialog(
                        this,
                        "La fecha inicial no puede ser mayor que la final.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return false;
            }

            return true;

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Formato de fecha incorrecto.\n"
                    + "Utiliza AAAA-MM-DD.\n"
                    + "Ejemplo: 2026-11-16",
                    "Fecha inválida",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
    }

    // REPORTE 1: RANKING DE EVALUADOS
    private void cargarRanking() {

        if (!validarFechas()) {
            return;
        }

        try {
            ReporteServicio.cargarRanking(
                    tblReportes,
                    txtFechaInicio.getText().trim(),
                    txtFechaFin.getText().trim()
            );

            reporteActual = "Ranking de Evaluados";
            actualizarTitulo();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al generar el ranking:\n"
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // REPORTE 2: MATRIZ CRUZADA
    private void cargarMatriz() {

        if (!validarFechas()) {
            return;
        }

        try {
            ReporteServicio.cargarMatriz(
                    tblReportes,
                    txtFechaInicio.getText().trim(),
                    txtFechaFin.getText().trim()
            );

            reporteActual = "Matriz Cruzada (PIVOT)";
            actualizarTitulo();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al generar la matriz:\n"
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ACTUALIZAR TÍTULO DEL REPORTE
    private void actualizarTitulo() {

        lblResultados.setText(
                reporteActual
                + " | Desde: "
                + txtFechaInicio.getText().trim()
                + " | Hasta: "
                + txtFechaFin.getText().trim()
                + " | Registros: "
                + tblReportes.getRowCount()
        );
    }

    // IMPRIMIR REPORTE
    private void imprimirReporte() {

        if (tblReportes.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero debes generar un reporte "
                    + "que contenga registros.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            MessageFormat encabezado = new MessageFormat(
                    "ERP Papelería - Selección - "
                    + reporteActual
            );

            MessageFormat pie = new MessageFormat(
                    "Página {0}"
            );

            boolean impreso = tblReportes.print(
                    JTable.PrintMode.FIT_WIDTH,
                    encabezado,
                    pie
            );

            if (!impreso) {
                JOptionPane.showMessageDialog(
                        this,
                        "La impresión fue cancelada.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo imprimir el reporte:\n"
                    + ex.getMessage(),
                    "Error de impresión",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // TARJETAS BLANCAS
    private JPanel crearTarjetaBlanca() {

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(
                        0, 0,
                        getWidth(),
                        getHeight(),
                        16, 16
                );

                g2.setColor(
                        new Color(226, 232, 240)
                );

                g2.drawRoundRect(
                        0, 0,
                        getWidth() - 1,
                        getHeight() - 1,
                        16, 16
                );

                g2.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 22, 18, 22
                )
        );

        return panel;
    }

    // ETIQUETAS
    private JLabel crearEtiqueta(String texto) {

        JLabel lbl = new JLabel(texto);
        lbl.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );
        lbl.setForeground(
                new Color(71, 85, 105)
        );

        return lbl;
    }

    // CAMPOS DE FECHA
    private JTextField crearCampoTexto(
            String valorDefecto) {

        JTextField txt = new JTextField(valorDefecto);

        txt.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );
        txt.setPreferredSize(
                new Dimension(130, 34)
        );

        txt.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(203, 213, 225),
                                1,
                                true
                        ),
                        BorderFactory.createEmptyBorder(
                                4, 10, 4, 10
                        )
                )
        );

        return txt;
    }

    // BOTONES
    private JButton crearBoton(
            String texto,
            Color color) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        boton.setForeground(Color.WHITE);
        boton.setBackground(color);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 16, 10, 16
                )
        );

        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new VistaReportes().setVisible(true)
        );
    }
}
