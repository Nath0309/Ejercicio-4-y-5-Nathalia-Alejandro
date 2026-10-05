package model;

import java.util.*;

/** Administra las colecciones y coordina operaciones sin leer ni imprimir. */
public class EmpresaRentaMovil {
    private String nombre;
    private List<Vehiculo> flota;
    private List<Cliente> clientes;
    private List<Alquiler> alquileres;
    private int siguienteNumeroAlquiler;
    public EmpresaRentaMovil(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("Nombre obligatorio.");
        this.nombre = nombre.trim(); flota = new ArrayList<>(); clientes = new ArrayList<>();
        alquileres = new ArrayList<>(); siguienteNumeroAlquiler = 1;
    }
    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("Vehículo obligatorio.");
        for (Vehiculo existente : flota)
            if (existente.getPlaca().equalsIgnoreCase(vehiculo.getPlaca()))
                throw new IllegalArgumentException("La placa ya está registrada.");
        flota.add(vehiculo);
    }
    public void registrarCliente(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente obligatorio.");
        for (Cliente existente : clientes)
            if (existente.getIdentificador().equalsIgnoreCase(cliente.getIdentificador()))
                throw new IllegalArgumentException("El identificador ya está registrado.");
        clientes.add(cliente);
    }
    public Vehiculo buscarVehiculo(String placa) {
        if (placa == null || placa.trim().isEmpty()) throw new IllegalArgumentException("Placa obligatoria.");
        for (Vehiculo vehiculo : flota)
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) return vehiculo;
        throw new IllegalArgumentException("No existe un vehículo con esa placa.");
    }
    public Cliente buscarCliente(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) throw new IllegalArgumentException("Identificador obligatorio.");
        for (Cliente cliente : clientes)
            if (cliente.getIdentificador().equalsIgnoreCase(identificador.trim())) return cliente;
        throw new IllegalArgumentException("No existe un cliente con ese identificador.");
    }
    public List<Vehiculo> consultarFlota() { return Collections.unmodifiableList(flota); }
    public List<Cliente> consultarClientes() { return Collections.unmodifiableList(clientes); }
    public Cotizacion cotizar(String placa, String identificador, int dias) {
        return new Cotizacion(buscarCliente(identificador), buscarVehiculo(placa), dias);
    }
    public Alquiler confirmarAlquiler(Cotizacion cotizacion) {
        if (cotizacion == null) throw new IllegalArgumentException("Cotización obligatoria.");
        Vehiculo vehiculo = buscarVehiculo(cotizacion.getVehiculo().getPlaca());
        Cliente cliente = buscarCliente(cotizacion.getCliente().getIdentificador());
        if (vehiculo != cotizacion.getVehiculo() || cliente != cotizacion.getCliente())
            throw new IllegalArgumentException("La cotización no pertenece a los registros de esta empresa.");
        // Revalidar antes de cambiar estados, ingresos o conteos.
        Cotizacion actual = new Cotizacion(cliente, vehiculo, cotizacion.getDias());
        if (!actual.esAlquilable()) throw new IllegalStateException(String.join("; ", actual.getMotivosRechazo()));
        if (Double.compare(actual.getTotal(), cotizacion.getTotal()) != 0)
            throw new IllegalStateException("El precio cambió. Solicite una nueva cotización y confirme su monto.");
        if (actual.getDias() > Integer.MAX_VALUE - vehiculo.getDiasAcumulados())
            throw new IllegalArgumentException("Los días exceden el rango del acumulado.");
        if (siguienteNumeroAlquiler == Integer.MAX_VALUE)
            throw new IllegalStateException("Se alcanzó el límite de números de alquiler.");
        Alquiler alquiler = new Alquiler(siguienteNumeroAlquiler, actual);
        vehiculo.marcarAlquilado();
        cliente.registrarAlquiler(alquiler);
        alquileres.add(alquiler);
        siguienteNumeroAlquiler++;
        return alquiler;
    }
    public Alquiler registrarDevolucion(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo.getEstado() != EstadoVehiculo.ALQUILADO)
            throw new IllegalStateException("El vehículo no está alquilado.");
        Alquiler alquiler = buscarAlquilerActivo(vehiculo);
        vehiculo.registrarDevolucion(alquiler.getDias());
        alquiler.finalizar();
        return alquiler;
    }
    public void registrarFinMantenimiento(String placa) { buscarVehiculo(placa).finalizarMantenimiento(); }
    public Map<String, Map<EstadoVehiculo, Integer>> resumirFlotaPorCategoria() {
        Map<String, Map<EstadoVehiculo, Integer>> resumen = new LinkedHashMap<>();
        for (Vehiculo vehiculo : flota) {
            String categoria = vehiculo.getCategoria();
            if (!resumen.containsKey(categoria)) {
                Map<EstadoVehiculo, Integer> estados = new EnumMap<>(EstadoVehiculo.class);
                for (EstadoVehiculo estado : EstadoVehiculo.values()) estados.put(estado, 0);
                resumen.put(categoria, estados);
            }
            Map<EstadoVehiculo, Integer> estados = resumen.get(categoria);
            estados.put(vehiculo.getEstado(), estados.get(vehiculo.getEstado()) + 1);
        }
        return resumen;
    }
    public double calcularIngresoTotal() {
        double total = 0;
        for (Alquiler alquiler : alquileres) total += alquiler.getTotal();
        return total;
    }
    public Map<String, Double> calcularIngresosPorCategoria() {
        Map<String, Double> ingresos = new LinkedHashMap<>();
        for (Vehiculo vehiculo : flota) ingresos.putIfAbsent(vehiculo.getCategoria(), 0.0);
        for (Alquiler alquiler : alquileres) {
            String categoria = alquiler.getVehiculo().getCategoria();
            ingresos.put(categoria, ingresos.getOrDefault(categoria, 0.0) + alquiler.getTotal());
        }
        return ingresos;
    }
    public double calcularTotalDescuentos() {
        double total = 0;
        for (Alquiler alquiler : alquileres) total += alquiler.getDescuento();
        return total;
    }
    public List<Alquiler> consultarAlquileresActivos() {
        List<Alquiler> activos = new ArrayList<>();
        for (Alquiler alquiler : alquileres) if (alquiler.estaActivo()) activos.add(alquiler);
        return Collections.unmodifiableList(activos);
    }
    public List<Alquiler> consultarHistorialCliente(String identificador) { return buscarCliente(identificador).getAlquileres(); }
    public double calcularTotalPagadoPorCliente(String identificador) { return buscarCliente(identificador).calcularTotalPagado(); }
    private Alquiler buscarAlquilerActivo(Vehiculo vehiculo) {
        for (Alquiler alquiler : alquileres)
            if (alquiler.getVehiculo() == vehiculo && alquiler.estaActivo()) return alquiler;
        throw new IllegalStateException("No se encontró un alquiler activo para este vehículo.");
    }
}
