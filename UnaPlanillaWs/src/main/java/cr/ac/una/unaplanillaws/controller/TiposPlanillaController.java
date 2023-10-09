/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.unaplanillaws.controller;

import cr.ac.una.unaplanillaws.model.EmpleadoDto;
import cr.ac.una.unaplanillaws.model.TipoPlanillaDto;
import cr.ac.una.unaplanillaws.service.TipoPlanillaService;
import cr.ac.una.unaplanillaws.util.CodigoRespuesta;
import cr.ac.una.unaplanillaws.util.Respuesta;
import cr.ac.una.unaplanillaws.util.Secure;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BiblioPZ UNA
 */
@Path("/TiposPlanillaController")
@Tag(name = "Planillas", description = "Operaciones sobre planillas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Secure
public class TiposPlanillaController {

    @EJB
    TipoPlanillaService planillaService;

    @GET
    @Path("/planilla/{id}")
    public Response getPlanilla(@PathParam("id") Long id) {
        try {
            Respuesta res = planillaService.getTipoPlanilla(id);

            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok(res.getResultado("TipoPlanilla")).build();

        } catch (Exception ex) {
            Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error obteniendo la planilla").build();
        }
    }

    @POST
    @Path("/planilla")
    public Response guardarPlanilla(TipoPlanillaDto tipoPlanillaDto) {
        try {
            Respuesta res = planillaService.guardarTipoPlanilla(tipoPlanillaDto);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(res.getResultado("TipoPlanilla")).build();

        } catch (Exception ex) {
            Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error guardando la planilla").build();
        }
    }
    @DELETE
    @Path("/planilla/{id}")
    public Response eliminarPlanilla(@PathParam("id") Long id) {
        try {
            Respuesta res = planillaService.eliminarTipoPlanilla(id);
            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }
            return Response.ok(res.getResultado("TipoPlanilla")).build();

        } catch (Exception ex) {
            Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error eliminando la planilla").build();
        }
    }
    

    @GET
    @Path("/planillas/{codigo}/{descripcion}/{planillaMes}")
    public Response getPlanillas(@PathParam("codigo") String codigo, @PathParam("descripcion") String descripcion, @PathParam("planillaMes") String planillaMes) {
        try {
            Respuesta res = planillaService.getPlanillas(codigo, descripcion, planillaMes);

            if (!res.getEstado()) {
                return Response.status(res.getCodigoRespuesta().getValue()).entity(res.getMensaje()).build();
            }

            return Response.ok(new GenericEntity<List<TipoPlanillaDto>>((List<TipoPlanillaDto>) res.getResultado("TipoPlanilla")) {
            }).build();
            
        } catch (Exception ex) {
            Logger.getLogger(EmpleadoController.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(CodigoRespuesta.ERROR_INTERNO.getValue()).entity("Error obteniendo la planilla").build();
        }
    }
    
}
