
package com.mycompany.proyecto_seleccion_cliente;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VentanaContratacion extends JDialog {

    private final JSpinner cantidad =
        new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));

    private final DefaultTableModel modelo =
        new DefaultTableModel(
            new Object[]{
                "Cédula", "Candidato", "Puntaje",
                "Nombres", "Apellidos", "Cargo", "Sueldo"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return columna >= 3;
            }
        };

    private final JTable tabla = new JTable(modelo);

    private List<ProcesoServicio.Candidato> elegibles =
        new ArrayList<>();

    public VentanaContratacion(Frame propietario) {

        super(propietario,
              "Contratación de mejores candidatos", true);

        setSize(1050, 530);
        setLocationRelativeTo(propietario);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );

        JLabel titulo = new JLabel(
            "Contratación - Selección a Nómina"
        );

        titulo.setFont(
            new Font("Segoe UI", Font.BOLD, 21)
        );

        JPanel arriba = new JPanel(
            new BorderLayout(0, 12)
        );

        arriba.add(titulo, BorderLayout.NORTH);

        JPanel opciones = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        opciones.add(new JLabel(
            "Cantidad de mejores candidatos:"
        ));

        opciones.add(cantidad);

        JButton actualizar =
            new JButton("Actualizar lista");

        opciones.add(actualizar);
        arriba.add(opciones, BorderLayout.SOUTH);

        raiz.add(arriba, BorderLayout.NORTH);

        tabla.setRowHeight(29);
        tabla.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        int[] anchos = {
            105, 180, 75, 150, 150, 170, 90
        };

        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel()
                 .getColumn(i)
                 .setPreferredWidth(anchos[i]);
        }

        raiz.add(
            new JScrollPane(tabla),
            BorderLayout.CENTER
        );

        JPanel abajo = new JPanel(
            new BorderLayout()
        );

        abajo.add(
            new JLabel(
                "Complete nombres y apellidos; "
                + "asigne un cargo distinto y un sueldo "
                + "a cada persona."
            ),
            BorderLayout.NORTH
        );

        JPanel botones = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        JButton cancelar =
            new JButton("Cerrar");

        JButton guardar =
            new JButton("Confirmar contratación");

        botones.add(cancelar);
        botones.add(guardar);

        abajo.add(botones, BorderLayout.SOUTH);
        raiz.add(abajo, BorderLayout.SOUTH);

        setContentPane(raiz);

        actualizar.addActionListener(e -> cargar());
        cantidad.addChangeListener(e -> mostrar());
        cancelar.addActionListener(e -> dispose());
        guardar.addActionListener(e -> guardar());

        cargar();
    }

    private void cargar() {

        try {
            elegibles = ProcesoServicio.listarElegibles();

            int max = Math.max(1, elegibles.size());

            cantidad.setModel(
                new SpinnerNumberModel(
                    Math.min(
                        (Integer) cantidad.getValue(),
                        max
                    ),
                    1, max, 1
                )
            );

            mostrar();

            if (elegibles.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No hay candidatos evaluados "
                    + "pendientes de contratación."
                );
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                this,
                "No se pudo consultar el servidor: "
                + ex.getMessage()
            );
        }
    }

    private void mostrar() {

        modelo.setRowCount(0);

        int n = Math.min(
            (Integer) cantidad.getValue(),
            elegibles.size()
        );

        for (int i = 0; i < n; i++) {

            ProcesoServicio.Candidato c =
                elegibles.get(i);

            modelo.addRow(
                new Object[]{
                    c.cedula,
                    c.nombre,
                    c.puntaje,
                    "",
                    "",
                    "",
                    ""
                }
            );
        }
    }

    private void guardar() {

        if (tabla.isEditing()) {
            tabla.getCellEditor().stopCellEditing();
        }

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "No hay candidatos para contratar."
            );
            return;
        }

        List<ProcesoServicio.Contratado> datos =
            new ArrayList<>();

        Set<String> cargos = new HashSet<>();

        try {

            for (int i = 0;
                 i < modelo.getRowCount();
                 i++) {

                String cedula = valor(i, 0);
                String nombres = valor(i, 3);
                String apellidos = valor(i, 4);
                String cargo = valor(i, 5);
                String sueldo = valor(i, 6);

                if (nombres.isEmpty()
                        || apellidos.isEmpty()
                        || cargo.isEmpty()
                        || sueldo.isEmpty()) {

                    throw new IllegalArgumentException(
                        "Complete todos los campos "
                        + "de la fila " + (i + 1)
                    );
                }

                if (!cargos.add(
                        cargo.toUpperCase(Locale.ROOT))) {

                    throw new IllegalArgumentException(
                        "Los cargos deben ser diferentes: "
                        + cargo
                    );
                }

                double monto = Double.parseDouble(
                    sueldo.replace(',', '.')
                );

                if (!Double.isFinite(monto)
                        || monto <= 0) {

                    throw new IllegalArgumentException(
                        "Sueldo incorrecto en fila "
                        + (i + 1)
                    );
                }

                datos.add(
                    new ProcesoServicio.Contratado(
                        cedula,
                        nombres,
                        apellidos,
                        cargo,
                        monto
                    )
                );
            }

            int confirmar =
                JOptionPane.showConfirmDialog(
                    this,
                    "¿Registrar " + datos.size()
                    + " contrataciones en Selección "
                    + "y Nómina?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
                );

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            ProcesoServicio.contratarLote(datos);

            JOptionPane.showMessageDialog(
                this,
                "Contratación registrada correctamente "
                + "en Selección y Nómina."
            );

            cargar();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                this,
                "Escriba sueldos numéricos válidos."
            );

        } catch (IllegalArgumentException
                 | IOException ex) {

            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private String valor(int fila, int columna) {

        Object v = modelo.getValueAt(
            fila, columna
        );

        return v == null
            ? ""
            : v.toString().trim();
    }
}
