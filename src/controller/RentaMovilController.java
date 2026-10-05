package controller;

import java.util.*;

import model.*;
import view.RentaMovilView;

/** Coordina la vista y el modelo sin decidir recargos, licencias o descuentos. */
public class RentaMovilController {
    private EmpresaRentaMovil empresa;
    private RentaMovilView vista;
    public RentaMovilController(EmpresaRentaMovil empresa, RentaMovilView vista) {
        if (empresa == null || vista == null) throw new IllegalArgumentException("Modelo y vista obligatorios.");
        this.empresa = empresa; this.vista = vista;
    }
    public void iniciar() {
        while (true) {
            try {
                vista.mostrarMenu();
                int opcion = vista.leerEntero("Seleccione una opción:");
                if (opcion == 0) { vista.mostrarMensaje("Hasta pronto."); return; }
                procesarOpcion(opcion);
            } catch (IllegalArgumentException | IllegalStateException ex) {
                vista.mostrarError(ex.getMessage());
            } catch (NoSuchElementException ex) {
                vista.mostrarMensaje("Entrada finalizada. Hasta pronto."); return;
            }
        }
    }
    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1: registrarVehiculo(); break;
            case 2: registrarCliente(); break;
            case 3: consultarFlota(); break;
            case 4: consultarClientes(); break;
            case 5: cotizarAlquiler(); break;
            case 6: alquilarVehiculo(); break;
            case 7: registrarDevolucion(); break;
            case 8: registrarFinMantenimiento(); break;
            case 9: reporteFlota(); break;
            case 10: reporteIngresos(); break;
            case 11: reporteDescuentos(); break;
            case 12: reporteAlquileresActivos(); break;
            case 13: reporteHistorialCliente(); break;
            default: vista.mostrarError("Opción fuera del menú.");
        }
    }
    private void registrarVehiculo() {
        empresa.registrarVehiculo(crearVehiculo()); vista.mostrarMensaje("Vehículo registrado y disponible.");
    }
    private void registrarCliente() {
        empresa.registrarCliente(crearCliente()); vista.mostrarMensaje("Cliente registrado.");
    }
    private Vehiculo crearVehiculo() {
        vista.mostrarMensaje("1. Automóvil | 2. Motocicleta | 3. Camioneta de carga | 4. Microbús");
        int categoria = vista.leerEntero("Categoría:");
        if (categoria < 1 || categoria > 4) throw new IllegalArgumentException("Categoría inválida.");
        String placa = vista.leerTexto("Placa:");
        String marca = vista.leerTexto("Marca:");
        String modelo = vista.leerTexto("Modelo:");
        double tarifaDiaria = vista.leerDecimal("Tarifa diaria:");
        // Solo el registro selecciona qué clase concreta construir.
        switch (categoria) {
            case 1: return new Automovil(placa, marca, modelo, tarifaDiaria,
                    vista.leerEntero("Cantidad de pasajeros:"), vista.leerConfirmacion("¿Transmisión automática?"));
            case 2: return new Motocicleta(placa, marca, modelo, tarifaDiaria, vista.leerEntero("Cilindraje (cc):"));
            case 3: return new CamionetaCarga(placa, marca, modelo, tarifaDiaria, vista.leerDecimal("Capacidad máxima (toneladas):"));
            case 4: return new Microbus(placa, marca, modelo, tarifaDiaria,
                    vista.leerEntero("Cantidad de pasajeros:"), vista.leerConfirmacion("¿Se entrega con piloto de la empresa?"));
            default: throw new IllegalArgumentException("Categoría inválida.");
        }
    }
    private Cliente crearCliente() {
        vista.mostrarMensaje("1. Individual | 2. Corporativo");
        int tipo = vista.leerEntero("Tipo de cliente:");
        if (tipo < 1 || tipo > 2) throw new IllegalArgumentException("Tipo de cliente inválido.");
        String identificador = vista.leerTexto("Identificador (DPI o NIT):");
        String nombre = vista.leerTexto("Nombre de la persona o empresa:");
        Set<Licencia> licencias = vista.leerLicencias("Licencias presentadas");
        switch (tipo) {
            case 1: return new ClienteIndividual(identificador, nombre, licencias);
            case 2: return new ClienteCorporativo(identificador, nombre, vista.leerTexto("Nombre del contacto:"), licencias);
            default: throw new IllegalArgumentException("Tipo de cliente inválido.");
        }
    }
    private void consultarFlota() { vista.mostrarVehiculos(empresa.consultarFlota()); }
    private void consultarClientes() { vista.mostrarClientes(empresa.consultarClientes()); }
    private void cotizarAlquiler() {
        String placa = vista.leerTexto("Placa:");
        String identificador = vista.leerTexto("Identificador del cliente:");
        int dias = vista.leerEntero("Días de alquiler:");
        vista.mostrarCotizacion(empresa.cotizar(placa, identificador, dias));
    }
    private void alquilarVehiculo() {
        String placa = vista.leerTexto("Placa:");
        String identificador = vista.leerTexto("Identificador del cliente:");
        int dias = vista.leerEntero("Días de alquiler:");
        Cotizacion cotizacion = empresa.cotizar(placa, identificador, dias);
        vista.mostrarCotizacion(cotizacion);
        if (!cotizacion.esAlquilable()) return;
        if (vista.leerConfirmacion("¿Confirma el alquiler y el cobro?"))
            vista.mostrarAlquiler(empresa.confirmarAlquiler(cotizacion));
        else vista.mostrarMensaje("Alquiler cancelado. No se modificó ningún dato.");
    }
    private void registrarDevolucion() {
        Alquiler alquiler = empresa.registrarDevolucion(vista.leerTexto("Placa del vehículo a devolver:"));
        vista.mostrarAlquiler(alquiler);
        vista.mostrarMensaje("Estado del vehículo: " + alquiler.getVehiculo().getEstado().getDescripcion());
    }
    private void registrarFinMantenimiento() {
        empresa.registrarFinMantenimiento(vista.leerTexto("Placa:"));
        vista.mostrarMensaje("Mantenimiento finalizado. Vehículo disponible y acumulado en cero.");
    }
    private void reporteFlota() { vista.mostrarResumenFlota(empresa.resumirFlotaPorCategoria()); }
    private void reporteIngresos() { vista.mostrarIngresos(empresa.calcularIngresoTotal(), empresa.calcularIngresosPorCategoria()); }
    private void reporteDescuentos() { vista.mostrarDescuentos(empresa.calcularTotalDescuentos()); }
    private void reporteAlquileresActivos() { vista.mostrarAlquileres("Alquileres activos", empresa.consultarAlquileresActivos()); }
    private void reporteHistorialCliente() {
        String identificador = vista.leerTexto("Identificador del cliente:");
        vista.mostrarHistorialCliente(empresa.buscarCliente(identificador), empresa.consultarHistorialCliente(identificador),
                empresa.calcularTotalPagadoPorCliente(identificador));
    }
}
