package view;

import java.util.*;

import model.*;

/** Entrada y salida por consola. Los cálculos pertenecen al modelo. */
public class RentaMovilView {
    private Scanner scanner;
    public RentaMovilView() { scanner = new Scanner(System.in, "UTF-8"); }
    public void mostrarMenu() {
        System.out.println("\nRentaMovil");
        System.out.println("1. Registrar vehículo\n2. Registrar cliente\n3. Consultar flota\n4. Consultar clientes");
        System.out.println("5. Cotizar\n6. Alquilar (confirmar o cancelar)\n7. Registrar devolución\n8. Finalizar mantenimiento");
        System.out.println("9. Reporte de flota\n10. Reporte de ingresos\n11. Reporte de descuentos");
        System.out.println("12. Alquileres activos\n13. Historial de un cliente\n0. Salir");
    }
    public int leerEntero(String mensaje) {
        while (true) {
            try { return Integer.parseInt(leerTexto(mensaje)); }
            catch (NumberFormatException ex) { mostrarError("Ingrese un número entero válido."); }
        }
    }
    public double leerDecimal(String mensaje) {
        while (true) {
            try {
                String texto = leerTexto(mensaje);
                // Acepta coma o punto decimal; no acepta separadores de miles.
                if (!texto.matches("[+-]?(?:[0-9]+(?:[.,][0-9]*)?|[.,][0-9]+)"))
                    throw new NumberFormatException();
                double valor = Double.parseDouble(texto.replace(',', '.'));
                if (!Double.isFinite(valor)) throw new NumberFormatException();
                return valor;
            } catch (NumberFormatException ex) { mostrarError("Ingrese un decimal finito, por ejemplo 1.5."); }
        }
    }
    public String leerTexto(String mensaje) {
        System.out.print(mensaje + " ");
        if (!scanner.hasNextLine()) throw new NoSuchElementException("Fin de la entrada.");
        return scanner.nextLine().trim();
    }
    public boolean leerConfirmacion(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje + " (S/N):");
            if (respuesta.equalsIgnoreCase("S")) return true;
            if (respuesta.equalsIgnoreCase("N")) return false;
            mostrarError("Responda S o N.");
        }
    }
    public Set<Licencia> leerLicencias(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje + " (A, B, C, M; separadas por comas):");
            try {
                Set<Licencia> licencias = EnumSet.noneOf(Licencia.class);
                for (String parte : texto.split(",", -1)) licencias.add(Licencia.desdeTexto(parte));
                return licencias;
            } catch (IllegalArgumentException ex) { mostrarError(ex.getMessage()); }
        }
    }
    public void mostrarVehiculos(List<Vehiculo> vehiculos) {
        if (vehiculos.isEmpty()) mostrarMensaje("No hay vehículos registrados.");
        for (Vehiculo vehiculo : vehiculos) mostrarMensaje(vehiculo.toString());
    }
    public void mostrarClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) mostrarMensaje("No hay clientes registrados.");
        for (Cliente cliente : clientes) mostrarMensaje(cliente.toString());
    }
    public void mostrarCotizacion(Cotizacion cotizacion) {
        mostrarMensaje("Vehículo: " + cotizacion.getVehiculo());
        mostrarMensaje("Cliente: " + cotizacion.getCliente());
        mostrarMensaje("Días: " + cotizacion.getDias());
        mostrarMensaje("Subtotal: " + formatearMonto(cotizacion.getSubtotal()));
        mostrarMensaje("Descuento: " + formatearMonto(cotizacion.getDescuento()));
        mostrarMensaje("Total: " + formatearMonto(cotizacion.getTotal()));
        if (cotizacion.esAlquilable()) mostrarMensaje("El cliente puede alquilar este vehículo.");
        else {
            mostrarMensaje("El cliente no puede alquilarlo en este momento:");
            for (String motivo : cotizacion.getMotivosRechazo()) mostrarMensaje("- " + motivo);
        }
    }
    public void mostrarAlquiler(Alquiler alquiler) {
        mostrarMensaje(alquiler.toString());
        mostrarMensaje("Características: " + alquiler.getVehiculo().describirCaracteristicas());
    }
    public void mostrarResumenFlota(Map<String, Map<EstadoVehiculo, Integer>> resumen) {
        if (resumen.isEmpty()) mostrarMensaje("No hay vehículos registrados.");
        for (Map.Entry<String, Map<EstadoVehiculo, Integer>> entrada : resumen.entrySet()) {
            int total = 0;
            for (int cantidad : entrada.getValue().values()) total += cantidad;
            mostrarMensaje(entrada.getKey() + " | Total: " + total);
            for (EstadoVehiculo estado : EstadoVehiculo.values())
                mostrarMensaje("  " + estado.getDescripcion() + ": " + entrada.getValue().getOrDefault(estado, 0));
        }
    }
    public void mostrarIngresos(double total, Map<String, Double> porCategoria) {
        mostrarMensaje("Ingreso total: " + formatearMonto(total));
        for (Map.Entry<String, Double> entrada : porCategoria.entrySet())
            mostrarMensaje(entrada.getKey() + ": " + formatearMonto(entrada.getValue()));
    }
    public void mostrarDescuentos(double total) { mostrarMensaje("Descuentos otorgados: " + formatearMonto(total)); }
    public void mostrarAlquileres(String titulo, List<Alquiler> alquileres) {
        mostrarMensaje(titulo);
        if (alquileres.isEmpty()) mostrarMensaje("No hay alquileres para mostrar.");
        for (Alquiler alquiler : alquileres) mostrarAlquiler(alquiler);
    }
    public void mostrarHistorialCliente(Cliente cliente, List<Alquiler> historial, double totalPagado) {
        mostrarMensaje(cliente.toString());
        mostrarAlquileres("Historial de alquileres de esta ejecución", historial);
        mostrarMensaje("Total pagado en esta ejecución: " + formatearMonto(totalPagado));
    }
    public void mostrarMensaje(String mensaje) { System.out.println(mensaje); }
    public void mostrarError(String mensaje) { System.out.println("Error: " + mensaje); }
    private String formatearMonto(double monto) { return String.format(Locale.US, "Q%,.2f", monto); }
}
