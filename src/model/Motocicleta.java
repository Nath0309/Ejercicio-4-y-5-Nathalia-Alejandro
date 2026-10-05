package model;

import java.util.*;

/** Reglas propias de motocicleta. */
public class Motocicleta extends Vehiculo {
    private static final int CILINDRAJE_LIMITE = 250;
    private static final double RECARGO_ALTO_CILINDRAJE = 75.0;
    private int cilindraje;
    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, int cilindraje) {
        this(placa, marca, modelo, tarifaDiaria, cilindraje, 0);
    }
    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, int cilindraje, int diasAcumulados) {
        super(placa, marca, modelo, tarifaDiaria, diasAcumulados);
        if (cilindraje <= 0) throw new IllegalArgumentException("El cilindraje debe ser mayor que cero.");
        this.cilindraje = cilindraje;
    }
    public int getCilindraje() { return cilindraje; }
    @Override public double calcularRecargo(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser enteros positivos.");
        return cilindraje > CILINDRAJE_LIMITE ? RECARGO_ALTO_CILINDRAJE : 0;
    }
    @Override public Optional<Licencia> getLicenciaRequerida() { return Optional.of(Licencia.M); }
    @Override public int getUmbralMantenimiento() { return 20; }
    @Override public String getCategoria() { return "Motocicleta"; }
    @Override public String describirCaracteristicas() { return "Cilindraje: " + cilindraje + " cc"; }
}
