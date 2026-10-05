package model;

import java.util.*;

/** Conserva los montos cobrados al confirmar, incluso después de la devolución. */
public class Alquiler {
    private final int numero;
    private final Cliente cliente;
    private final Vehiculo vehiculo;
    private final int dias;
    private final double subtotal;
    private final double descuento;
    private final double total;
    private EstadoAlquiler estado;
    public Alquiler(int numero, Cotizacion cotizacion) {
        if (numero <= 0 || cotizacion == null || !cotizacion.esAlquilable())
            throw new IllegalArgumentException("Número o cotización inválidos.");
        this.numero = numero;
        this.cliente = cotizacion.getCliente();
        this.vehiculo = cotizacion.getVehiculo();
        this.dias = cotizacion.getDias();
        this.subtotal = cotizacion.getSubtotal();
        this.descuento = cotizacion.getDescuento();
        this.total = cotizacion.getTotal();
        this.estado = EstadoAlquiler.ACTIVO;
    }
    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public int getDias() { return dias; }
    public double getSubtotal() { return subtotal; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public EstadoAlquiler getEstado() { return estado; }
    public boolean estaActivo() { return estado == EstadoAlquiler.ACTIVO; }
    public void finalizar() {
        if (!estaActivo()) throw new IllegalStateException("El alquiler ya está finalizado.");
        estado = EstadoAlquiler.FINALIZADO;
    }
    @Override public String toString() {
        return String.format(Locale.US,
                "Alquiler #%d | Cliente: %s | Vehículo: %s | Días: %d | Subtotal: Q%,.2f | Descuento: Q%,.2f | Total: Q%,.2f | %s",
                numero, cliente.getIdentificador(), vehiculo.getPlaca(), dias, subtotal, descuento, total, estado.getDescripcion());
    }
}