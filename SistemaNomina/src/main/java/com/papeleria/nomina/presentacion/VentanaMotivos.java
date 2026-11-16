
package com.papeleria.nomina.presentacion;

import com.papeleria.nomina.negocio.MotivoNomina;
import com.papeleria.nomina.negocio.MotivoNominaServicio;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class VentanaMotivos extends JFrame {

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

    private static final Color BLANCO =
            Color.WHITE;

    // ==========================================
    // COMPONENTES
    // ==========================================

    private JTextField txtId;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;
    private JTextField txtBuscar;

    private JComboBox<String> cmbTipo;
    private JComboBox<String> cmbEstado;

    private JTable tablaMotivos;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> ordenador;

    private final MotivoNominaServicio servicio =
            new MotivoNominaServicio();

    public VentanaMotivos() {

        setTitle("Sistema de Papelería - Motivos");

        setSize(1150, 700);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        inicializarComponentes();
        cargarMotivos();
    }

    // ==========================================
    // INTERFAZ PRINCIPAL
    // ==========================================

    private void inicializarComponentes() {

        JPanel principal = new JPanel(
                new BorderLayout()
        );

        principal.setBackground(FONDO);

        // ENCABEZADO SUPERIOR
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
                "Gestión de motivos"
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
        // CONTENIDO CENTRAL
        // ==========================================

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
                new BoxLayout(
                        textos, BoxLayout.Y_AXIS
                )
        );

        JLabel titulo = new JLabel(
                "Motivos"
        );

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 28)
        );

        titulo.setForeground(AZUL_OSCURO);

        JLabel descripcion = new JLabel(
                "Gestiona los motivos de ingresos y egresos de nómina."
        );

        descripcion.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        descripcion.setForeground(GRIS);

        textos.add(titulo);
        textos.add(Box.createVerticalStrut(5));
        textos.add(descripcion);

        cabecera.add(
                textos, BorderLayout.NORTH
        );

        // ==========================================
        // BUSCADOR Y BOTONES
        // ==========================================

        JPanel herramientas = new JPanel(
                new BorderLayout(12, 0)
        );

        herramientas.setOpaque(false);

        txtBuscar = new JTextField();

        txtBuscar.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        txtBuscar.setToolTipText(
                "Buscar por código, nombre, tipo o estado"
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

        JButton btnNuevo =
                crearBotonAccion(
                        "+ Nuevo motivo", AZUL
                );

        JPanel acciones = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT, 8, 0
                )
        );

        acciones.setOpaque(false);

        acciones.add(btnBuscar);
        acciones.add(btnNuevo);

        herramientas.add(
                txtBuscar, BorderLayout.CENTER
        );

        herramientas.add(
                acciones, BorderLayout.EAST
        );

        cabecera.add(
                herramientas, BorderLayout.SOUTH
        );

        // FILTROS
        JPanel filtros = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT, 8, 0
                )
        );

        filtros.setOpaque(false);

        JButton btnMostrar =
                crearBotonAccion(
                        "Todos", AZUL_OSCURO
                );

        JButton btnIngresos =
                crearBotonAccion(
                        "Ingresos", AZUL
                );

        JButton btnEgresos =
                crearBotonAccion(
                        "Egresos", AZUL
                );

        filtros.add(btnMostrar);
        filtros.add(btnIngresos);
        filtros.add(btnEgresos);

        JPanel parteSuperior = new JPanel(
                new BorderLayout(0, 15)
        );

        parteSuperior.setOpaque(false);

        parteSuperior.add(
                cabecera, BorderLayout.NORTH
        );

        parteSuperior.add(
                filtros, BorderLayout.SOUTH
        );

        centro.add(
                parteSuperior, BorderLayout.NORTH
        );

        // ==========================================
        // TABLA DE MOTIVOS
        // ==========================================

        modeloTabla = new DefaultTableModel(
                new String[]{
                    "ID",
                    "Código",
                    "Nombre",
                    "Tipo",
                    "Descripción",
                    "Estado"
                }, 0) {

            @Override
            public boolean isCellEditable(
                    int fila, int columna) {

                return false;
            }
        };

        tablaMotivos = new JTable(modeloTabla);

        tablaMotivos.setRowHeight(38);

        tablaMotivos.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        tablaMotivos.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tablaMotivos.setGridColor(
                new Color(232, 239, 247)
        );

        tablaMotivos.setShowVerticalLines(false);

        tablaMotivos.setSelectionBackground(
                new Color(225, 240, 255)
        );

        tablaMotivos.setSelectionForeground(
                AZUL_OSCURO
        );

        JTableHeader encabezadoTabla =
                tablaMotivos.getTableHeader();

        encabezadoTabla.setFont(
                new Font("Segoe UI", Font.BOLD, 12)
        );

        encabezadoTabla.setBackground(CELESTE);
        encabezadoTabla.setForeground(AZUL_OSCURO);

        encabezadoTabla.setPreferredSize(
                new Dimension(0, 42)
        );

        ordenador = new TableRowSorter<>(
                modeloTabla
        );

        tablaMotivos.setRowSorter(ordenador);

        JScrollPane scroll =
                new JScrollPane(tablaMotivos);

        scroll.setBorder(
                new LineBorder(
                        new Color(214, 230, 244),
                        1,
                        true
                )
        );

        scroll.getViewport().setBackground(
                Color.WHITE
        );

        centro.add(
                scroll, BorderLayout.CENTER
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
                "   Sistema de Gestión de Papelería - Motivos"
        );

        lblPie.setForeground(AZUL_OSCURO);

        pie.add(lblPie, BorderLayout.WEST);

        principal.add(
                centro, BorderLayout.CENTER
        );

        principal.add(
                pie, BorderLayout.SOUTH
        );

        setContentPane(principal);

        // ==========================================
        // EVENTOS
        // ==========================================

        btnNuevo.addActionListener(e -> {
            limpiarFormulario();
            abrirFormulario();
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
            cargarMotivos();
        });

        btnIngresos.addActionListener(e -> {
            txtBuscar.setText("");
            ordenador.setRowFilter(null);
            cargarPorTipo("INGRESO");
        });

        btnEgresos.addActionListener(e -> {
            txtBuscar.setText("");
            ordenador.setRowFilter(null);
            cargarPorTipo("EGRESO");
        });

        btnInicio.addActionListener(
                e -> dispose()
        );

        btnEmpleados.addActionListener(e -> {
            new VentanaEmpleados().setVisible(true);
        });

        btnNomina.addActionListener(e -> {
            new VentanaNomina().setVisible(true);
        });

        btnMotivos.addActionListener(
                e -> cargarMotivos()
        );

        btnReportes.addActionListener(e -> {
            new VentanaReportes().setVisible(true);
        });

        btnRegresar.addActionListener(
                e -> dispose()
        );
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

        boton.setForeground(BLANCO);
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
    // FORMULARIO EMERGENTE
    // ==========================================

    private void abrirFormulario() {

        JDialog dialogo = new JDialog(
                this,
                "Registrar nuevo motivo",
                true
        );

        dialogo.setSize(590, 470);
        dialogo.setLocationRelativeTo(this);

        JPanel principal = new JPanel(
                new BorderLayout(0, 20)
        );

        principal.setBackground(BLANCO);

        principal.setBorder(
                new EmptyBorder(22, 25, 22, 25)
        );

        JLabel titulo = new JLabel(
                "Nuevo motivo"
        );

        titulo.setFont(
                new Font("Segoe UI", Font.BOLD, 22)
        );

        titulo.setForeground(AZUL_OSCURO);

        principal.add(
                titulo, BorderLayout.NORTH
        );

        JPanel formulario = new JPanel(
                new GridLayout(6, 2, 12, 12)
        );

        formulario.setBackground(BLANCO);

        txtId = new JTextField();
        txtId.setEditable(false);

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();

        cmbTipo = new JComboBox<>(
                new String[]{
                    "INGRESO",
                    "EGRESO"
                }
        );

        cmbEstado = new JComboBox<>(
                new String[]{
                    "ACTIVO",
                    "INACTIVO"
                }
        );

        formulario.add(
                new JLabel("ID motivo:")
        );

        formulario.add(txtId);

        formulario.add(
                new JLabel("Código:")
        );

        formulario.add(txtCodigo);

        formulario.add(
                new JLabel("Nombre:")
        );

        formulario.add(txtNombre);

        formulario.add(
                new JLabel("Tipo:")
        );

        formulario.add(cmbTipo);

        formulario.add(
                new JLabel("Descripción:")
        );

        formulario.add(txtDescripcion);

        formulario.add(
                new JLabel("Estado:")
        );

        formulario.add(cmbEstado);

        principal.add(
                formulario, BorderLayout.CENTER
        );

        JPanel botones = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT, 10, 0
                )
        );

        botones.setBackground(BLANCO);

        JButton btnCancelar =
                crearBotonAccion(
                        "Cancelar", GRIS
                );

        JButton btnRegistrar =
                crearBotonAccion(
                        "Registrar", AZUL
                );

        botones.add(btnCancelar);
        botones.add(btnRegistrar);

        principal.add(
                botones, BorderLayout.SOUTH
        );

        btnCancelar.addActionListener(
                e -> dialogo.dispose()
        );

        btnRegistrar.addActionListener(e -> {

            if (registrarMotivo()) {
                dialogo.dispose();
            }
        });

        dialogo.setContentPane(principal);
        dialogo.setVisible(true);
    }

    // ==========================================
    // CARGAR TODOS LOS MOTIVOS
    // ==========================================

    private void cargarMotivos() {

        try {

            List<MotivoNomina> lista =
                    servicio.listarTodos();

            mostrarEnTabla(lista);

        } catch (SQLException e) {

            mostrarError(
                    "Error al cargar motivos: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // FILTRAR POR TIPO
    // ==========================================

    private void cargarPorTipo(String tipo) {

        try {

            List<MotivoNomina> lista;

            if ("INGRESO".equals(tipo)) {

                lista = servicio.listarIngresos();

            } else {

                lista = servicio.listarEgresos();
            }

            mostrarEnTabla(lista);

        } catch (SQLException e) {

            mostrarError(
                    "Error al filtrar motivos: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // MOSTRAR EN TABLA
    // ==========================================

    private void mostrarEnTabla(
            List<MotivoNomina> lista) {

        modeloTabla.setRowCount(0);

        for (MotivoNomina m : lista) {

            modeloTabla.addRow(
                    new Object[]{
                        m.getIdMotivo(),
                        m.getCodigo(),
                        m.getNombre(),
                        m.getTipo(),
                        m.getDescripcion(),
                        m.getEstado()
                    }
            );
        }
    }

    // ==========================================
    // BUSCAR MOTIVOS
    // ==========================================

    private void filtrarTabla() {

        String texto =
                txtBuscar.getText().trim();

        if (texto.isEmpty()) {

            ordenador.setRowFilter(null);
            return;
        }

        ordenador.setRowFilter(
                RowFilter.regexFilter(
                        "(?i)" + Pattern.quote(texto),
                        1, 2, 3, 5
                )
        );
    }

    // ==========================================
    // REGISTRAR MOTIVO EN ORACLE
    // ==========================================

    private boolean registrarMotivo() {

        try {

            String codigo =
                    txtCodigo.getText().trim();

            String nombre =
                    txtNombre.getText().trim();

            String descripcion =
                    txtDescripcion.getText().trim();

            String tipo =
                    cmbTipo.getSelectedItem().toString();

            String estado =
                    cmbEstado.getSelectedItem().toString();

            if (codigo.isEmpty()
                    || nombre.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Debes ingresar el código "
                        + "y el nombre del motivo.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );

                return false;
            }

            MotivoNomina motivo =
                    new MotivoNomina();

            motivo.setCodigo(codigo);
            motivo.setNombre(nombre);
            motivo.setTipo(tipo);
            motivo.setDescripcion(descripcion);
            motivo.setEstado(estado);

            boolean registrado =
                    servicio.registrar(motivo);

            if (registrado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Motivo registrado correctamente.",
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE
                );

                cargarMotivos();

                return true;

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar el motivo.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );

                return false;
            }

        } catch (IllegalArgumentException e) {

            mostrarError(e.getMessage());
            return false;

        } catch (SQLException e) {

            mostrarError(
                    "Error al registrar motivo: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // ==========================================
    // LIMPIAR FORMULARIO
    // ==========================================

    private void limpiarFormulario() {

        if (txtId != null) {
            txtId.setText("");
        }

        if (txtCodigo != null) {
            txtCodigo.setText("");
        }

        if (txtNombre != null) {
            txtNombre.setText("");
        }

        if (txtDescripcion != null) {
            txtDescripcion.setText("");
        }

        if (cmbTipo != null) {
            cmbTipo.setSelectedIndex(0);
        }

        if (cmbEstado != null) {
            cmbEstado.setSelectedIndex(0);
        }

        if (tablaMotivos != null) {
            tablaMotivos.clearSelection();
        }
    }

    // ==========================================
    // MENSAJE DE ERROR
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
    // EJECUCIÓN
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new VentanaMotivos().setVisible(true);

        });
    }
}
