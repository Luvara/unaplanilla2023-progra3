/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cr.ac.una.unaplanilla.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.ws.rs.core.GenericType;
import cr.ac.una.unaplanilla.model.EmpleadoDto;
import cr.ac.una.unaplanilla.util.Request;
import cr.ac.una.unaplanilla.util.Respuesta;
import jakarta.ws.rs.core.GenericType;

/**
 *
 * @author Carlos
 */
public class EmpleadoService {

    public Respuesta getUsuario(String usuario, String clave) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("usuario", usuario);
            parametros.put("clave", clave);
            Request request = new Request("EmpleadoController/usuario", "/{usuario}/{clave}", parametros);
            request.getToken();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            EmpleadoDto empleadoDto = (EmpleadoDto) request.readEntity(EmpleadoDto.class);
            return new Respuesta(true, "", "", "Empleado", empleadoDto);

        } catch (Exception ex) {
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Error obteniendo el usuario [" + usuario + "]", ex);
            return new Respuesta(false, "Error obteniendo el usuario.", "getUsuario " + ex.getMessage());
        }
    }

    public Respuesta getEmpleado(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("EmpleadoController/empleado", "/{id}/", parametros);
            request.get();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            
            EmpleadoDto empleadoDto = (EmpleadoDto) request.readEntity(EmpleadoDto.class);
            return new Respuesta(true, "", "", "Empleado", empleadoDto);

        } catch (Exception ex) {
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Error obteniendo el empleado [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el empleado.", "getEmpleado " + ex.getMessage());
        }
    }

    public Respuesta getEmpleados(String cedula, String nombre, String pApellido) {
        try {
           Map<String, Object> parametros = new HashMap<>();
            parametros.put("cedula", cedula);
            parametros.put("nombre", nombre);
            parametros.put("pApellido", pApellido);
            
            Request request = new Request("EmpleadoController/empleados", "/{usuario}/{clave}/{pApellido}", parametros);
            request.get();
            
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            
            List <EmpleadoDto> empleados;

            empleados = (List<EmpleadoDto>) request.readEntity(new GenericType<List<EmpleadoDto>>(){});
            return new Respuesta(true, "", "", "Empleado", empleados);
            
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Error obteniendo empleados.", ex);
            return new Respuesta(false, "Error obteniendo empleados.", "getEmpleados " + ex.getMessage());
        }
    }    
    
    public Respuesta renovarToken() {
        try {
            Request request = new Request("EmpleadoController/renovar");
            request.getRenewal();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }
            String token = (String) request.readEntity(String.class);
            return new Respuesta(true, "", "", "Token", token);
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Error obteniendo el token", ex);
            return new Respuesta(false, "Error renovando el token.", "renovarToken " + ex.getMessage());
        }
    }

    public Respuesta guardarEmpleado(EmpleadoDto empleado) {
        try {
            Request request = new Request("EmpleadoController/empleado");
            request.post(empleado);
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            EmpleadoDto empleadoDto = (EmpleadoDto) request.readEntity(EmpleadoDto.class);
            return new Respuesta(true, "", "", "Empleado", empleadoDto);

        } catch (Exception ex) {
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Error guardando el empleado.", ex);
            return new Respuesta(false, "Error guardando el empleado.", "guardarEmpleado " + ex.getMessage());
        }
    }

    public Respuesta eliminarEmpleado(Long id) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("id", id);
            Request request = new Request("EmpleadoController/empleado", "/{id}/", parametros);
            request.delete();
            if (request.isError()) {
                return new Respuesta(false, request.getError(), "");
            }

            return new Respuesta(true, "", "");

        } catch (Exception ex) {
            Logger.getLogger(EmpleadoService.class.getName()).log(Level.SEVERE, "Error eliminando el empleado.", ex);
            return new Respuesta(false, "Error eliminando el empleado.", "eliminarEmpleado " + ex.getMessage());
        }
    }
}
