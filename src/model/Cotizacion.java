package model;

import java.util.*;

/** Consulta de precio sin cambios en vehículos, historiales o ingresos. */
public class Cotizacion {
    private final Cliente cliente;
    private final Vehiculo vehiculo;
    private final int dias;
    private final double subtotal;
    private final double descuento;
    private final double total;
    private final List<String> motivosRechazo;
    public Cotizacion(Cliente cliente, Vehiculo vehiculo, int dias) {
        if (cliente == null || vehiculo == null) throw new IllegalArgumentException("Cliente y vehículo obligatorios.");
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser enteros positivos.");
        this.cliente = cliente; this.vehiculo = vehiculo; this.dias = dias;
        this.subtotal = vehiculo.calcularSubtotal(dias);
        this.descuento = cliente.calcularDescuento(subtotal);
        this.total = subtotal - descuento;
        this.motivosRechazo = new ArrayList<>();
        if (!vehiculo.estaDisponible()) motivosRechazo.add("Vehículo no disponible: " + vehiculo.getEstado().getDescripcion());
        if (!cliente.tieneLicenciaPara(vehiculo)) motivosRechazo.add("Licencia inadecuada para este vehículo.");
        if (!cliente.puedeTomarOtroAlquiler()) motivosRechazo.add("Límite de alquileres activos alcanzado.");
    }
    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public int getDias() { return dias; }
    public double getSubtotal() { return subtotal; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public List<String> getMotivosRechazo() { return Collections.unmodifiableList(motivosRechazo); }
    public boolean esAlquilable() { return motivosRechazo.isEmpty(); }
}
