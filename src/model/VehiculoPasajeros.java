package model;

/** Comparte la cantidad de pasajeros entre automóvil y microbús. */
public abstract class VehiculoPasajeros extends Vehiculo {
    private int cantidadPasajeros;
    protected VehiculoPasajeros(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros) {
        this(placa, marca, modelo, tarifaDiaria, cantidadPasajeros, 0);
    }
    protected VehiculoPasajeros(String placa, String marca, String modelo, double tarifaDiaria,
            int cantidadPasajeros, int diasAcumulados) {
        super(placa, marca, modelo, tarifaDiaria, diasAcumulados);
        if (cantidadPasajeros <= 0) throw new IllegalArgumentException("Los pasajeros deben ser mayores que cero.");
        this.cantidadPasajeros = cantidadPasajeros;
    }
    public int getCantidadPasajeros() { return cantidadPasajeros; }
    @Override public String describirCaracteristicas() { return "Pasajeros: " + cantidadPasajeros; }
}
