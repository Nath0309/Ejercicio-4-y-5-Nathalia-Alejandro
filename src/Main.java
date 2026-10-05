import java.util.*;

import model.*;
import view.RentaMovilView;
import controller.RentaMovilController;

/** Punto de entrada: ensambla MVC y carga los datos de la tabla 28. */
public class Main {
    public static void main(String[] args) {
        EmpresaRentaMovil empresa = new EmpresaRentaMovil("RentaMovil");
        cargarDatosIniciales(empresa);
        RentaMovilView vista = new RentaMovilView();
        RentaMovilController controlador = new RentaMovilController(empresa, vista);
        controlador.iniciar();
    }
    private static void cargarDatosIniciales(EmpresaRentaMovil empresa) {
        empresa.registrarVehiculo(new Automovil("P101ABC", "Toyota", "Yaris", 250.0, 5, true, 0));
        empresa.registrarVehiculo(new Automovil("P102ABC", "Suzuki", "Swift", 200.0, 4, false, 28));
        empresa.registrarVehiculo(new Motocicleta("M201ABC", "Honda", "CB500", 150.0, 500, 0));
        empresa.registrarVehiculo(new Motocicleta("M202ABC", "Yamaha", "XTZ", 100.0, 150, 0));
        empresa.registrarVehiculo(new CamionetaCarga("C301ABC", "Toyota", "Hilux", 200.0, 1.5, 0));
        empresa.registrarVehiculo(new CamionetaCarga("C302ABC", "Isuzu", "NPR", 350.0, 3.0, 13));
        empresa.registrarVehiculo(new Microbus("B401ABC", "Toyota", "Hiace", 450.0, 15, true, 0));
        empresa.registrarVehiculo(new Microbus("B402ABC", "Hyundai", "H1", 380.0, 12, false, 0));
        empresa.registrarCliente(new ClienteIndividual("1234567890101", "Ana López", EnumSet.of(Licencia.C), 0));
        empresa.registrarCliente(new ClienteIndividual("2345678901201", "Carlos Méndez", EnumSet.of(Licencia.B, Licencia.M), 3));
        empresa.registrarCliente(new ClienteCorporativo("1234567-8", "Transportes Quetzal, S.A.", "María Pérez", EnumSet.of(Licencia.A)));
        empresa.registrarCliente(new ClienteCorporativo("7654321-0", "Agroexport, S.A.", "Luis Ramírez", EnumSet.of(Licencia.C)));
    }
}

