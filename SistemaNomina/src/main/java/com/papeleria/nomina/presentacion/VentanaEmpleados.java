
package com.papeleria.nomina.presentacion;

import com.papeleria.nomina.negocio.Empleado;
import com.papeleria.nomina.negocio.EmpleadoServicio;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class VentanaEmpleados extends JFrame {

    private final EmpleadoServicio servicio =
            new EmpleadoServicio();

    private final SimpleDateFormat formato =
            new SimpleDateFormat("yyyy-MM-dd");

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

    private JTextField txtId;
    private JTextField txtCedula;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtFecha;
    private JTextField txtCargo;
    private JTextField txtSueldo;
    private JComboBox<String> txtEstado;
    private JTextField txtBuscar;

    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> ordenador;

    public VentanaEmpleados() {

        setTitle("Sistema de Papelería - Empleados");
        setSize(1150, 700);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        formato.setLenient(false);

        inicializarCampos();
        crearInterfaz();
        cargarEmpleados();
    }

    // =====================================
    // CAMPOS DEL FORMULARIO
    // =====================================

    private void inicializarCampos() {

        txtId = new JTextField();
        txtCedula = new JTextField();
        txtNombres = new JTextField();
        txtApellidos = new JTextField();
        txtFecha = new JTextField();
        txtCargo = new JTextField();
        txtSueldo = new JTextField();

        txtEstado = new JComboBox<>(
                new String[]{"ACTIVO", "INACTIVO"}
        );

        txtId.setEditable(false);
        txtFecha.setEditable(false);
        txtFecha.setToolTipText("Haz clic para seleccionar la fecha de ingreso");
        txtFecha.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        txtFecha.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarCalendario();
            }
        });
    }

    // =====================================
    // INTERFAZ PRINCIPAL
    // =====================================

    private void crearInterfaz() {

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        // ENCABEZADO
        JPanel superior = new JPanel(new BorderLayout());
        superior.setBackground(CELESTE);
        superior.setPreferredSize(new Dimension(1150, 58));
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
                "Gestión de empleados"
        );
        lblSeccion.setForeground(AZUL_OSCURO);

        superior.add(lblSistema, BorderLayout.WEST);
        superior.add(lblSeccion, BorderLayout.EAST);

        principal.add(superior, BorderLayout.NORTH);

        // MENÚ LATERAL
        JPanel lateral = new JPanel();
        lateral.setBackground(AZUL_OSCURO);
        lateral.setPreferredSize(new Dimension(200, 0));
        lateral.setLayout(
                new BoxLayout(lateral, BoxLayout.Y_AXIS)
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

        for (JButton boton : new JButton[]{
                btnInicio,
                btnEmpleados,
                btnNomina,
                btnMotivos,
                btnReportes
        }) {
            lateral.add(boton);
            lateral.add(Box.createVerticalStrut(7));
        }

        lateral.add(Box.createVerticalGlue());
        lateral.add(new JSeparator());
        lateral.add(Box.createVerticalStrut(12));
        lateral.add(btnRegresar);

        principal.add(lateral, BorderLayout.WEST);

        // CONTENIDO CENTRAL
        JPanel centro = new JPanel(
                new BorderLayout(0, 20)
        );
        centro.setBackground(FONDO);
        centro.setBorder(
                new EmptyBorder(28, 30, 25, 30)
        );

        JPanel cabecera = new JPanel(
                new BorderLayout(0, 16)
        );
        cabecera.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(
                new BoxLayout(textos, BoxLayout.Y_AXIS)
        );

        JLabel titulo = new JLabel("Empleados");
        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );
        titulo.setForeground(AZUL_OSCURO);

        JLabel descripcion = new JLabel(
                "Gestiona la información de los empleados."
        );
        descripcion.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );
        descripcion.setForeground(GRIS);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(descripcion);

        cabecera.add(textos, BorderLayout.NORTH);

        // BUSCADOR
        JPanel herramientas = new JPanel(
                new BorderLayout(12, 0)
        );
        herramientas.setOpaque(false);

        txtBuscar = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(145, 155, 165));
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    Insets insets = getInsets();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString("Ingrese el ID del empleado que desea buscar",
                            insets.left, y);
                    g2.dispose();
                }
            }
        };
        txtBuscar.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );
        txtBuscar.setToolTipText(
                "Buscar únicamente por ID del empleado"
        );
        txtBuscar.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(
                                new Color(210, 225, 240)
                        ),
                        new EmptyBorder(10, 12, 10, 12)
                )
        );

        JButton btnBuscar =
                crearBotonAccion("Buscar", AZUL);

        JButton btnMostrar =
                crearBotonAccion(
                        "Mostrar todos", AZUL_OSCURO
                );

        JButton btnNuevo =
                crearBotonAccion("+ Nuevo", AZUL);

        JPanel accionesBusqueda = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        accionesBusqueda.setOpaque(false);

        accionesBusqueda.add(btnBuscar);
        accionesBusqueda.add(btnMostrar);
        accionesBusqueda.add(btnNuevo);

        herramientas.add(
                txtBuscar, BorderLayout.CENTER
        );
        herramientas.add(
                accionesBusqueda, BorderLayout.EAST
        );

        cabecera.add(
                herramientas, BorderLayout.SOUTH
        );

        centro.add(cabecera, BorderLayout.NORTH);

        // TABLA
        modelo = new DefaultTableModel(
                new String[]{
                    "ID",
                    "Cédula",
                    "Nombres",
                    "Apellidos",
                    "Fecha ingreso",
                    "Cargo",
                    "Sueldo",
                    "Estado",
                    "Acciones"
                }, 0) {

            @Override
            public boolean isCellEditable(
                    int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );
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

        JTableHeader encabezadoTabla =
                tabla.getTableHeader();

        encabezadoTabla.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );
        encabezadoTabla.setBackground(CELESTE);
        encabezadoTabla.setForeground(AZUL_OSCURO);
        encabezadoTabla.setPreferredSize(
                new Dimension(0, 42)
        );

        ordenador = new TableRowSorter<>(modelo);
        tabla.setRowSorter(ordenador);

        tabla.getColumnModel()
                .getColumn(8)
                .setCellRenderer(new RenderAcciones());

        tabla.getColumnModel()
                .getColumn(8)
                .setPreferredWidth(110);

        tabla.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                int filaVista =
                        tabla.rowAtPoint(e.getPoint());

                int columnaVista =
                        tabla.columnAtPoint(e.getPoint());

                if (filaVista < 0) {
                    return;
                }

                int filaModelo =
                        tabla.convertRowIndexToModel(
                                filaVista
                        );

                if (columnaVista == 8) {

                    Rectangle celda =
                            tabla.getCellRect(
                                    filaVista, 8, true
                            );

                    int posicionX = e.getX() - celda.x;

                    cargarFilaFormulario(filaModelo);

                    if (posicionX < celda.width / 2) {
                        abrirFormulario(true);
                    } else {
                        eliminar();
                    }

                } else if (e.getClickCount() == 2) {

                    cargarFilaFormulario(filaModelo);
                    abrirFormulario(true);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(
                new LineBorder(
                        new Color(214, 230, 244),
                        1,
                        true
                )
        );
        scroll.getViewport().setBackground(Color.WHITE);

        centro.add(scroll, BorderLayout.CENTER);

        // PIE INFERIOR
        JPanel pie = new JPanel(new BorderLayout());
        pie.setBackground(CELESTE);
        pie.setPreferredSize(new Dimension(0, 35));

        JLabel lblPie = new JLabel(
                "   Sistema de Gestión de Papelería - Empleados"
        );
        lblPie.setForeground(AZUL_OSCURO);

        pie.add(lblPie, BorderLayout.WEST);

        principal.add(centro, BorderLayout.CENTER);
        principal.add(pie, BorderLayout.SOUTH);

        setContentPane(principal);

        // EVENTOS
        btnNuevo.addActionListener(e -> {
            limpiar();
            abrirFormulario(false);
        });

        btnBuscar.addActionListener(
                e -> filtrarTabla()
        );

        txtBuscar.addActionListener(
                e -> filtrarTabla()
        );

        btnMostrar.addActionListener(e -> {
            txtBuscar.setText("");
            ordenador.setRowFilter(null);
            cargarEmpleados();
        });

        btnInicio.addActionListener(
                e -> regresarMenu()
        );

        btnEmpleados.addActionListener(e -> {
            ordenador.setRowFilter(null);
            txtBuscar.setText("");
            cargarEmpleados();
        });

        btnNomina.addActionListener(
                e -> new VentanaNomina().setVisible(true)
        );

        btnMotivos.addActionListener(
                e -> new VentanaMotivos().setVisible(true)
        );

        btnReportes.addActionListener(
                e -> new VentanaReportes().setVisible(true)
        );

        btnRegresar.addActionListener(
                e -> regresarMenu()
        );
    }

    // =====================================
    // BOTONES
    // =====================================

    private JButton crearBotonLateral(String texto) {

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
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
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
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return boton;
    }

    // =====================================
    // FORMULARIO NUEVO / EDITAR
    // =====================================

    private void abrirFormulario(boolean edicion) {

        JDialog dialogo = new JDialog(
                this,
                edicion
                        ? "Editar empleado"
                        : "Registrar empleado",
                true
        );

        dialogo.setSize(630, 570);
        dialogo.setMinimumSize(
                new Dimension(600, 540)
        );
        dialogo.setLocationRelativeTo(this);

        JPanel principal = new JPanel(
                new BorderLayout(0, 20)
        );
        principal.setBackground(Color.WHITE);
        principal.setBorder(
                new EmptyBorder(22, 25, 22, 25)
        );

        JLabel titulo = new JLabel(
                edicion
                        ? "Editar empleado"
                        : "Nuevo empleado"
        );
        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );
        titulo.setForeground(AZUL_OSCURO);

        principal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(
                new GridLayout(8, 2, 12, 12)
        );
        formulario.setBackground(Color.WHITE);

        txtId.setEditable(false);
        txtFecha.setEditable(false);

        if (!edicion) {
            limpiar();
        }

        formulario.add(new JLabel("ID empleado:"));
        formulario.add(txtId);

        formulario.add(new JLabel("Cédula:"));
        formulario.add(txtCedula);

        formulario.add(new JLabel("Nombres:"));
        formulario.add(txtNombres);

        formulario.add(new JLabel("Apellidos:"));
        formulario.add(txtApellidos);

        // FECHA DE INGRESO CON CALENDARIO
        formulario.add(
                new JLabel(
                        "Fecha de ingreso (AAAA-MM-DD):"
                )
        );

        JPanel panelFecha = new JPanel(
                new BorderLayout(5, 0)
        );
        panelFecha.setBackground(Color.WHITE);

        // Selector compacto: al pulsar la flecha o el campo se abre el calendario.
        JButton btnCalendario = new JButton("▼");
        btnCalendario.setPreferredSize(new Dimension(38, 30));
        btnCalendario.setToolTipText("Seleccionar fecha de ingreso");
        btnCalendario.setFocusPainted(false);
        btnCalendario.setBackground(CELESTE);
        btnCalendario.setForeground(AZUL_OSCURO);
        btnCalendario.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        btnCalendario.addActionListener(
                e -> mostrarCalendario()
        );

        panelFecha.add(
                txtFecha, BorderLayout.CENTER
        );
        panelFecha.add(
                btnCalendario, BorderLayout.EAST
        );

        formulario.add(panelFecha);

        formulario.add(new JLabel("Cargo:"));
        formulario.add(txtCargo);

        formulario.add(new JLabel("Sueldo base:"));
        formulario.add(txtSueldo);

        formulario.add(new JLabel("Estado:"));
        formulario.add(txtEstado);

        principal.add(
                formulario, BorderLayout.CENTER
        );

        JPanel botones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        botones.setBackground(Color.WHITE);

        JButton btnCancelar =
                crearBotonAccion("Cancelar", GRIS);

        JButton btnGuardar =
                crearBotonAccion(
                        edicion ? "Actualizar" : "Registrar",
                        AZUL
                );

        botones.add(btnCancelar);
        botones.add(btnGuardar);

        principal.add(botones, BorderLayout.SOUTH);

        btnCancelar.addActionListener(
                e -> dialogo.dispose()
        );

        btnGuardar.addActionListener(e -> {

            boolean correcto = edicion
                    ? actualizar()
                    : registrar();

            if (correcto) {
                dialogo.dispose();
            }
        });

        dialogo.setContentPane(principal);
        dialogo.setVisible(true);
    }

    // =====================================
    // CALENDARIO VISUAL
    // =====================================

    private void mostrarCalendario() {

        JDialog calendario = new JDialog(
                this,
                "Seleccionar fecha de ingreso",
                true
        );

        calendario.setSize(390, 355);
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

        LocalDate fechaInicial = LocalDate.now();

        try {
            if (!txtFecha.getText().trim().isEmpty()) {
                fechaInicial = LocalDate.parse(
                        txtFecha.getText().trim()
                );
            }
        } catch (Exception ex) {
            fechaInicial = LocalDate.now();
        }

        final YearMonth[] mesActual = {
            YearMonth.from(fechaInicial)
        };

        Runnable actualizarCalendario = () -> {

            dias.removeAll();

            YearMonth mes = mesActual[0];

            String[] meses = {
                "Enero", "Febrero", "Marzo",
                "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre"
            };

            lblMes.setText(
                    meses[mes.getMonthValue() - 1]
                    + " " + mes.getYear()
            );

            String[] nombresDias = {
                "L", "M", "M", "J", "V", "S", "D"
            };

            for (String nombre : nombresDias) {

                JLabel etiqueta = new JLabel(
                        nombre,
                        SwingConstants.CENTER
                );
                etiqueta.setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12
                        )
                );
                etiqueta.setForeground(AZUL_OSCURO);
                dias.add(etiqueta);
            }

            int espacios =
                    mes.atDay(1)
                            .getDayOfWeek()
                            .getValue() - 1;

            for (int i = 0; i < espacios; i++) {
                dias.add(new JLabel(""));
            }

            for (int dia = 1;
                    dia <= mes.lengthOfMonth();
                    dia++) {

                final int numeroDia = dia;

                JButton botonDia = new JButton(
                        String.valueOf(dia)
                );

                botonDia.setFocusPainted(false);
                botonDia.setBackground(Color.WHITE);
                botonDia.setForeground(AZUL_OSCURO);
                botonDia.setMargin(
                        new Insets(3, 3, 3, 3)
                );

                LocalDate fechaBoton =
                        mes.atDay(numeroDia);

                if (fechaBoton.equals(LocalDate.now())) {
                    botonDia.setBackground(CELESTE);
                }

                botonDia.addActionListener(e -> {

                    txtFecha.setText(
                            fechaBoton.toString()
                    );

                    calendario.dispose();
                });

                dias.add(botonDia);
            }

            dias.revalidate();
            dias.repaint();
        };

        anterior.addActionListener(e -> {
            mesActual[0] = mesActual[0].minusMonths(1);
            actualizarCalendario.run();
        });

        siguiente.addActionListener(e -> {
            mesActual[0] = mesActual[0].plusMonths(1);
            actualizarCalendario.run();
        });

        // Sin botón «Hoy»: la fecha se elige directamente en el calendario.
        calendario.add(cabecera, BorderLayout.NORTH);
        calendario.add(dias, BorderLayout.CENTER);

        actualizarCalendario.run();

        calendario.setVisible(true);
    }

    // =====================================
    // OBTENER DATOS DEL FORMULARIO
    // =====================================

    private Empleado obtenerDatos() throws Exception {

        if (txtCedula.getText().trim().isEmpty()
                || txtNombres.getText().trim().isEmpty()
                || txtApellidos.getText().trim().isEmpty()
                || txtFecha.getText().trim().isEmpty()
                || txtCargo.getText().trim().isEmpty()
                || txtSueldo.getText().trim().isEmpty()) {

            throw new Exception(
                    "Completa todos los campos."
            );
        }

        Date fecha = formato.parse(
                txtFecha.getText().trim()
        );

        double sueldo = Double.parseDouble(
                txtSueldo.getText().trim()
        );

        if (!Double.isFinite(sueldo) || sueldo < 0) {
            throw new Exception(
                    "El sueldo debe ser un número válido "
                    + "y no puede ser negativo."
            );
        }

        Empleado empleado = new Empleado();

        empleado.setCedula(
                txtCedula.getText().trim()
        );

        empleado.setNombres(
                txtNombres.getText().trim()
        );

        empleado.setApellidos(
                txtApellidos.getText().trim()
        );

        empleado.setFechaIngreso(fecha);

        empleado.setCargo(
                txtCargo.getText().trim()
        );

        empleado.setSueldoBase(sueldo);

        empleado.setEstado(
                txtEstado.getSelectedItem().toString()
        );

        return empleado;
    }

    // =====================================
    // REGISTRAR EMPLEADO
    // =====================================

    private boolean registrar() {

        try {

            Empleado empleado = obtenerDatos();

            servicio.registrarEmpleado(empleado);

            JOptionPane.showMessageDialog(
                    this,
                    "Empleado registrado correctamente."
            );

            limpiar();
            cargarEmpleados();

            return true;

        } catch (Exception ex) {

            mostrarError(ex);
            return false;
        }
    }

    // =====================================
    // ACTUALIZAR EMPLEADO
    // =====================================

    private boolean actualizar() {

        try {

            if (txtId.getText().isEmpty()) {

                throw new Exception(
                        "Selecciona un empleado."
                );
            }

            Empleado empleado = obtenerDatos();

            empleado.setIdEmpleado(
                    Integer.parseInt(
                            txtId.getText()
                    )
            );

            boolean resultado =
                    servicio.actualizarEmpleado(
                            empleado
                    );

            if (resultado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Empleado actualizado correctamente."
                );

                limpiar();
                cargarEmpleados();

                return true;

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró el empleado."
                );

                return false;
            }

        } catch (Exception ex) {

            mostrarError(ex);
            return false;
        }
    }

    // =====================================
    // ELIMINAR EMPLEADO
    // =====================================

    private void eliminar() {

        try {

            if (txtId.getText().isEmpty()) {

                throw new Exception(
                        "Selecciona un empleado."
                );
            }

            int respuesta =
                    JOptionPane.showConfirmDialog(
                            this,
                            "¿Deseas eliminar este empleado?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            boolean resultado =
                    servicio.eliminarEmpleado(
                            Integer.parseInt(
                                    txtId.getText()
                            )
                    );

            if (resultado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Empleado eliminado correctamente."
                );

                limpiar();
                cargarEmpleados();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró el empleado."
                );
            }

        } catch (Exception ex) {

            mostrarError(ex);
        }
    }

    // =====================================
    // BUSCAR EMPLEADOS
    // =====================================

    private void filtrarTabla() {

        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            ordenador.setRowFilter(null);
            return;
        }

        final int idBuscado;
        try {
            idBuscado = Integer.parseInt(texto);
            if (idBuscado < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un ID de empleado válido (solo números).",
                    "Búsqueda de empleados",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        ordenador.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entrada) {
                Object valor = entrada.getValue(0);
                try {
                    return Integer.parseInt(String.valueOf(valor).trim()) == idBuscado;
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        });

        if (tabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "No existe un empleado con el ID " + idBuscado + ".",
                    "Búsqueda de empleados",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // =====================================
    // CARGAR EMPLEADOS DESDE ORACLE
    // =====================================

    private void cargarEmpleados() {

        try {

            List<Empleado> empleados =
                    servicio.listarEmpleados();

            modelo.setRowCount(0);

            for (Empleado e : empleados) {

                modelo.addRow(new Object[]{
                    e.getIdEmpleado(),
                    e.getCedula(),
                    e.getNombres(),
                    e.getApellidos(),
                    e.getFechaIngreso() == null
                            ? ""
                            : formato.format(
                                    e.getFechaIngreso()
                            ),
                    e.getCargo(),
                    e.getSueldoBase(),
                    e.getEstado(),
                    "Editar  |  Eliminar"
                });
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al consultar empleados:\n"
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =====================================
    // SELECCIONAR EMPLEADO
    // =====================================

    private void cargarFilaFormulario(int fila) {

        txtId.setText(
                String.valueOf(
                        modelo.getValueAt(fila, 0)
                )
        );

        txtCedula.setText(
                String.valueOf(
                        modelo.getValueAt(fila, 1)
                )
        );

        txtNombres.setText(
                String.valueOf(
                        modelo.getValueAt(fila, 2)
                )
        );

        txtApellidos.setText(
                String.valueOf(
                        modelo.getValueAt(fila, 3)
                )
        );

        txtFecha.setText(
                String.valueOf(
                        modelo.getValueAt(fila, 4)
                )
        );

        txtCargo.setText(
                String.valueOf(
                        modelo.getValueAt(fila, 5)
                )
        );

        txtSueldo.setText(
                String.valueOf(
                        modelo.getValueAt(fila, 6)
                )
        );

        txtEstado.setSelectedItem(
                String.valueOf(
                        modelo.getValueAt(fila, 7)
                ).trim().toUpperCase()
        );
    }

    // =====================================
    // LIMPIAR FORMULARIO
    // =====================================

    private void limpiar() {

        txtId.setText("");
        txtCedula.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtFecha.setText("");
        txtCargo.setText("");
        txtSueldo.setText("");

        txtEstado.setSelectedItem("ACTIVO");

        if (tabla != null) {
            tabla.clearSelection();
        }
    }

    // =====================================
    // MOSTRAR ERRORES
    // =====================================

    private void mostrarError(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Error: " + ex.getMessage(),
                "Sistema de Nómina",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void regresarMenu() {
        dispose();
    }

    // =====================================
    // COLUMNA EDITAR / ELIMINAR
    // =====================================

    private class RenderAcciones
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable tabla,
                Object valor,
                boolean seleccionado,
                boolean foco,
                int fila,
                int columna) {

            JPanel panel = new JPanel(
                    new GridLayout(1, 2, 5, 0)
            );

            panel.setBorder(
                    new EmptyBorder(5, 4, 5, 4)
            );

            panel.setBackground(
                    seleccionado
                            ? tabla.getSelectionBackground()
                            : Color.WHITE
            );

            JLabel editar = new JLabel(
                    "Editar",
                    SwingConstants.CENTER
            );

            editar.setForeground(AZUL);
            editar.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            JLabel eliminar = new JLabel(
                    "Eliminar",
                    SwingConstants.CENTER
            );

            eliminar.setForeground(
                    new Color(210, 55, 65)
            );

            eliminar.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            panel.add(editar);
            panel.add(eliminar);

            return panel;
        }
    }

    // =====================================
    // EJECUCIÓN
    // =====================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new VentanaEmpleados().setVisible(true);

        });
    }
}
