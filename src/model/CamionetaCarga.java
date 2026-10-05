package model;

import java.util.*;

/** Reglas propias de camioneta de carga. */
public class CamionetaCarga extends Vehiculo {
    private static final double RECARGO_POR_TONELADA_DIA = 100.0;
    private double capacidadToneladas;
    public CamionetaCarga(String placa, String marca, String modelo, double tarifaDiaria, double capacidadToneladas) {
        this(placa, marca, modelo, tarifaDiaria, capacidadToneladas, 0);
    }
    public CamionetaCarga(String placa, String marca, String modelo, double tarifaDiaria, double capacidadToneladas, int diasAcumulados) {
        super(placa, marca, modelo, tarifaDiaria, diasAcumulados);
        if (!Double.isFinite(capacidadToneladas) || capacidadToneladas <= 0) throw new IllegalArgumentException("La capacidad debe ser mayor que cero y finita.");
        this.capacidadToneladas = capacidadToneladas;
    }
    public double getCapacidadToneladas() { return capacidadToneladas; }
    @Override public double calcularRecargo(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser enteros positivos.");
        return RECARGO_POR_TONELADA_DIA * capacidadToneladas * dias;
    }
    @Override public Optional<Licencia> getLicenciaRequerida() { return Optional.of(Licencia.B); }
    @Override public int getUmbralMantenimiento() { return 15; }
    @Override public String getCategoria() { return "Camioneta de carga"; }
    @Override public String describirCaracteristicas() { return "Capacidad: " + capacidadToneladas + " toneladas"; }
}