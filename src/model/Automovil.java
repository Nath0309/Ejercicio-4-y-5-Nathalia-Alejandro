package model;

import java.util.*;

/** Reglas propias de automóvil. */
public class Automovil extends VehiculoPasajeros {
    private static final double RECARGO_AUTOMATICA_DIARIO = 50.0;
    private boolean transmisionAutomatica;
    public Automovil(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros, boolean transmisionAutomatica) {
        this(placa, marca, modelo, tarifaDiaria, cantidadPasajeros, transmisionAutomatica, 0);
    }
    public Automovil(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros, boolean transmisionAutomatica, int diasAcumulados) {
        super(placa, marca, modelo, tarifaDiaria, cantidadPasajeros, diasAcumulados);
        
        this.transmisionAutomatica = transmisionAutomatica;
    }
    public boolean isTransmisionAutomatica() { return transmisionAutomatica; }
    @Override public double calcularRecargo(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días deben ser enteros positivos.");
        return transmisionAutomatica ? RECARGO_AUTOMATICA_DIARIO * dias : 0;
    }
    @Override public Optional<Licencia> getLicenciaRequerida() { return Optional.of(Licencia.C); }
    @Override public int getUmbralMantenimiento() { return 30; }
    @Override public String getCategoria() { return "Automóvil"; }
    @Override public String describirCaracteristicas() { return super.describirCaracteristicas() + " | Transmisión: " + (transmisionAutomatica ? "automática" : "manual"); }
}
