# RentaMovil

Aplicación de consola en Java basada en las clases, atributos, métodos, constructores y datos iniciales del documento «Ejercicios 4 y 5 N.E A». El proyecto contiene 18 archivos Java y organiza las responsabilidades con MVC.

## Requisitos

JDK 17 o posterior, con `java` y `javac` disponibles en la terminal. No utiliza bibliotecas externas, Maven, Gradle, archivos de datos ni base de datos. Los archivos fuente están codificados en UTF-8.

## Archivos y responsabilidades

| Carpeta | Archivos | Responsabilidad |
| --- | --- | --- |
| `src/model/` | `Licencia.java`, `EstadoVehiculo.java`, `EstadoAlquiler.java` | Enumeraciones del dominio. |
| `src/model/` | `Vehiculo.java`, `VehiculoPasajeros.java`, `Automovil.java`, `Motocicleta.java`, `CamionetaCarga.java`, `Microbus.java` | Jerarquía de vehículos, recargos, licencias y mantenimiento. |
| `src/model/` | `Cliente.java`, `ClienteIndividual.java`, `ClienteCorporativo.java` | Jerarquía de clientes, descuentos e historial. |
| `src/model/` | `Cotizacion.java`, `Alquiler.java`, `EmpresaRentaMovil.java` | Cotizaciones, alquileres y administración del negocio. |
| `src/view/` | `RentaMovilView.java` | Lectura de datos y presentación por consola. |
| `src/controller/` | `RentaMovilController.java` | Menú y coordinación entre vista y modelo. |
| `src/` | `Main.java` | Punto de entrada y carga de los datos iniciales. |

`Vehiculo`, `VehiculoPasajeros` y `Cliente` son abstractas. Cada clase o enumeración está en su propio archivo. Se conservan los nombres, tipos y modificadores de los atributos, las firmas de los métodos y los constructores descritos en las tablas 5 a 23. `Main` incluye `main` y `cargarDatosIniciales`, según su tabla de métodos.

Las clases `VehiculoElectrico` y `ClienteGubernamental` se mencionan en el documento como extensiones futuras; no forman parte de esta implementación ni tienen reglas definidas en el análisis.



## Menú

| Opción | Operación |
| --- | --- |
| 1 | Registrar vehículo |
| 2 | Registrar cliente |
| 3 | Consultar flota |
| 4 | Consultar clientes |
| 5 | Cotizar |
| 6 | Alquilar, mostrando el cobro y permitiendo confirmar o cancelar |
| 7 | Registrar devolución |
| 8 | Finalizar mantenimiento |
| 9 | Reporte de flota por categoría y estado |
| 10 | Reporte de ingresos total y por categoría |
| 11 | Reporte de descuentos |
| 12 | Consultar alquileres activos |
| 13 | Consultar historial y total pagado de un cliente |
| 0 | Salir |

Las confirmaciones aceptan `S` o `N`. Las licencias se escriben separadas por comas, por ejemplo `B,M`. Los decimales admiten punto o coma decimal, sin separadores de miles. Las entradas con formato incorrecto se vuelven a solicitar; las operaciones que incumplen una validación muestran el error y regresan al menú.

## Datos iniciales

Se cargan exactamente los ocho vehículos y cuatro clientes de la tabla 28 del documento. Todos los vehículos comienzan disponibles y no existen ingresos registrados.

| Placa | Vehículo | Datos relevantes |
| --- | --- | --- |
| P101ABC | Toyota Yaris | Q250/día, 5 pasajeros, automático, 0 días acumulados |
| P102ABC | Suzuki Swift | Q200/día, 4 pasajeros, manual, 28 días acumulados |
| M201ABC | Honda CB500 | Q150/día, 500 cc, 0 días acumulados |
| M202ABC | Yamaha XTZ | Q100/día, 150 cc, 0 días acumulados |
| C301ABC | Toyota Hilux | Q200/día, 1.5 toneladas, 0 días acumulados |
| C302ABC | Isuzu NPR | Q350/día, 3 toneladas, 13 días acumulados |
| B401ABC | Toyota Hiace | Q450/día, 15 pasajeros, con piloto, 0 días acumulados |
| B402ABC | Hyundai H1 | Q380/día, 12 pasajeros, sin piloto, 0 días acumulados |

| Identificador | Cliente | Licencias y antecedentes |
| --- | --- | --- |
| 1234567890101 | Ana López | C; 0 alquileres previos |
| 2345678901201 | Carlos Méndez | B y M; 3 alquileres previos |
| 1234567-8 | Transportes Quetzal, S.A. | A; contacto María Pérez |
| 7654321-0 | Agroexport, S.A. | C; contacto Luis Ramírez |

Los tres alquileres previos de Carlos se representan con `alquileresPrevios`, como indica el documento: permiten aplicar el descuento sin crear ingresos ni alquileres ficticios. El historial detallado y el total pagado corresponden a los alquileres registrados durante la ejecución.

## Recorrido de ejemplo

1. Elegir `5`, placa `C301ABC`, cliente `1234567-8` y `3` días: subtotal Q1,050.00, descuento Q105.00 y total Q945.00. Cotizar no registra ingreso.
2. Elegir `6` con esos mismos datos y responder `N`: no cambia ningún estado, conteo ni ingreso.
3. Repetir la opción `6` y responder `S`: se registra el alquiler y el ingreso de Q945.00.
4. Elegir `7` e indicar `C301ABC`: el alquiler finaliza y el ingreso se conserva.
5. Para observar mantenimiento, alquilar `P102ABC` a Ana por 2 días y devolverlo. Sus 28 días iniciales se convierten en 30 y pasa a mantenimiento. La opción `8` lo deja disponible con acumulado cero.