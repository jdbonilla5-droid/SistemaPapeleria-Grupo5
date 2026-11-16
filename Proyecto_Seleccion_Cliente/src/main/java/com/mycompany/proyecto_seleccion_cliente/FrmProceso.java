
package com.mycompany.proyecto_seleccion_cliente;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmProceso extends JFrame {

    private JComboBox<String> cmbCandidato;
    private JComboBox<String> cmbParametro;

    private JTextField txtFecha;
    private JTextField txtCalificacion;

    private JTable tblDetalle;
    private DefaultTableModel modeloDetalle;

    private JButton btnAgregarLinea;
    private JButton btnGuardarEvaluacion;
    private JButton btnContratar;

    public FrmProceso() {

        initComponents();

        // Cargar candidatos desde Oracle mediante sockets
        ProcesoServicio.cargarComboCandidatos(
            cmbCandidato
        );

        // Cargar parámetros de evaluación
        ProcesoServicio.cargarComboParametros(
            cmbParametro
        );

        // Mostrar fecha actual
        txtFecha.setText(
            LocalDate.now().toString()
        );
    }

    // ==========================================
    // CONSTRUCCIÓN DE LA INTERFAZ
    // ==========================================

    private void initComponents() {

        setTitle(
            "Sistema de Selección - Evaluación de Candidatos"
        );

        setSize(950, 620);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
            JFrame.DISPOSE_ON_CLOSE
        );

        JPanel panelPrincipal = new JPanel(
            new BorderLayout(15, 15)
        );

        panelPrincipal.setBorder(
            BorderFactory.createEmptyBorder(
                20, 20, 20, 20
            )
        );

        // ======================================
        // TÍTULO
        // ======================================

        JLabel lblTitulo = new JLabel(
            "EVALUACIÓN Y CONTRATACIÓN DE CANDIDATOS",
            SwingConstants.CENTER
        );

        lblTitulo.setFont(
            new Font("Arial", Font.BOLD, 20)
        );

        panelPrincipal.add(
            lblTitulo,
            BorderLayout.NORTH
        );

        // ======================================
        // CAMPOS DEL FORMULARIO
        // ======================================

        cmbCandidato = new JComboBox<>();
        cmbParametro = new JComboBox<>();

        txtFecha = new JTextField();
        txtFecha.setEditable(false);

        txtCalificacion = new JTextField();

        JPanel panelDatos = new JPanel(
            new GridLayout(3, 2, 12, 12)
        );

        panelDatos.add(
            new JLabel("Candidato:")
        );
        panelDatos.add(cmbCandidato);

        panelDatos.add(
            new JLabel("Fecha:")
        );
        panelDatos.add(txtFecha);

        panelDatos.add(
            new JLabel("Parámetro de evaluación:")
        );
        panelDatos.add(cmbParametro);

        // ======================================
        // CALIFICACIÓN
        // ======================================

        JPanel panelCalificacion = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        panelCalificacion.add(
            new JLabel("Calificación:")
        );

        txtCalificacion.setColumns(12);

        panelCalificacion.add(
            txtCalificacion
        );

        btnAgregarLinea = new JButton(
            "Agregar Línea"
        );

        panelCalificacion.add(
            btnAgregarLinea
        );

        // ======================================
        // PANEL SUPERIOR
        // ======================================

        JPanel panelSuperior = new JPanel(
            new BorderLayout(10, 10)
        );

        panelSuperior.add(
            panelDatos,
            BorderLayout.CENTER
        );

        panelSuperior.add(
            panelCalificacion,
            BorderLayout.SOUTH
        );

        // ======================================
        // TABLA DE EVALUACIONES
        // ======================================

        modeloDetalle = new DefaultTableModel(
            new Object[]{
                "Cód. Parámetro",
                "Parámetro Evaluado",
                "Calificación"
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

        tblDetalle = new JTable(
            modeloDetalle
        );

        tblDetalle.setRowHeight(28);

        tblDetalle.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        JScrollPane scrollTabla =
            new JScrollPane(tblDetalle);

        // ======================================
        // PANEL CENTRAL
        // ======================================

        JPanel panelCentro = new JPanel(
            new BorderLayout(10, 10)
        );

        panelCentro.add(
            panelSuperior,
            BorderLayout.NORTH
        );

        panelCentro.add(
            scrollTabla,
            BorderLayout.CENTER
        );

        panelPrincipal.add(
            panelCentro,
            BorderLayout.CENTER
        );

        // ======================================
        // BOTONES INFERIORES
        // ======================================

        JPanel panelBotones = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 15, 10)
        );

        btnGuardarEvaluacion = new JButton(
            "Guardar Evaluación"
        );

        btnContratar = new JButton(
            "Contratar"
        );

        panelBotones.add(
            btnGuardarEvaluacion
        );

        panelBotones.add(
            btnContratar
        );

        panelPrincipal.add(
            panelBotones,
            BorderLayout.SOUTH
        );

        setContentPane(panelPrincipal);

        // ======================================
        // EVENTOS DE LOS BOTONES
        // ======================================

        btnAgregarLinea.addActionListener(
            e -> agregarLinea()
        );

        btnGuardarEvaluacion.addActionListener(
            e -> guardarEvaluacion()
        );

        btnContratar.addActionListener(
            e -> btnContratarActionPerformed(e)
        );
    }

    // ==========================================
    // AGREGAR LÍNEA DE EVALUACIÓN
    // ==========================================

    private void agregarLinea() {

        if (cmbParametro.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                this,
                "Seleccione un parámetro."
            );

            return;
        }

        String textoCalificacion =
            txtCalificacion.getText().trim();

        if (textoCalificacion.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Ingrese una calificación."
            );

            return;
        }

        String parametroSeleccionado =
            cmbParametro.getSelectedItem().toString();

        String[] partes =
            parametroSeleccionado.split(" - ", 2);

        if (partes.length < 2) {

            JOptionPane.showMessageDialog(
                this,
                "Formato de parámetro incorrecto."
            );

            return;
        }

        String codigoParametro =
            partes[0].trim();

        String nombreParametro =
            partes[1].trim();

        try {

            double calificacion =
                Double.parseDouble(
                    textoCalificacion.replace(',', '.')
                );

            if (!Double.isFinite(calificacion)
                    || calificacion < 0) {

                JOptionPane.showMessageDialog(
                    this,
                    "La calificación debe ser "
                    + "un número válido mayor o igual a cero."
                );

                return;
            }

            // Verificar que el parámetro no se repita
            for (int i = 0;
                 i < modeloDetalle.getRowCount();
                 i++) {

                String codigoExistente =
                    modeloDetalle.getValueAt(
                        i, 0
                    ).toString();

                if (codigoExistente.equals(
                        codigoParametro)) {

                    JOptionPane.showMessageDialog(
                        this,
                        "Este parámetro ya fue agregado."
                    );

                    return;
                }
            }

            // Agregar a la tabla
            modeloDetalle.addRow(
                new Object[]{
                    codigoParametro,
                    nombreParametro,
                    calificacion
                }
            );

            txtCalificacion.setText("");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Ingrese una calificación numérica válida.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // GUARDAR EVALUACIÓN EN ORACLE
    // ==========================================

    private void guardarEvaluacion() {

        if (cmbCandidato.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                this,
                "Seleccione un candidato."
            );

            return;
        }

        if (modeloDetalle.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                this,
                "Agregue al menos una calificación."
            );

            return;
        }

        String candidatoSeleccionado =
            cmbCandidato.getSelectedItem().toString();

        String cedula =
            candidatoSeleccionado.split(
                " - ", 2
            )[0].trim();

        StringBuilder detalles =
            new StringBuilder();

        for (int i = 0;
             i < modeloDetalle.getRowCount();
             i++) {

            String codigoParametro =
                modeloDetalle.getValueAt(
                    i, 0
                ).toString();

            String calificacion =
                modeloDetalle.getValueAt(
                    i, 2
                ).toString();

            detalles.append(codigoParametro)
                    .append(",")
                    .append(calificacion);

            if (i < modeloDetalle.getRowCount() - 1) {

                detalles.append(";");
            }
        }

        int confirmacion =
            JOptionPane.showConfirmDialog(
                this,
                "¿Desea guardar la evaluación "
                + "del candidato seleccionado?",
                "Confirmar evaluación",
                JOptionPane.YES_NO_OPTION
            );

        if (confirmacion != JOptionPane.YES_OPTION) {

            return;
        }

        boolean exito =
            ProcesoServicio.registrarEvaluacion(
                cedula,
                detalles.toString()
            );

        if (exito) {

            JOptionPane.showMessageDialog(
                this,
                "¡Evaluación guardada correctamente "
                + "en Oracle!"
            );

            modeloDetalle.setRowCount(0);
            txtCalificacion.setText("");
        }
    }

    // ==========================================
    // BOTÓN CONTRATAR
    // ==========================================

    private void btnContratarActionPerformed(
            java.awt.event.ActionEvent evt) {

        // Abrir ventana para contratar candidatos
        // con cargos y sueldos individuales

        VentanaContratacion ventana =
            new VentanaContratacion(this);

        ventana.setLocationRelativeTo(this);

        ventana.setVisible(true);
    }

    // ==========================================
    // MÉTODO MAIN
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            FrmProceso ventana = new FrmProceso();

            ventana.setVisible(true);
        });
    }
}
