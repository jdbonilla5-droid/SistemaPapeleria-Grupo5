
package com.papeleria.nomina.negocio;

import com.papeleria.nomina.persistencia.EmpleadoDAO;
import java.sql.SQLException;
import java.util.List;

public class EmpleadoServicio {

    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    public List<Empleado> listarEmpleados() throws SQLException {
        return empleadoDAO.listar();
    }

    public void registrarEmpleado(Empleado empleado) throws SQLException {
        if (empleado == null) {
            throw new IllegalArgumentException("Empleado no válido");
        }

        empleadoDAO.insertar(empleado);
    }

    public boolean actualizarEmpleado(Empleado empleado)
            throws SQLException {
        return empleadoDAO.actualizar(empleado);
    }

    public boolean eliminarEmpleado(int id) throws SQLException {
        return empleadoDAO.eliminar(id);
    }

    public Empleado buscarEmpleado(int id) throws SQLException {
        return empleadoDAO.buscarPorId(id);
    }

    public Empleado buscarPorCedula(String cedula)
            throws SQLException {
        return empleadoDAO.buscarPorCedula(cedula);
    }
}
