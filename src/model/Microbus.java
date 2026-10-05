package model;

import java.util.*;

/** Reglas propias de microbús. */
public class Microbus extends VehiculoPasajeros {
    private static final double RECARGO_PILOTO_DIARIO = 250.0;
    private boolean incluyePiloto;
    public Microbus(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros, boolean incluyePiloto) {
        this(placa, marca, modelo, tarifaDiaria, cantidadPasajeros, incluyePiloto, 0);
    }
    public Microbus(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros, boolean incluyePiloto, int diasAcumulados) {
        super(placa, marca, modelo, tarifaDiaria, cantidadPasajeros, diasAcumulados);
        
        this.incluyePiloto = incluyePiloto;
    }
    public boolean isIncluyePiloto() { return incluyePiloto; }
    @Override public double calcularRecargo(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser enteros positivos.");
        return incluyePiloto ? RECARGO_PILOTO_DIARIO * dias : 0;
    }
    @Override public Optional<Licencia> getLicenciaRequerida() { return incluyePiloto ? Optional.empty() : Optional.of(Licencia.B); }
    @Override public int getUmbralMantenimiento() { return 25; }
    @Override public String getCategoria() { return "Microbús"; }
    @Override public String describirCaracteristicas() { return super.describirCaracteristicas() + " | Piloto: " + (incluyePiloto ? "incluido" : "no incluido"); }
}
