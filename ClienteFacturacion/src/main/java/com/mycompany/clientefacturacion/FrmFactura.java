
package com.mycompany.clientefacturacion;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class FrmFactura extends JFrame {

    private static final Color AZUL_OSCURO = new Color(9, 48, 89);
    private static final Color VERDE = new Color(33, 150, 83);
    private static final Color FONDO = new Color(248, 251, 255);
    private static final Color BLANCO = Color.WHITE;
    private static final Color GRIS = new Color(100, 115, 130);
    private static final Color AZUL_BOTON = new Color(20, 125, 235);

    private JTextField txtNumFactura;
    private JTextField txtFecha;
    private JComboBox<String> cbCliente;
    private JComboBox<String> cbCiudad;
    private JComboBox<String> cbArticulo;
    private JTextField txtCantidad;

    private JTable tblDetalle;
    private DefaultTableModel modelo;
    private JLabel lblTotal;

    public FrmFactura() {
        setTitle("Facturación - Nueva Factura");
        setSize(1000, 700);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarDatosIniciales();
    }

    private void inicializarComponentes() {

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(FONDO);

        // ENCABEZADO

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(FONDO);
        encabezado.setBorder(
            new EmptyBorder(20, 30, 10, 30)
        );

        JLabel titulo = new JLabel("Nueva Factura");
        titulo.setFont(
            new Font("Segoe UI", Font.BOLD, 28)
        );
        titulo.setForeground(VERDE);

        JLabel subtitulo = new JLabel(
            "Registra una nueva venta en el sistema."
        );
        subtitulo.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );
        subtitulo.setForeground(GRIS);

        JPanel panelTitulos = new JPanel();
        panelTitulos.setOpaque(false);
        panelTitulos.setLayout(
            new BoxLayout(
                panelTitulos,
                BoxLayout.Y_AXIS
            )
        );

        panelTitulos.add(titulo);
        panelTitulos.add(
            Box.createVerticalStrut(5)
        );
        panelTitulos.add(subtitulo);

        encabezado.add(
            panelTitulos,
            BorderLayout.WEST
        );

        principal.add(
            encabezado,
            BorderLayout.NORTH
        );

        // FORMULARIO

        JPanel cuerpo = new JPanel(
            new BorderLayout(0, 20)
        );
        cuerpo.setBackground(FONDO);
        cuerpo.setBorder(
            new EmptyBorder(10, 30, 20, 30)
        );

        JPanel panelFormulario =
            new JPanel(new GridBagLayout());

        panelFormulario.setOpaque(false);

        GridBagConstraints gbc =
            new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridwidth = 1;

        // FILA 1

        gbc.gridx = 0;
        gbc.gridy = 0;

        panelFormulario.add(
            new JLabel("N° Factura:"),
            gbc
        );

        txtNumFactura = new JTextField();
        txtNumFactura.setEditable(false);
        txtNumFactura.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        gbc.gridx = 1;
        panelFormulario.add(
            txtNumFactura,
            gbc
        );

        gbc.gridx = 2;
        panelFormulario.add(
            new JLabel("Fecha:"),
            gbc
        );

        txtFecha = new JTextField();
        txtFecha.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        gbc.gridx = 3;
        panelFormulario.add(
            txtFecha,
            gbc
        );

        // FILA 2

        gbc.gridx = 0;
        gbc.gridy = 1;

        panelFormulario.add(
            new JLabel("Cliente:"),
            gbc
        );

        cbCliente = new JComboBox<>();
        cbCliente.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        gbc.gridx = 1;
        panelFormulario.add(
            cbCliente,
            gbc
        );

        gbc.gridx = 2;
        panelFormulario.add(
            new JLabel("Ciudad:"),
            gbc
        );

        cbCiudad = new JComboBox<>();
        cbCiudad.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        gbc.gridx = 3;
        panelFormulario.add(
            cbCiudad,
            gbc
        );

        cuerpo.add(
            panelFormulario,
            BorderLayout.NORTH
        );

        // DETALLE DE FACTURA

        JPanel panelDetalle = new JPanel(
            new BorderLayout(0, 10)
        );

        panelDetalle.setOpaque(false);

        JPanel panelAgregar = new JPanel(
            new FlowLayout(
                FlowLayout.LEFT,
                10,
                0
            )
        );

        panelAgregar.setOpaque(false);

        panelAgregar.add(
            new JLabel("Artículo:")
        );

        cbArticulo = new JComboBox<>();
        cbArticulo.setPreferredSize(
            new Dimension(250, 30)
        );
        cbArticulo.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        panelAgregar.add(cbArticulo);

        panelAgregar.add(
            new JLabel("Cantidad:")
        );

        txtCantidad = new JTextField(5);
        txtCantidad.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );

        panelAgregar.add(txtCantidad);

        JButton btnAgregar = new JButton(
            "+ Agregar Artículo"
        );

        btnAgregar.setFont(
            new Font("Segoe UI", Font.BOLD, 13)
        );
        btnAgregar.setForeground(BLANCO);
        btnAgregar.setBackground(AZUL_BOTON);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(
            new EmptyBorder(8, 15, 8, 15)
        );

        panelAgregar.add(btnAgregar);

        panelDetalle.add(
            panelAgregar,
            BorderLayout.NORTH
        );

        // TABLA

        modelo = new DefaultTableModel(
            new Object[]{
                "Código",
                "Artículo",
                "Cantidad",
                "Precio Unit.",
                "Subtotal"
            },
            0
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {
                return false;
            }
        };

        tblDetalle = new JTable(modelo);
        tblDetalle.setRowHeight(35);
        tblDetalle.setFont(
            new Font("Segoe UI", Font.PLAIN, 14)
        );
        tblDetalle.setShowVerticalLines(false);

        tblDetalle.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        tblDetalle.getTableHeader().setBackground(
            new Color(240, 246, 252)
        );

        tblDetalle.getTableHeader().setForeground(
            AZUL_OSCURO
        );

        tblDetalle.getTableHeader().setPreferredSize(
            new Dimension(0, 40)
        );

        JScrollPane scroll =
            new JScrollPane(tblDetalle);

        scroll.setBorder(
            new LineBorder(
                new Color(214, 230, 244),
                1,
                true
            )
        );

        scroll.getViewport().setBackground(
            BLANCO
        );

        panelDetalle.add(
            scroll,
            BorderLayout.CENTER
        );

        cuerpo.add(
            panelDetalle,
            BorderLayout.CENTER
        );

        principal.add(
            cuerpo,
            BorderLayout.CENTER
        );

        // PIE DE PÁGINA

        JPanel panelPie =
            new JPanel(new BorderLayout());

        panelPie.setBackground(FONDO);
        panelPie.setBorder(
            new EmptyBorder(10, 30, 20, 30)
        );

        JButton btnRegresar = new JButton(
            "<< Regresar al menú"
        );

        btnRegresar.setFont(
            new Font("Segoe UI", Font.BOLD, 14)
        );

        btnRegresar.setForeground(
            AZUL_OSCURO
        );

        btnRegresar.setContentAreaFilled(false);
        btnRegresar.setBorderPainted(false);

        btnRegresar.addActionListener(
            e -> dispose()
        );

        lblTotal = new JLabel("TOTAL: $0.00");

        lblTotal.setFont(
            new Font("Segoe UI", Font.BOLD, 24)
        );

        lblTotal.setForeground(VERDE);
        lblTotal.setHorizontalAlignment(
            SwingConstants.RIGHT
        );

        JButton btnGuardar = new JButton(
            "GUARDAR FACTURA"
        );

        btnGuardar.setFont(
            new Font("Segoe UI", Font.BOLD, 16)
        );

        btnGuardar.setForeground(BLANCO);
        btnGuardar.setBackground(VERDE);
        btnGuardar.setFocusPainted(false);

        btnGuardar.setBorder(
            new EmptyBorder(12, 25, 12, 25)
        );

        JPanel panelDerechaPie =
            new JPanel(new BorderLayout(20, 0));

        panelDerechaPie.setOpaque(false);

        panelDerechaPie.add(
            lblTotal,
            BorderLayout.CENTER
        );

        panelDerechaPie.add(
            btnGuardar,
            BorderLayout.EAST
        );

        panelPie.add(
            btnRegresar,
            BorderLayout.WEST
        );

        panelPie.add(
            panelDerechaPie,
            BorderLayout.EAST
        );

        principal.add(
            panelPie,
            BorderLayout.SOUTH
        );

        setContentPane(principal);

        // EVENTOS

        btnAgregar.addActionListener(
            e -> agregarArticulo()
        );

        btnGuardar.addActionListener(
            e -> guardarFactura()
        );
    }

    // CARGAR DATOS DE ORACLE

    private void cargarDatosIniciales() {

        SimpleDateFormat formato =
            new SimpleDateFormat("dd/MM/yyyy");

        txtFecha.setText(
            formato.format(new java.util.Date())
        );

        try (Connection con = Conexion.conectar()) {

            if (con == null) {
                throw new SQLException(
                    "No se pudo conectar a Oracle."
                );
            }

            cbCliente.removeAllItems();
            cbCiudad.removeAllItems();
            cbArticulo.removeAllItems();

            // CLIENTES

            try (
                PreparedStatement ps =
                    con.prepareStatement(
                        "SELECT RUC, NOMBRE "
                        + "FROM JENNIFER.CLIENTE "
                        + "ORDER BY NOMBRE"
                    );
                ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    cbCliente.addItem(
                        rs.getString("RUC")
                        + " - "
                        + rs.getString("NOMBRE")
                    );
                }
            }

            // CIUDADES

            try (
                PreparedStatement ps =
                    con.prepareStatement(
                        "SELECT CODIGO, NOMBRE "
                        + "FROM JENNIFER.CIUDAD "
                        + "ORDER BY NOMBRE"
                    );
                ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    cbCiudad.addItem(
                        rs.getString("CODIGO")
                        + " - "
                        + rs.getString("NOMBRE")
                    );
                }
            }

            // ARTÍCULOS

            try (
                PreparedStatement ps =
                    con.prepareStatement(
                        "SELECT CODIGO, NOMBRE "
                        + "FROM JENNIFER.ARTICULO "
                        + "ORDER BY NOMBRE"
                    );
                ResultSet rs = ps.executeQuery()
            ) {
                while (rs.next()) {
                    cbArticulo.addItem(
                        rs.getString("CODIGO")
                        + " - "
                        + rs.getString("NOMBRE")
                    );
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al cargar datos: "
                + e.getMessage()
            );
        }
    }

    // AGREGAR ARTÍCULO

    private void agregarArticulo() {

        if (cbArticulo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione un artículo."
            );
            return;
        }

        int cantidad;

        try {
            cantidad = Integer.parseInt(
                txtCantidad.getText().trim()
            );

            if (cantidad <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "La cantidad debe ser mayor a cero."
            );
            return;
        }

        String seleccion =
            cbArticulo.getSelectedItem().toString();

        String codigo =
            seleccion.split(" - ", 2)[0];

        try (Connection con = Conexion.conectar()) {

            if (con == null) {
                throw new SQLException(
                    "No se pudo conectar a Oracle."
                );
            }

            String sql =
                "SELECT NOMBRE, PRECIO, STOCK "
                + "FROM JENNIFER.ARTICULO "
                + "WHERE CODIGO = ?";

            try (
                PreparedStatement ps =
                    con.prepareStatement(sql)
            ) {
                ps.setString(1, codigo);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException(
                            "El artículo no existe."
                        );
                    }

                    String nombre =
                        rs.getString("NOMBRE");

                    BigDecimal precio =
                        rs.getBigDecimal("PRECIO");

                    int stock =
                        rs.getInt("STOCK");

                    if (precio == null) {
                        throw new SQLException(
                            "El artículo no tiene precio."
                        );
                    }

                    int cantidadExistente = 0;

                    for (int i = 0;
                            i < modelo.getRowCount();
                            i++) {

                        if (codigo.equals(
                                modelo.getValueAt(
                                    i, 0
                                ).toString())) {

                            cantidadExistente +=
                                Integer.parseInt(
                                    modelo.getValueAt(
                                        i, 2
                                    ).toString()
                                );
                        }
                    }

                    if (cantidad + cantidadExistente
                            > stock) {

                        JOptionPane.showMessageDialog(
                            this,
                            "Stock insuficiente.\n"
                            + "Disponible: " + stock
                        );
                        return;
                    }

                    BigDecimal subtotal =
                        precio.multiply(
                            BigDecimal.valueOf(cantidad)
                        );

                    modelo.addRow(
                        new Object[]{
                            codigo,
                            nombre,
                            cantidad,
                            precio,
                            subtotal
                        }
                    );

                    calcularTotal();

                    txtCantidad.setText("");
                    txtCantidad.requestFocus();
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al agregar artículo: "
                + e.getMessage()
            );
        }
    }

    // CALCULAR TOTAL

    private BigDecimal obtenerTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0;
                i < modelo.getRowCount();
                i++) {

            BigDecimal subtotal =
                new BigDecimal(
                    modelo.getValueAt(
                        i, 4
                    ).toString()
                );

            total = total.add(subtotal);
        }

        return total;
    }

    private void calcularTotal() {

        BigDecimal total = obtenerTotal();

        lblTotal.setText(
            "TOTAL: $"
            + total.setScale(
                2,
                java.math.RoundingMode.HALF_UP
            ).toPlainString()
        );
    }

    // GUARDAR FACTURA Y ASIENTO CONTABLE

    private void guardarFactura() {

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Debe agregar al menos un artículo."
            );
            return;
        }

        if (cbCliente.getSelectedItem() == null
                || cbCiudad.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                this,
                "Seleccione cliente y ciudad."
            );
            return;
        }

        String clienteRuc =
            cbCliente.getSelectedItem()
                .toString()
                .split(" - ", 2)[0];

        String ciudadCodigo =
            cbCiudad.getSelectedItem()
                .toString()
                .split(" - ", 2)[0];

        java.sql.Date fechaSql;

        try {
            SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy");

            sdf.setLenient(false);

            fechaSql = new java.sql.Date(
                sdf.parse(
                    txtFecha.getText().trim()
                ).getTime()
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Fecha inválida. Use DD/MM/AAAA."
            );
            return;
        }

        BigDecimal totalFactura =
            obtenerTotal();

        if (totalFactura.signum() <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "El total debe ser mayor a cero."
            );
            return;
        }

        try (Connection con = Conexion.conectar()) {

            if (con == null) {
                throw new SQLException(
                    "No se pudo conectar a Oracle."
                );
            }

            boolean autoCommitAnterior =
                con.getAutoCommit();

            try {
                con.setAutoCommit(false);

                // 1. VALIDAR Y BLOQUEAR STOCK

                for (int i = 0;
                        i < modelo.getRowCount();
                        i++) {

                    String codigo =
                        modelo.getValueAt(
                            i, 0
                        ).toString();

                    int cantidadTotal = 0;

                    for (int j = 0;
                            j < modelo.getRowCount();
                            j++) {

                        if (codigo.equals(
                                modelo.getValueAt(
                                    j, 0
                                ).toString())) {

                            cantidadTotal +=
                                Integer.parseInt(
                                    modelo.getValueAt(
                                        j, 2
                                    ).toString()
                                );
                        }
                    }

                    String sqlStock =
                        "SELECT STOCK "
                        + "FROM JENNIFER.ARTICULO "
                        + "WHERE CODIGO = ? "
                        + "FOR UPDATE";

                    try (
                        PreparedStatement ps =
                            con.prepareStatement(
                                sqlStock
                            )
                    ) {
                        ps.setString(1, codigo);

                        try (
                            ResultSet rs =
                                ps.executeQuery()
                        ) {
                            if (!rs.next()) {
                                throw new SQLException(
                                    "Artículo no encontrado: "
                                    + codigo
                                );
                            }

                            if (rs.getInt("STOCK")
                                    < cantidadTotal) {

                                throw new SQLException(
                                    "Stock insuficiente: "
                                    + codigo
                                );
                            }
                        }
                    }
                }

                // 2. GUARDAR CABECERA

                String sqlCabecera =
                    "BEGIN "
                    + "INSERT INTO JENNIFER.FACTURA_CAB "
                    + "(FECHA, CIUDAD_CODIGO, "
                    + "CLIENTE_RUC, TOTAL) "
                    + "VALUES (?, ?, ?, ?) "
                    + "RETURNING NUMERO INTO ?; "
                    + "END;";

                String numeroFactura;

                try (
                    CallableStatement cs =
                        con.prepareCall(
                            sqlCabecera
                        )
                ) {
                    cs.setDate(1, fechaSql);
                    cs.setString(2, ciudadCodigo);
                    cs.setString(3, clienteRuc);
                    cs.setBigDecimal(
                        4,
                        totalFactura
                    );

                    cs.registerOutParameter(
                        5,
                        Types.VARCHAR
                    );

                    cs.execute();

                    numeroFactura =
                        cs.getString(5);
                }

                if (numeroFactura == null
                        || numeroFactura.isBlank()) {

                    throw new SQLException(
                        "No se generó el número "
                        + "de factura."
                    );
                }

                // 3. GUARDAR DETALLES

                String sqlDetalle =
                    "INSERT INTO JENNIFER.FACTURA_DET "
                    + "(FACTURA_NUMERO, "
                    + "ARTICULO_CODIGO, CANTIDAD, PRECIO) "
                    + "VALUES (?, ?, ?, ?)";

                try (
                    PreparedStatement ps =
                        con.prepareStatement(
                            sqlDetalle
                        )
                ) {
                    for (int i = 0;
                            i < modelo.getRowCount();
                            i++) {

                        ps.setString(
                            1,
                            numeroFactura
                        );

                        ps.setString(
                            2,
                            modelo.getValueAt(
                                i, 0
                            ).toString()
                        );

                        ps.setInt(
                            3,
                            Integer.parseInt(
                                modelo.getValueAt(
                                    i, 2
                                ).toString()
                            )
                        );

                        ps.setBigDecimal(
                            4,
                            new BigDecimal(
                                modelo.getValueAt(
                                    i, 3
                                ).toString()
                            )
                        );

                        ps.executeUpdate();
                    }
                }

                // 4. ACTUALIZAR STOCK

                String sqlActualizarStock =
                    "UPDATE JENNIFER.ARTICULO "
                    + "SET STOCK = STOCK - ? "
                    + "WHERE CODIGO = ?";

                try (
                    PreparedStatement ps =
                        con.prepareStatement(
                            sqlActualizarStock
                        )
                ) {
                    for (int i = 0;
                            i < modelo.getRowCount();
                            i++) {

                        ps.setInt(
                            1,
                            Integer.parseInt(
                                modelo.getValueAt(
                                    i, 2
                                ).toString()
                            )
                        );

                        ps.setString(
                            2,
                            modelo.getValueAt(
                                i, 0
                            ).toString()
                        );

                        if (ps.executeUpdate() != 1) {
                            throw new SQLException(
                                "No se pudo actualizar "
                                + "el stock."
                            );
                        }
                    }
                }

                // 5. INTEGRACIÓN CON CONTABILIDAD

                int idComprobante =
                    generarAsientoContable(
                        con,
                        numeroFactura,
                        fechaSql,
                        totalFactura
                    );

                // 6. CONFIRMAR TODO

                con.commit();

                JOptionPane.showMessageDialog(
                    this,
                    "FACTURA GUARDADA CORRECTAMENTE\n\n"
                    + "Factura N.º: "
                    + numeroFactura
                    + "\nTotal: $"
                    + totalFactura
                    + "\n\n"
                    + "INTEGRACIÓN CONTABLE EXITOSA\n"
                    + "Comprobante N.º: "
                    + idComprobante
                    + "\nEstado: BORRADOR"
                );

                txtNumFactura.setText(
                    numeroFactura
                );

                modelo.setRowCount(0);

                lblTotal.setText(
                    "TOTAL: $0.00"
                );

                cargarDatosIniciales();

            } catch (Exception e) {

                try {
                    con.rollback();
                } catch (SQLException ex) {
                    e.addSuppressed(ex);
                }

                JOptionPane.showMessageDialog(
                    this,
                    "No se guardó la operación.\n\n"
                    + "Detalle: "
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );

            } finally {
                con.setAutoCommit(
                    autoCommitAnterior
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                this,
                "Error de conexión: "
                + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // INTEGRACIÓN FACTURACIÓN -> CONTABILIDAD

    private int generarAsientoContable(
            Connection con,
            String numeroFactura,
            java.sql.Date fecha,
            BigDecimal total) throws SQLException {

        String referencia =
            "FACTURA-NUM-" + numeroFactura;

        // EVITAR ASIENTOS DUPLICADOS

        String sqlExiste =
            "SELECT COUNT(*) "
            + "FROM CONTABILIDAD_APP.COMPROBANTE_CONTABLE "
            + "WHERE OBSERVACIONES = ?";

        try (
            PreparedStatement ps =
                con.prepareStatement(
                    sqlExiste
                )
        ) {
            ps.setString(
                1,
                referencia
            );

            try (
                ResultSet rs =
                    ps.executeQuery()
            ) {
                rs.next();

                if (rs.getInt(1) > 0) {
                    throw new SQLException(
                        "Esta factura ya tiene "
                        + "un asiento contable."
                    );
                }
            }
        }

        // CREAR COMPROBANTE

        String sqlComprobante =
            "BEGIN "
            + "INSERT INTO "
            + "CONTABILIDAD_APP.COMPROBANTE_CONTABLE "
            + "(FECHA, OBSERVACIONES, ESTADO) "
            + "VALUES (?, ?, 'BORRADOR') "
            + "RETURNING ID_COMPROBANTE INTO ?; "
            + "END;";

        int idComprobante;

        try (
            CallableStatement cs =
                con.prepareCall(
                    sqlComprobante
                )
        ) {
            cs.setDate(1, fecha);

            cs.setString(
                2,
                referencia
            );

            cs.registerOutParameter(
                3,
                Types.NUMERIC
            );

            cs.execute();

            idComprobante =
                cs.getInt(3);
        }

        // CUENTA CAJA = 1
        // CUENTA VENTAS = 6

        // DEBE: CAJA

        insertarDetalleContable(
            con,
            idComprobante,
            1,
            total,
            BigDecimal.ZERO
        );

        // HABER: VENTAS

        insertarDetalleContable(
            con,
            idComprobante,
            6,
            BigDecimal.ZERO,
            total
        );

        return idComprobante;
    }

    // GUARDAR DETALLE CONTABLE

    private void insertarDetalleContable(
            Connection con,
            int idComprobante,
            int idCuenta,
            BigDecimal debe,
            BigDecimal haber) throws SQLException {

        String sql =
            "INSERT INTO "
            + "CONTABILIDAD_APP.DETALLE_CONTABLE "
            + "(ID_COMPROBANTE, ID_CUENTA, "
            + "DEBE, HABER) "
            + "VALUES (?, ?, ?, ?)";

        try (
            PreparedStatement ps =
                con.prepareStatement(sql)
        ) {
            ps.setInt(
                1,
                idComprobante
            );

            ps.setInt(
                2,
                idCuenta
            );

            ps.setBigDecimal(
                3,
                debe
            );

            ps.setBigDecimal(
                4,
                haber
            );

            ps.executeUpdate();
        }
    }

    // EJECUTAR VENTANA

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
            () -> new FrmFactura().setVisible(true)
        );
    }
}
