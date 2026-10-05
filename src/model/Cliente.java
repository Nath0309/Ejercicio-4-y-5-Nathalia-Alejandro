package model;

import java.util.*;

/** Datos y operaciones comunes de los clientes; no utiliza la consola. */
public abstract class Cliente {
    private String identificador;
    private String nombre;
    private Set<Licencia> licencias;
    private List<Alquiler> alquileres;
    protected Cliente(String identificador, String nombre, Set<Licencia> licencias) {
        if (identificador == null || identificador.trim().isEmpty() || nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("Identificador y nombre son obligatorios.");
        if (licencias == null || licencias.isEmpty())
            throw new IllegalArgumentException("Debe presentar al menos una licencia válida.");
        for (Licencia licencia : licencias)
            if (licencia == null) throw new IllegalArgumentException("Licencia inválida.");
        this.identificador = identificador.trim(); this.nombre = nombre.trim();
        this.licencias = EnumSet.copyOf(licencias); this.alquileres = new ArrayList<>();
    }
    public String getIdentificador() { return identificador; }
    public String getNombre() { return nombre; }
    public Set<Licencia> getLicencias() { return Collections.unmodifiableSet(licencias); }
    public List<Alquiler> getAlquileres() { return Collections.unmodifiableList(alquileres); }
    public abstract double calcularDescuento(double subtotal);
    public abstract int getLimiteAlquileresActivos();
    public abstract String getTipoCliente();
    public abstract String describirDatos();
    public int contarAlquileresActivos() {
        int cantidad = 0;
        for (Alquiler alquiler : alquileres) if (alquiler.estaActivo()) cantidad++;
        return cantidad;
    }
    public boolean puedeTomarOtroAlquiler() { return contarAlquileresActivos() < getLimiteAlquileresActivos(); }
    public boolean tieneLicenciaPara(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("Vehículo obligatorio.");
        return vehiculo.admiteConductor(licencias);
    }
    public void registrarAlquiler(Alquiler alquiler) {
        if (alquiler == null || alquiler.getCliente() != this || alquileres.contains(alquiler))
            throw new IllegalArgumentException("Alquiler nulo, duplicado o de otro cliente.");
        alquileres.add(alquiler);
    }
    public double calcularTotalPagado() {
        double total = 0;
        for (Alquiler alquiler : alquileres) total += alquiler.getTotal();
        return total;
    }
    @Override public String toString() {
        return getTipoCliente() + " | " + identificador + " | " + nombre + " | Licencias: " + licencias + " | " + describirDatos();
    }
}
