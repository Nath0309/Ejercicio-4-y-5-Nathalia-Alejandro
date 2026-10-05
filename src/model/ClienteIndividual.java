package model;

import java.util.*;

public class ClienteIndividual extends Cliente {
    private static final int DIGITOS_DPI = 13;
    private static final int LIMITE_ACTIVOS = 1;
    private static final int ALQUILERES_SIN_DESCUENTO = 3;
    private static final double PORCENTAJE_DESCUENTO = 0.05;
    private int alquileresPrevios;
    public ClienteIndividual(String dpi, String nombre, Set<Licencia> licencias) { this(dpi, nombre, licencias, 0); }
    public ClienteIndividual(String dpi, String nombre, Set<Licencia> licencias, int alquileresPrevios) {
        super(dpi, nombre, licencias);
        if (!getIdentificador().matches("[0-9]{" + DIGITOS_DPI + "}"))
            throw new IllegalArgumentException("El DPI debe tener exactamente 13 dígitos.");
        if (alquileresPrevios < 0) throw new IllegalArgumentException("El historial previo no puede ser negativo.");
        this.alquileresPrevios = alquileresPrevios;
    }
    public int contarAlquileresConfirmados() { return alquileresPrevios + getAlquileres().size(); }
    @Override public double calcularDescuento(double subtotal) {
        if (!Double.isFinite(subtotal) || subtotal < 0) throw new IllegalArgumentException("Subtotal inválido.");
        return contarAlquileresConfirmados() >= ALQUILERES_SIN_DESCUENTO ? subtotal * PORCENTAJE_DESCUENTO : 0;
    }
    @Override public int getLimiteAlquileresActivos() { return LIMITE_ACTIVOS; }
    @Override public String getTipoCliente() { return "Individual"; }
    @Override public String describirDatos() {
        return "DPI: " + getIdentificador() + " | Alquileres confirmados: " + contarAlquileresConfirmados();
    }
}
