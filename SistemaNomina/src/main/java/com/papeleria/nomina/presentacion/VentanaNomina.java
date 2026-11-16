
package com.papeleria.nomina.presentacion;

import com.papeleria.nomina.conexion.ConexionOracle;
import com.papeleria.nomina.negocio.*;
import com.papeleria.nomina.persistencia.IntegracionContabilidadDAO;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class VentanaNomina extends JFrame {

    private static final Color AZUL_OSCURO = new Color(9, 48, 89);
    private static final Color AZUL = new Color(20, 125, 235);
    private static final Color CELESTE = new Color(234, 246, 255);
    private static final Color FONDO = new Color(248, 251, 255);
    private static final Color GRIS = new Color(100, 115, 130);
    private static final Color VERDE = new Color(18, 145, 85);
    private static final Color ROJO = new Color(210, 55, 65);

    private final NominaServicio nominaServicio = new NominaServicio();
    private final MotivoNominaServicio motivoServicio =
            new MotivoNominaServicio();

    private final List<DetalleNomina> detalles = new ArrayList<>();

    private JComboBox<EmpleadoItem> cmbEmpleado;
    private JComboBox<MotivoItem> cmbMotivo;

    private JTextField txtCedulaEmpleado;
    private JLabel lblDatosEmpleado;
    private JTextField txtFechaInicio;
    private JTextField txtFechaFin;
    private JTextField txtValor;
    private JTextField txtObservacion;
    private JTextField txtBuscar;

    private JLabel lblIngresos;
    private JLabel lblEgresos;
    private JLabel lblNeto;

    private JTable tablaDetalles;
    private JTable tablaNominas;

    private DefaultTableModel modeloDetalles;
    private DefaultTableModel modeloNominas;

    private TableRowSorter<DefaultTableModel> ordenador;

    public VentanaNomina() {
        setTitle("Sistema de Papelería - Nómina");
        setSize(1150, 700);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        construirInterfaz();
        cargarNominas();
    }

    // =====================================
    // INTERFAZ PRINCIPAL
    // =====================================

    private void construirInterfaz() {

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(CELESTE);
        superior.setPreferredSize(new Dimension(1150, 58));
        superior.setBorder(new EmptyBorder(0, 24, 0, 24));

        JLabel lblSistema = new JLabel(
                "Sistema de Papelería - Nómina"
        );
        lblSistema.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSistema.setForeground(AZUL_OSCURO);

        JLabel lblSeccion = new JLabel("Gestión de nóminas");
        lblSeccion.setForeground(AZUL_OSCURO);

        superior.add(lblSistema, BorderLayout.WEST);
        superior.add(lblSeccion, BorderLayout.EAST);
        principal.add(superior, BorderLayout.NORTH);

        JPanel lateral = new JPanel();
        lateral.setBackground(AZUL_OSCURO);
        lateral.setPreferredSize(new Dimension(200, 0));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(new EmptyBorder(22, 10, 18, 10));

        JButton btnInicio = crearBotonLateral("Inicio");
        JButton btnEmpleados = crearBotonLateral("Empleados");
        JButton btnNomina = crearBotonLateral("Nómina");
        JButton btnMotivos = crearBotonLateral("Motivos");
        JButton btnReportes = crearBotonLateral("Reportes");
        JButton btnRegresar = crearBotonLateral("Regresar al menú");

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

        principal.add(lateral, BorderLayout.WEST);

        JPanel centro = new JPanel(new BorderLayout(0, 20));
        centro.setBackground(FONDO);
        centro.setBorder(new EmptyBorder(28, 30, 25, 30));

        JPanel cabecera = new JPanel(new BorderLayout(0, 16));
        cabecera.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Nómina");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(AZUL_OSCURO);

        JLabel descripcion = new JLabel(
                "Consulta y administra los registros de nómina."
        );
        descripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descripcion.setForeground(GRIS);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(descripcion);
        cabecera.add(textos, BorderLayout.NORTH);

        JPanel herramientas = new JPanel(new BorderLayout(12, 0));
        herramientas.setOpaque(false);

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBuscar.setToolTipText("Buscar por cédula o ID de empleado");
        txtBuscar.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(210, 225, 240)),
                        new EmptyBorder(10, 12, 10, 12)
                )
        );

        JButton btnBuscar = crearBotonAccion("Buscar", AZUL);
        JButton btnMostrar = crearBotonAccion(
                "Mostrar todos", AZUL_OSCURO
        );
        JButton btnAsiento = crearBotonAccion(
                "Generar asiento contable", VERDE
        );
        JButton btnNueva = crearBotonAccion(
                "+ Nueva nómina", AZUL
        );

        JPanel acciones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        acciones.setOpaque(false);
        acciones.add(btnBuscar);
        acciones.add(btnMostrar);
        acciones.add(btnAsiento);
        acciones.add(btnNueva);

        herramientas.add(txtBuscar, BorderLayout.CENTER);
        herramientas.add(acciones, BorderLayout.EAST);
        cabecera.add(herramientas, BorderLayout.SOUTH);
        centro.add(cabecera, BorderLayout.NORTH);

        modeloNominas = new DefaultTableModel(
                new String[]{
                    "N.º Nómina",
                    "ID Empleado",
                    "Cédula",
                    "Nombres",
                    "Apellidos",
                    "Cargo",
                    "Fecha inicio",
                    "Fecha fin",
                    "Ingresos",
                    "Egresos",
                    "Sueldo neto",
                    "Estado"
                }, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaNominas = new JTable(modeloNominas);
        configurarTabla(tablaNominas);
        tablaNominas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaNominas.getColumnModel().getColumn(3).setPreferredWidth(140);
        tablaNominas.getColumnModel().getColumn(4).setPreferredWidth(140);
        tablaNominas.getColumnModel().getColumn(5).setPreferredWidth(130);

        ordenador = new TableRowSorter<>(modeloNominas);
        tablaNominas.setRowSorter(ordenador);

        JScrollPane scroll = new JScrollPane(tablaNominas);
        scroll.setBorder(
                new LineBorder(new Color(214, 230, 244), 1, true)
        );
        scroll.getViewport().setBackground(Color.WHITE);
        centro.add(scroll, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(CELESTE);
        pie.setPreferredSize(new Dimension(0, 35));

        JLabel lblPie = new JLabel(
                "   Sistema de Gestión de Papelería - Nómina"
        );
        lblPie.setForeground(AZUL_OSCURO);
        pie.add(lblPie, BorderLayout.WEST);

        principal.add(centro, BorderLayout.CENTER);
        principal.add(pie, BorderLayout.SOUTH);
        setContentPane(principal);

        btnNueva.addActionListener(e -> abrirFormularioNomina());
        btnBuscar.addActionListener(e -> filtrarNominas());
        txtBuscar.addActionListener(e -> filtrarNominas());

        btnMostrar.addActionListener(e -> {
            txtBuscar.setText("");
            ordenador.setRowFilter(null);
            cargarNominas();
        });

        btnAsiento.addActionListener(
                e -> comprobarNominaParaAsiento()
        );

        btnInicio.addActionListener(e -> dispose());

        btnEmpleados.addActionListener(e -> {
            new VentanaEmpleados().setVisible(true);
        });

        btnNomina.addActionListener(e -> cargarNominas());

        btnMotivos.addActionListener(e -> {
            new VentanaMotivos().setVisible(true);
        });

        btnReportes.addActionListener(e -> {
            new VentanaReportes().setVisible(true);
        });

        btnRegresar.addActionListener(e -> dispose());
    }

    // =====================================
    // GENERAR ASIENTO CONTABLE EN ORACLE
    // =====================================

    private void comprobarNominaParaAsiento() {

        int fila = tablaNominas.getSelectedRow();

        if (fila < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una nómina de la tabla.",
                    "Generar asiento contable",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int filaModelo = tablaNominas.convertRowIndexToModel(fila);

        int idNomina = Integer.parseInt(
                modeloNominas.getValueAt(filaModelo, 0).toString()
        );

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Desea generar el asiento contable de la nómina N.º "
                        + idNomina + "?",
                "Confirmar integración",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            IntegracionContabilidadDAO dao =
                    new IntegracionContabilidadDAO();

            int idComprobante = dao.generarAsiento(idNomina);

            JOptionPane.showMessageDialog(
                    this,
                    "Asiento contable generado correctamente."
                    + "\nNómina N.º: " + idNomina
                    + "\nComprobante contable N.º: " + idComprobante,
                    "Integración exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException ex) {
            mostrarError(
                    "No se pudo generar el asiento contable:\n"
                    + ex.getMessage()
            );
        }
    }

    // =====================================
    // FORMULARIO PARA REGISTRAR NÓMINA
    // =====================================

    private void abrirFormularioNomina() {

        JDialog dialogo = new JDialog(
                this, "Registrar nueva nómina", true
        );

        dialogo.setSize(1000, 730);
        dialogo.setMinimumSize(new Dimension(900, 650));
        dialogo.setLocationRelativeTo(this);

        JPanel principal = new JPanel(new BorderLayout(0, 14));
        principal.setBackground(FONDO);
        principal.setBorder(new EmptyBorder(18, 22, 18, 22));

        JLabel titulo = new JLabel("Nueva nómina");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(AZUL_OSCURO);
        principal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(214, 230, 244), 1, true
                        ),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        cmbEmpleado = new JComboBox<>();
        txtCedulaEmpleado = new JTextField();
        lblDatosEmpleado = new JLabel(
                "Ingrese una cédula y presione Buscar."
        );
        lblDatosEmpleado.setForeground(AZUL_OSCURO);
        cmbEmpleado.setEnabled(false);

        cmbMotivo = new JComboBox<>();

        txtFechaInicio = new JTextField();
        txtFechaFin = new JTextField();
        txtFechaInicio.setEditable(false);
        txtFechaFin.setEditable(false);

        txtValor = new JTextField();
        txtObservacion = new JTextField();

        cmbEmpleado.setPreferredSize(new Dimension(250, 32));
        cmbMotivo.setPreferredSize(new Dimension(250, 32));
        txtFechaInicio.setPreferredSize(new Dimension(160, 32));
        txtFechaFin.setPreferredSize(new Dimension(160, 32));
        txtValor.setPreferredSize(new Dimension(160, 32));
        txtObservacion.setPreferredSize(new Dimension(250, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // FILA 0: BÚSQUEDA POR CÉDULA

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        formulario.add(
                new JLabel("Cédula del empleado:"), gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;
        formulario.add(txtCedulaEmpleado, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        JButton btnBuscarEmpleado = crearBotonAccion(
                "Buscar empleado", AZUL
        );
        formulario.add(btnBuscarEmpleado, gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        formulario.add(lblDatosEmpleado, gbc);

        btnBuscarEmpleado.addActionListener(
                e -> buscarEmpleadoPorCedula()
        );
        txtCedulaEmpleado.addActionListener(
                e -> buscarEmpleadoPorCedula()
        );

        // FILA 1

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        formulario.add(new JLabel("Empleado:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formulario.add(cmbEmpleado, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        formulario.add(new JLabel("Inicio del período:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        formulario.add(
                crearCampoConCalendario(txtFechaInicio), gbc
        );

        // FILA 2

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.weightx = 0;
        formulario.add(new JLabel("Fin del período:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formulario.add(
                crearCampoConCalendario(txtFechaFin), gbc
        );

        gbc.gridx = 2;
        gbc.weightx = 0;
        formulario.add(new JLabel("Concepto:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        formulario.add(cmbMotivo, gbc);

        // FILA 3

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0;
        formulario.add(new JLabel("Valor ($):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        formulario.add(txtValor, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        formulario.add(new JLabel("Observación:"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 1;
        formulario.add(txtObservacion, gbc);

        // FILA 4

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 0;

        JButton btnAgregar = crearBotonAccion(
                "+ Agregar concepto", AZUL
        );
        formulario.add(btnAgregar, gbc);

        // TABLA DE DETALLES

        modeloDetalles = new DefaultTableModel(
                new String[]{
                    "ID Motivo",
                    "Concepto",
                    "Tipo",
                    "Valor",
                    "Observación"
                }, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaDetalles = new JTable(modeloDetalles);
        configurarTabla(tablaDetalles);

        JPanel panelDetalles = new JPanel(
                new BorderLayout(0, 8)
        );
        panelDetalles.setBackground(Color.WHITE);
        panelDetalles.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(214, 230, 244), 1, true
                        ),
                        new EmptyBorder(10, 10, 10, 10)
                )
        );

        JLabel tituloDetalles = new JLabel(
                "Detalle de ingresos y descuentos"
        );
        tituloDetalles.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );
        tituloDetalles.setForeground(AZUL_OSCURO);

        panelDetalles.add(
                tituloDetalles, BorderLayout.NORTH
        );
        panelDetalles.add(
                new JScrollPane(tablaDetalles),
                BorderLayout.CENTER
        );

        JButton btnEliminar = crearBotonAccion(
                "Eliminar concepto", ROJO
        );

        JPanel panelEliminar = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        panelEliminar.setOpaque(false);
        panelEliminar.add(btnEliminar);

        panelDetalles.add(
                panelEliminar, BorderLayout.SOUTH
        );

        // TOTALES

        JPanel resumen = new JPanel(
                new GridLayout(1, 3, 12, 0)
        );
        resumen.setOpaque(false);

        lblIngresos = crearEtiquetaTotal(
                "Ingresos: $0.00", VERDE
        );
        lblEgresos = crearEtiquetaTotal(
                "Egresos: $0.00", ROJO
        );
        lblNeto = crearEtiquetaTotal(
                "Sueldo neto: $0.00", AZUL_OSCURO
        );

        resumen.add(lblIngresos);
        resumen.add(lblEgresos);
        resumen.add(lblNeto);

        JPanel contenido = new JPanel(
                new BorderLayout(0, 12)
        );
        contenido.setOpaque(false);

        contenido.add(formulario, BorderLayout.NORTH);
        contenido.add(panelDetalles, BorderLayout.CENTER);
        contenido.add(resumen, BorderLayout.SOUTH);

        principal.add(contenido, BorderLayout.CENTER);

        // BOTONES INFERIORES

        JPanel botones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        botones.setOpaque(false);

        JButton btnLimpiar = crearBotonAccion(
                "Limpiar", GRIS
        );
        JButton btnCancelar = crearBotonAccion(
                "Cancelar", AZUL_OSCURO
        );
        JButton btnGuardar = crearBotonAccion(
                "Registrar nómina", AZUL
        );

        botones.add(btnLimpiar);
        botones.add(btnCancelar);
        botones.add(btnGuardar);

        principal.add(botones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarDetalle());
        btnEliminar.addActionListener(e -> eliminarDetalle());
        btnLimpiar.addActionListener(e -> limpiar());
        btnCancelar.addActionListener(e -> dialogo.dispose());

        btnGuardar.addActionListener(e -> {
            if (guardarNomina()) {
                dialogo.dispose();
            }
        });

        dialogo.setContentPane(principal);

        detalles.clear();
        cargarEmpleados();
        cmbEmpleado.setSelectedIndex(-1);
        cargarMotivos();
        actualizarTotales();

        dialogo.setVisible(true);
    }

    // =====================================
    // SELECTOR DE FECHAS
    // =====================================

    private JPanel crearCampoConCalendario(JTextField campo) {

        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);

        campo.setEditable(false);

        JButton boton = new JButton("▼");
        boton.setFocusPainted(false);
        boton.setBackground(CELESTE);
        boton.setForeground(AZUL_OSCURO);

        boton.addActionListener(e -> mostrarCalendario(campo));

        panel.add(campo, BorderLayout.CENTER);
        panel.add(boton, BorderLayout.EAST);

        return panel;
    }

    private void mostrarCalendario(JTextField destino) {

        JDialog calendario = new JDialog(
                this, "Seleccionar fecha del período", true
        );
        calendario.setSize(390, 390);
        calendario.setLocationRelativeTo(this);
        calendario.setLayout(new BorderLayout(8, 8));

        JPanel cabecera = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 10)
        );

        JButton anterior = new JButton("<");
        JButton siguiente = new JButton(">");

        JLabel lblMes = new JLabel();
        lblMes.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );
        lblMes.setForeground(AZUL_OSCURO);

        cabecera.add(anterior);
        cabecera.add(lblMes);
        cabecera.add(siguiente);

        JPanel dias = new JPanel(
                new GridLayout(0, 7, 4, 4)
        );
        dias.setBorder(
                new EmptyBorder(8, 12, 12, 12)
        );

        LocalDate inicial = LocalDate.now();

        try {
            if (!destino.getText().trim().isEmpty()) {
                inicial = LocalDate.parse(
                        destino.getText().trim()
                );
            }
        } catch (Exception ex) {
            inicial = LocalDate.now();
        }

        final YearMonth[] mesActual = {
            YearMonth.from(inicial)
        };

        Runnable actualizar = () -> {

            dias.removeAll();

            YearMonth mes = mesActual[0];

            String[] meses = {
                "Enero", "Febrero", "Marzo", "Abril",
                "Mayo", "Junio", "Julio", "Agosto",
                "Septiembre", "Octubre", "Noviembre",
                "Diciembre"
            };

            lblMes.setText(
                    meses[mes.getMonthValue() - 1]
                    + " " + mes.getYear()
            );

            for (String nombre : new String[]{
                "L", "M", "M", "J", "V", "S", "D"
            }) {

                JLabel etiqueta = new JLabel(
                        nombre, SwingConstants.CENTER
                );

                etiqueta.setFont(
                        new Font("Segoe UI", Font.BOLD, 12)
                );
                etiqueta.setForeground(AZUL_OSCURO);
                dias.add(etiqueta);
            }

            int espacios =
                    mes.atDay(1).getDayOfWeek().getValue() - 1;

            for (int i = 0; i < espacios; i++) {
                dias.add(new JLabel(""));
            }

            for (int dia = 1;
                    dia <= mes.lengthOfMonth();
                    dia++) {

                LocalDate fecha = mes.atDay(dia);

                JButton botonDia = new JButton(
                        String.valueOf(dia)
                );

                botonDia.setFocusPainted(false);
                botonDia.setBackground(
                        fecha.equals(LocalDate.now())
                        ? CELESTE : Color.WHITE
                );
                botonDia.setForeground(AZUL_OSCURO);
                botonDia.setMargin(
                        new Insets(3, 3, 3, 3)
                );

                botonDia.addActionListener(e -> {
                    destino.setText(fecha.toString());
                    calendario.dispose();
                });

                dias.add(botonDia);
            }

            dias.revalidate();
            dias.repaint();
        };

        anterior.addActionListener(e -> {
            mesActual[0] = mesActual[0].minusMonths(1);
            actualizar.run();
        });

        siguiente.addActionListener(e -> {
            mesActual[0] = mesActual[0].plusMonths(1);
            actualizar.run();
        });

        calendario.add(cabecera, BorderLayout.NORTH);
        calendario.add(dias, BorderLayout.CENTER);

        actualizar.run();
        calendario.setVisible(true);
    }

    // =====================================
    // COMPONENTES VISUALES
    // =====================================

    private JButton crearBotonLateral(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );
        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL_OSCURO);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);

        boton.setMaximumSize(new Dimension(180, 44));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        boton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(
                        new Color(22, 83, 145)
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(AZUL_OSCURO);
            }
        });

        return boton;
    }

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

    private JLabel crearEtiquetaTotal(
            String texto, Color color) {

        JLabel etiqueta = new JLabel(
                texto, SwingConstants.CENTER
        );

        etiqueta.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );
        etiqueta.setForeground(color);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(Color.WHITE);

        etiqueta.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(214, 230, 244)
                        ),
                        new EmptyBorder(12, 8, 12, 8)
                )
        );

        return etiqueta;
    }

    private void configurarTabla(JTable tabla) {

        tabla.setRowHeight(36);
        tabla.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );
        tabla.setGridColor(
                new Color(232, 239, 247)
        );
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(
                new Color(225, 240, 255)
        );
        tabla.setSelectionForeground(AZUL_OSCURO);

        JTableHeader encabezado = tabla.getTableHeader();

        encabezado.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        encabezado.setBackground(CELESTE);
        encabezado.setForeground(AZUL_OSCURO);
        encabezado.setPreferredSize(
                new Dimension(0, 40)
        );
    }

    // =====================================
    // CARGAR EMPLEADOS
    // =====================================

    private void cargarEmpleados() {

        String sql =
                "SELECT ID_EMPLEADO, CEDULA, "
                + "NOMBRES, APELLIDOS, CARGO, SUELDO_BASE "
                + "FROM EMPLEADOS "
                + "WHERE UPPER(ESTADO) = 'ACTIVO' "
                + "ORDER BY NOMBRES";

        try (Connection con = ConexionOracle.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            cmbEmpleado.removeAllItems();

            while (rs.next()) {

                cmbEmpleado.addItem(
                        new EmpleadoItem(
                                rs.getInt("ID_EMPLEADO"),
                                rs.getString("CEDULA"),
                                rs.getString("NOMBRES"),
                                rs.getString("APELLIDOS"),
                                rs.getString("CARGO"),
                                rs.getDouble("SUELDO_BASE")
                        )
                );
            }

        } catch (SQLException e) {
            mostrarError(
                    "Error al cargar empleados: "
                    + e.getMessage()
            );
        }
    }

    // =====================================
    // BUSCAR EMPLEADO POR CÉDULA
    // =====================================

    private void buscarEmpleadoPorCedula() {

        String cedula = txtCedulaEmpleado.getText().trim();

        cmbEmpleado.setSelectedIndex(-1);
        lblDatosEmpleado.setText(
                "Empleado no seleccionado"
        );

        if (cedula.isEmpty()) {
            mostrarError(
                    "Ingrese la cédula del empleado."
            );
            return;
        }

        for (int i = 0;
                i < cmbEmpleado.getItemCount();
                i++) {

            EmpleadoItem item = cmbEmpleado.getItemAt(i);

            if (cedula.equals(item.cedula)) {

                cmbEmpleado.setSelectedIndex(i);

                lblDatosEmpleado.setText(
                        "<html>" + item.nombreCompleto
                        + " | " + item.cargo
                        + " | Sueldo base: $"
                        + String.format("%.2f", item.sueldo)
                        + "</html>"
                );
                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "El colaborador buscado no existe o está INACTIVO.",
                "Búsqueda de empleado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================
    // CARGAR MOTIVOS
    // =====================================

    private void cargarMotivos() {

        try {
            cmbMotivo.removeAllItems();

            List<MotivoNomina> lista =
                    motivoServicio.listarTodos();

            for (MotivoNomina motivo : lista) {

                if ("ACTIVO".equalsIgnoreCase(
                        motivo.getEstado())) {

                    cmbMotivo.addItem(
                            new MotivoItem(motivo)
                    );
                }
            }

        } catch (SQLException e) {
            mostrarError(
                    "Error al cargar motivos: "
                    + e.getMessage()
            );
        }
    }

    // =====================================
    // AGREGAR CONCEPTO
    // =====================================

    private void agregarDetalle() {

        MotivoItem item =
                (MotivoItem) cmbMotivo.getSelectedItem();

        if (item == null) {
            mostrarError("Seleccione un concepto.");
            return;
        }

        try {

            double valor = Double.parseDouble(
                    txtValor.getText().trim()
            );

            if (!Double.isFinite(valor) || valor <= 0) {
                throw new NumberFormatException();
            }

            MotivoNomina motivo = item.motivo;

            DetalleNomina detalle = new DetalleNomina();
            detalle.setIdMotivo(motivo.getIdMotivo());
            detalle.setValor(valor);
            detalle.setObservacion(
                    txtObservacion.getText().trim()
            );

            detalles.add(detalle);

            modeloDetalles.addRow(
                    new Object[]{
                        motivo.getIdMotivo(),
                        motivo.getNombre(),
                        motivo.getTipo(),
                        String.format("%.2f", valor),
                        txtObservacion.getText().trim()
                    }
            );

            actualizarTotales();

            txtValor.setText("");
            txtObservacion.setText("");

        } catch (NumberFormatException e) {
            mostrarError(
                    "Ingrese un valor numérico mayor que cero."
            );
        }
    }

    // =====================================
    // ELIMINAR CONCEPTO
    // =====================================

    private void eliminarDetalle() {

        int fila = tablaDetalles.getSelectedRow();

        if (fila < 0) {
            mostrarError(
                    "Seleccione un concepto de la tabla."
            );
            return;
        }

        int indice = tablaDetalles
                .convertRowIndexToModel(fila);

        detalles.remove(indice);
        modeloDetalles.removeRow(indice);

        actualizarTotales();
    }

    // =====================================
    // CALCULAR TOTALES
    // =====================================

    private void actualizarTotales() {

        double ingresos = 0;
        double egresos = 0;

        for (DetalleNomina detalle : detalles) {

            MotivoItem item = buscarMotivo(
                    detalle.getIdMotivo()
            );

            if (item != null) {

                if ("INGRESO".equalsIgnoreCase(
                        item.motivo.getTipo())) {

                    ingresos += detalle.getValor();

                } else if ("EGRESO".equalsIgnoreCase(
                        item.motivo.getTipo())) {

                    egresos += detalle.getValor();
                }
            }
        }

        lblIngresos.setText(
                String.format(
                        "Ingresos: $%.2f", ingresos
                )
        );

        lblEgresos.setText(
                String.format(
                        "Egresos: $%.2f", egresos
                )
        );

        lblNeto.setText(
                String.format(
                        "Sueldo neto: $%.2f",
                        ingresos - egresos
                )
        );
    }

    private MotivoItem buscarMotivo(int id) {

        for (int i = 0;
                i < cmbMotivo.getItemCount();
                i++) {

            MotivoItem item = cmbMotivo.getItemAt(i);

            if (item.motivo.getIdMotivo() == id) {
                return item;
            }
        }

        return null;
    }

    // =====================================
    // REGISTRAR NÓMINA EN ORACLE
    // =====================================

    private boolean guardarNomina() {

        EmpleadoItem empleado =
                (EmpleadoItem) cmbEmpleado.getSelectedItem();

        if (empleado == null) {
            mostrarError(
                    "Busque primero un empleado activo por su cédula."
            );
            return false;
        }

        if (txtFechaInicio.getText().trim().isEmpty()
                || txtFechaFin.getText().trim().isEmpty()) {

            mostrarError(
                    "Ingrese las fechas de inicio y fin."
            );
            return false;
        }

        if (detalles.isEmpty()) {
            mostrarError(
                    "Agregue al menos un concepto de nómina."
            );
            return false;
        }

        try {

            SimpleDateFormat formato =
                    new SimpleDateFormat("yyyy-MM-dd");
            formato.setLenient(false);

            java.util.Date inicio = formato.parse(
                    txtFechaInicio.getText().trim()
            );

            java.util.Date fin = formato.parse(
                    txtFechaFin.getText().trim()
            );

            if (inicio.after(fin)) {
                mostrarError(
                        "La fecha inicial no puede ser posterior a la final."
                );
                return false;
            }

            double ingresos = 0;
            double egresos = 0;

            for (DetalleNomina detalle : detalles) {

                MotivoItem item = buscarMotivo(
                        detalle.getIdMotivo()
                );

                if (item != null) {

                    if ("INGRESO".equalsIgnoreCase(
                            item.motivo.getTipo())) {

                        ingresos += detalle.getValor();

                    } else if ("EGRESO".equalsIgnoreCase(
                            item.motivo.getTipo())) {

                        egresos += detalle.getValor();
                    }
                }
            }

            Nomina nomina = new Nomina();

            nomina.setIdEmpleado(empleado.id);
            nomina.setFechaInicio(inicio);
            nomina.setFechaFin(fin);
            nomina.setFechaPago(null);

            nomina.setTotalIngresos(ingresos);
            nomina.setTotalEgresos(egresos);
            nomina.setSueldoNeto(ingresos - egresos);
            nomina.setEstado("PENDIENTE");

            int id = nominaServicio.registrarNominaConDetalles(
                    nomina,
                    new ArrayList<>(detalles)
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Nómina registrada correctamente."
                    + "\nNúmero de nómina: " + id
                    + "\nEmpleado: " + empleado.nombreCompleto,
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiar();
            cargarNominas();

            return true;

        } catch (java.text.ParseException e) {

            mostrarError(
                    "Las fechas deben tener formato AAAA-MM-DD."
            );
            return false;

        } catch (Exception e) {

            mostrarError(
                    "Error al registrar nómina:\n"
                    + e.getMessage()
            );
            return false;
        }
    }

    // =====================================
    // CONSULTAR NÓMINAS DESDE ORACLE
    // =====================================

    private void cargarNominas() {

        try {

            List<Empleado> empleados =
                    new EmpleadoServicio().listarEmpleados();

            Map<Integer, Empleado> empleadosPorId =
                    new HashMap<>();

            for (Empleado e : empleados) {
                empleadosPorId.put(
                        e.getIdEmpleado(), e
                );
            }

            modeloNominas.setRowCount(0);

            List<Nomina> lista =
                    nominaServicio.listarNominas();

            for (Nomina n : lista) {

                Empleado e = empleadosPorId.get(
                        n.getIdEmpleado()
                );

                modeloNominas.addRow(
                        new Object[]{
                            n.getIdNomina(),
                            n.getIdEmpleado(),
                            e == null ? "" : e.getCedula(),
                            e == null ? "" : e.getNombres(),
                            e == null ? "" : e.getApellidos(),
                            e == null ? "" : e.getCargo(),
                            n.getFechaInicio(),
                            n.getFechaFin(),
                            String.format(
                                    "%.2f",
                                    n.getTotalIngresos()
                            ),
                            String.format(
                                    "%.2f",
                                    n.getTotalEgresos()
                            ),
                            String.format(
                                    "%.2f",
                                    n.getSueldoNeto()
                            ),
                            n.getEstado()
                        }
                );
            }

        } catch (SQLException e) {
            mostrarError(
                    "Error al consultar nóminas: "
                    + e.getMessage()
            );
        }
    }

    // =====================================
    // BUSCAR NÓMINAS
    // =====================================

    private void filtrarNominas() {

        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            ordenador.setRowFilter(null);
            return;
        }

        ordenador.setRowFilter(
                new RowFilter<DefaultTableModel, Integer>() {

                    @Override
                    public boolean include(
                            Entry<? extends DefaultTableModel,
                            ? extends Integer> entry) {

                        return texto.equals(
                                String.valueOf(
                                        entry.getValue(1)
                                ).trim()
                        ) || texto.equals(
                                String.valueOf(
                                        entry.getValue(2)
                                ).trim()
                        );
                    }
                }
        );

        if (tablaNominas.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existen nóminas para el empleado buscado.",
                    "Búsqueda de nóminas",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // =====================================
    // LIMPIAR FORMULARIO
    // =====================================

    private void limpiar() {

        txtCedulaEmpleado.setText("");
        cmbEmpleado.setSelectedIndex(-1);

        lblDatosEmpleado.setText(
                "Ingrese una cédula y presione Buscar."
        );

        txtFechaInicio.setText("");
        txtFechaFin.setText("");
        txtValor.setText("");
        txtObservacion.setText("");

        detalles.clear();
        modeloDetalles.setRowCount(0);

        actualizarTotales();
    }

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "Sistema de Nómina",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =====================================
    // ELEMENTO EMPLEADO
    // =====================================

    private static class EmpleadoItem {

        int id;
        String cedula;
        String nombreCompleto;
        String cargo;
        double sueldo;

        EmpleadoItem(
                int id,
                String cedula,
                String nombres,
                String apellidos,
                String cargo,
                double sueldo) {

            this.id = id;
            this.cedula = cedula;
            this.nombreCompleto =
                    nombres + " " + apellidos;
            this.cargo = cargo;
            this.sueldo = sueldo;
        }

        @Override
        public String toString() {
            return cedula + " - " + nombreCompleto;
        }
    }

    // =====================================
    // ELEMENTO MOTIVO
    // =====================================

    private static class MotivoItem {

        MotivoNomina motivo;

        MotivoItem(MotivoNomina motivo) {
            this.motivo = motivo;
        }

        @Override
        public String toString() {
            return motivo.getNombre()
                    + " - " + motivo.getTipo();
        }
    }

    // =====================================
    // EJECUCIÓN
    // =====================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new VentanaNomina().setVisible(true);
        });
    }
}
