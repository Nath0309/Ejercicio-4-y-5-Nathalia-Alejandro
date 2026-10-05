package model;

public enum EstadoAlquiler {
    ACTIVO("Activo"), FINALIZADO("Finalizado");
    private String descripcion;
    private EstadoAlquiler(String descripcion) { this.descripcion = descripcion; }
    public String getDescripcion() { return descripcion; }
}
