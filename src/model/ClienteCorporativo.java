package model;

import java.util.*;

public class ClienteCorporativo extends Cliente {
    private static final int LIMITE_ACTIVOS = 3;
    private static final double PORCENTAJE_DESCUENTO = 0.10;
    private String nombreContacto;
    public ClienteCorporativo(String nit, String nombreEmpresa, String nombreContacto, Set<Licencia> licencias) {
        super(nit, nombreEmpresa, licencias);
        if (nombreContacto == null || nombreContacto.trim().isEmpty())
            throw new IllegalArgumentException("El contacto es obligatorio.");
        this.nombreContacto = nombreContacto.trim();
    }
    public String getNombreContacto() { return nombreContacto; }
    @Override public double calcularDescuento(double subtotal) {
        if (!Double.isFinite(subtotal) || subtotal < 0) throw new IllegalArgumentException("Subtotal inválido.");
        return subtotal * PORCENTAJE_DESCUENTO;
    }
    @Override public int getLimiteAlquileresActivos() { return LIMITE_ACTIVOS; }
    @Override public String getTipoCliente() { return "Corporativo"; }
    @Override public String describirDatos() { return "NIT: " + getIdentificador() + " | Contacto: " + nombreContacto; }
}
