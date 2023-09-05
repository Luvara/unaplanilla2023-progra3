package cr.ac.una.unaplanilla.model;

/**
 *
 * @author BiblioPZ UNA
 */
public class Salonero {

    private Integer edad;
    private Boolean temporal;
    private Double salarioBase;
    private Double propina;

    public Salonero() {
    }

    public Salonero(Integer edad, Boolean temporal, Double salarioBase, Double propina) {
        this.edad = edad;
        this.temporal = temporal;
        this.salarioBase = salarioBase;
        this.propina = propina;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Boolean getTemporal() {
        return temporal;
    }

    public void setTemporal(Boolean temporal) {
        this.temporal = temporal;
    }

    public Double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(Double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public Double getPropina() {
        return propina;
    }

    public void setPropina(Double propina) {
        this.propina = propina;
    }
}
