package model;

import java.util.*;

/** Datos comunes, cálculo plantilla y transiciones del vehículo. */
public abstract class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private double tarifaDiaria;
    private EstadoVehiculo estado;
    private int diasAcumulados;
    protected Vehiculo(String placa, String marca, String modelo, double tarifaDiaria) {
        this(placa, marca, modelo, tarifaDiaria, 0);
    }
    protected Vehiculo(String placa, String marca, String modelo, double tarifaDiaria, int diasAcumulados) {
        if (placa == null || placa.trim().isEmpty() || marca == null || marca.trim().isEmpty()
                || modelo == null || modelo.trim().isEmpty())
            throw new IllegalArgumentException("Placa, marca y modelo son obligatorios.");
        if (!Double.isFinite(tarifaDiaria) || tarifaDiaria <= 0)
            throw new IllegalArgumentException("La tarifa debe ser mayor que cero y finita.");
        if (diasAcumulados < 0) throw new IllegalArgumentException("El acumulado no puede ser negativo.");
        this.placa = placa.trim(); this.marca = marca.trim(); this.modelo = modelo.trim();
        this.tarifaDiaria = tarifaDiaria; this.diasAcumulados = diasAcumulados;
        this.estado = EstadoVehiculo.DISPONIBLE;
    }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public double getTarifaDiaria() { return tarifaDiaria; }
    public EstadoVehiculo getEstado() { return estado; }
    public int getDiasAcumulados() { return diasAcumulados; }
    public double calcularSubtotal(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser enteros positivos.");
        double subtotal = tarifaDiaria * dias + calcularRecargo(dias);
        if (!Double.isFinite(subtotal)) throw new IllegalArgumentException("El monto excede el rango permitido.");
        return subtotal;
    }
    public abstract double calcularRecargo(int dias);
    public abstract Optional<Licencia> getLicenciaRequerida();
    public abstract int getUmbralMantenimiento();
    public abstract String getCategoria();
    public abstract String describirCaracteristicas();
    public boolean admiteConductor(Set<Licencia> licencias) {
        Optional<Licencia> requerida = getLicenciaRequerida();
        if (!requerida.isPresent()) return true;
        if (licencias == null) return false;
        for (Licencia licencia : licencias)
            if (licencia != null && licencia.autoriza(requerida.get())) return true;
        return false;
    }
    public boolean estaDisponible() { return estado == EstadoVehiculo.DISPONIBLE; }
    public void marcarAlquilado() {
        if (!estaDisponible()) throw new IllegalStateException("El vehículo no está disponible.");
        estado = EstadoVehiculo.ALQUILADO;
    }
    public void registrarDevolucion(int diasAlquilados) {
        if (estado != EstadoVehiculo.ALQUILADO) throw new IllegalStateException("El vehículo no está alquilado.");
        if (diasAlquilados <= 0 || diasAlquilados > Integer.MAX_VALUE - diasAcumulados)
            throw new IllegalArgumentException("Cantidad de días inválida para el acumulado.");
        diasAcumulados += diasAlquilados;
        estado = diasAcumulados >= getUmbralMantenimiento()
                ? EstadoVehiculo.EN_MANTENIMIENTO : EstadoVehiculo.DISPONIBLE;
    }
    public void finalizarMantenimiento() {
        if (estado != EstadoVehiculo.EN_MANTENIMIENTO)
            throw new IllegalStateException("El vehículo no está en mantenimiento.");
        estado = EstadoVehiculo.DISPONIBLE; diasAcumulados = 0;
    }
    @Override public String toString() {
        return String.format(Locale.US, "%s | %s | %s %s | Q%,.2f/día | %s | %s",
                placa, getCategoria(), marca, modelo, tarifaDiaria, estado.getDescripcion(), describirCaracteristicas());
    }
}
