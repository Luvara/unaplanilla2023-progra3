/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cr.ac.una.unaplanillaws.service;

import cr.ac.una.unaplanillaws.model.Empleado;
import cr.ac.una.unaplanillaws.model.EmpleadoDto;
import cr.ac.una.unaplanillaws.model.TipoPlanilla;
import cr.ac.una.unaplanillaws.model.TipoPlanillaDto;
import cr.ac.una.unaplanillaws.util.CodigoRespuesta;
import cr.ac.una.unaplanillaws.util.Respuesta;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Carlos
 */
// TODO
@Stateless
@LocalBean
public class TipoPlanillaService {

    // TODO
    @PersistenceContext(unitName = "UnaPlanillaWsPU")
    // TODO
    private EntityManager em;

    public Respuesta getTipoPlanilla(Long id) {
        try {
            Query qryTipoPlanilla = em.createNamedQuery("TipoPlanilla.findById", TipoPlanilla.class);
            qryTipoPlanilla.setParameter("id", id);

            TipoPlanilla tipoPlanilla = (TipoPlanilla) qryTipoPlanilla.getSingleResult();
            TipoPlanillaDto tipoPlanillaDto = new TipoPlanillaDto(tipoPlanilla);
            for (Empleado emp : tipoPlanilla.getEmpleados()) {
                tipoPlanillaDto.getEmpleados().add(new EmpleadoDto(emp));
            }
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "TipoPlanilla", tipoPlanillaDto);

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existe un tipo de planilla con el código ingresado.", "getTipoPlanilla NoResultException");
        } catch (NonUniqueResultException ex) {
            //LOG.log(Level.SEVERE, "Ocurrio un error al consultar el tipo de planilla.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al consultar el tipo de planilla.", "getTipoPlanilla NonUniqueResultException");
        } catch (Exception ex) {
            //LOG.log(Level.SEVERE, "Ocurrio un error al consultar el empleado.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al consultar el tipo de planilla.", "getTipoPlanilla " + ex.getMessage());
        }
    }

    public Respuesta guardarTipoPlanilla(TipoPlanillaDto tipoPlanillaDto) {
        try {
            TipoPlanilla tipoPlanilla;
            if (tipoPlanillaDto.getId() != null && tipoPlanillaDto.getId() > 0) {
                tipoPlanilla = em.find(TipoPlanilla.class, tipoPlanillaDto.getId());
                if (tipoPlanilla == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encontró el tipo de planilla a modificar.", "guardarTipoPlanilla NoResultException");
                }
                tipoPlanilla.actualizar(tipoPlanillaDto);
                for (EmpleadoDto emp : tipoPlanillaDto.getEmpleadosEliminados()) {
                    tipoPlanilla.getEmpleados().remove(new Empleado(emp.getId()));
                }
                if (!tipoPlanillaDto.getEmpleados().isEmpty()) {
                    for (EmpleadoDto emp : tipoPlanillaDto.getEmpleados()) {
                        if (emp.getModificado()) {
                            Empleado empleado = em.find(Empleado.class, emp.getId());
                            empleado.getTiposPlanilla().add(tipoPlanilla);
                            tipoPlanilla.getEmpleados().add(empleado);
                        }
                    }
                }
                tipoPlanilla = em.merge(tipoPlanilla);
            } else {
                tipoPlanilla = new TipoPlanilla(tipoPlanillaDto);
                em.persist(tipoPlanilla);
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "TipoPlanilla", new TipoPlanillaDto(tipoPlanilla));
        } catch (Exception ex) {
            //LOG.log(Level.SEVERE, "Ocurrio un error al guardar el tipo de planilla.", ex);
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al guardar el tipo de planilla.", "guardarTipoPlanilla " + ex.getMessage());
        }
    }

    public Respuesta eliminarTipoPlanilla(Long id) {
        try {
            TipoPlanilla tipoPlanilla;
            if (id != null && id > 0) {
                tipoPlanilla = em.find(TipoPlanilla.class, id);
                if (tipoPlanilla == null) {
                    return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No se encrontró el tipo de planilla a eliminar.", "eliminarTipoPlanilla NoResultException");
                }
                em.remove(tipoPlanilla);
            } else {
                return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "Debe cargar el tipo de planilla a eliminar.", "eliminarTipoPlanilla NoResultException");
            }
            em.flush();
            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "");
        } catch (Exception ex) {
            if (ex.getCause() != null && ex.getCause().getCause().getClass() == SQLIntegrityConstraintViolationException.class) {
                return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "No se puede eliminar el tipo de planilla porque tiene relaciones con otros registros.", "eliminarTipoPlanilla " + ex.getMessage());
            }
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error al eliminar el tipo de planilla.", "eliminarTipoPlanilla " + ex.getMessage());
        }
    }

    public Respuesta getPlanillas(String codigo, String descripcion, String planillaMes) {
        try {
            Query qryPlanilla = em.createNamedQuery("TipoPlanilla.findAll", TipoPlanilla.class);

            List<TipoPlanilla> planillas = qryPlanilla.getResultList();

            List<TipoPlanilla> planillasFiltro = planillas.stream()
                    .filter(plan -> (codigo.equals("%") || plan.getCodigo().contains(codigo)))
                    .filter(plan -> (descripcion.equals("%") || plan.getDescripcion().toLowerCase().contains(descripcion.toLowerCase())))
                    .filter(plan -> (planillaMes.equals("%") || plan.getPlanillaPorMes().toString().contains(planillaMes)))
                    .collect(Collectors.toList());
            
            
            List<TipoPlanillaDto> planillaDto = new ArrayList<>();

            if (codigo.equals("%") && descripcion.equals("%") && planillaMes.equals("%")) {
                for (TipoPlanilla pla : planillas) {
                    planillaDto.add(new TipoPlanillaDto(pla));
                }
            } else {
                for (TipoPlanilla pla : planillasFiltro) {
                    planillaDto.add(new TipoPlanillaDto(pla));
                }
            }

            return new Respuesta(true, CodigoRespuesta.CORRECTO, "", "", "TipoPlanilla", planillaDto);

        } catch (NoResultException ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_NOENCONTRADO, "No existen planillas con estos datos.", "getPlanillas NoResultException");
        } catch (Exception ex) {
            return new Respuesta(false, CodigoRespuesta.ERROR_INTERNO, "Ocurrio un error en la consulta.", "getPlanillas " + ex.getMessage());
        }
    }
}
