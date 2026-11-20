package interfaz;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import modelo.InventarioException;
import modelo.Producto;
import modelo.ProductoNoPerecedero;
import modelo.ProductoPerecedero;
import negocio.GestorInventario;
import negocio.IGestorInventario;

public class ConsolaInventario {
    private final IGestorInventario gestor;
    private final Scanner scanner;

    public ConsolaInventario(){
        this.gestor= new GestorInventario();
        this.scanner =new Scanner(System.in);
    }

    public void iniciar() {
        cargarDatosIniciales();
        int opcion= -1;
        while(opcion !=0){
            mostrarMenu();
            String entrada= scanner.nextLine().trim();
            try {
                opcion =Integer.parseInt(entrada);
                procesarOpcion(opcion);
            } catch(NumberFormatException e) {
                System.out.println("[ERROR] Ingrese un numero de opcion valido.");
            }
        }
    }

    private void mostrarMenu(){
        System.out.println("\n======================================================================");
        System.out.println("  SISTEMA DE GESTION DE INVENTARIOS - ANDESTECH S.A.S. (ISWZ1103)");
        System.out.println("======================================================================");
        System.out.printf("  Capacidad Bodega: %d/%d uds | Capital Invertido: $%.2f / $%.2f%n",
                gestor.calcularTotalUnidades(),GestorInventario.CAPACIDAD_MAXIMA_UNIDADES,
                gestor.calcularValorTotalInventario(), GestorInventario.PRESUPUESTO_MAXIMO);
        System.out.println("----------------------------------------------------------------------");
        System.out.println("  1. Registrar producto (RF1)");
        System.out.println("  2. Consultar inventario completo (RF2 - Polimorfismo)");
        System.out.println("  3. Registrar entrada/salida de stock (RF3)");
        System.out.println("  4. Reporte de alertas de vencimiento y stock critico (RF4)");
        System.out.println("  5. Eliminar producto por codigo (RF5)");
        System.out.println("  6. Ejecutar Suite Completa de Casos de Prueba (CP1 - CP6)");
        System.out.println("  0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private void procesarOpcion(int opcion) {
        switch(opcion){
            case 1:
                registrarProductoUI();
                break;
            case 2:
                listarInventarioUI();
                break;
            case 3:
                actualizarStockUI();
                break;
            case 4:
                mostrarAlertasUI();
                break;
            case 5:
                eliminarProductoUI();
                break;
            case 6:
                ejecutarSuitePruebas();
                break;
            case 0:
                System.out.println("Saliendo del sistema. Hasta pronto!");
                break;
            default:
                System.out.println("[ERROR] Opcion fuera de rango.");
                break;
        }
    }

    public void registrarProductoUI(){
        try {
            System.out.println("\n--- REGISTRO DE NUEVO PRODUCTO ---");
            System.out.print("Tipo (1 = No Perecedero, 2 = Perecedero): ");
            String tipoStr= scanner.nextLine().trim();
            if(tipoStr.isEmpty()) {
                throw new InventarioException("Debe seleccionar el tipo de producto.");
            }
            int tipo =Integer.parseInt(tipoStr);

            System.out.print("Codigo (ej. NP-010): ");
            String codigo= scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre =scanner.nextLine();
            if (nombre== null || nombre.trim().isEmpty()){
                throw new InventarioException("El nombre del producto es obligatorio.");
            }

            System.out.print("Cantidad inicial: ");
            String cantStr= scanner.nextLine().trim();
            if(cantStr.isEmpty()){
                throw new InventarioException("Error de validacion: La cantidad es obligatoria y no puede estar vacia.");
            }
            int cantidad= Integer.parseInt(cantStr);

            System.out.print("Precio unitario (USD): ");
            String precioStr =scanner.nextLine().trim();
            if (precioStr.isEmpty()) {
                throw new InventarioException("Error de validacion: El precio unitario es obligatorio.");
            }
            double precio= Double.parseDouble(precioStr);

            System.out.print("Stock minimo de alerta: ");
            int stockMin =Integer.parseInt(scanner.nextLine().trim());

            Producto nuevo;
            if(tipo ==1){
                System.out.print("Meses de garantia: ");
                int garantia= Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Ubicacion en bodega (ej. A-01): ");
                String pasillo =scanner.nextLine();
                nuevo= new ProductoNoPerecedero(codigo,nombre, cantidad, precio,stockMin, garantia, pasillo);
            } else if(tipo== 2) {
                System.out.print("Fecha de vencimiento (YYYY-MM-DD): ");
                LocalDate fechaVenc= LocalDate.parse(scanner.nextLine().trim());
                System.out.print("Temperatura maxima almacenamiento (C): ");
                double temp =Double.parseDouble(scanner.nextLine().trim());
                nuevo =new ProductoPerecedero(codigo, nombre,cantidad, precio, stockMin,fechaVenc, temp);
            } else {
                throw new InventarioException("Tipo de producto invalido (use 1 o 2).");
            }

            gestor.registrarProducto(nuevo);
            System.out.println("[EXITO] Producto registrado correctamente: "+ nuevo.getNombre());
        } catch(NumberFormatException e){
            System.out.println("[ERROR DE FORMATO] Debe ingresar un valor numerico valido.");
        } catch (DateTimeParseException e) {
            System.out.println("[ERROR DE FECHA] Formato esperado YYYY-MM-DD (ej. 2026-12-31).");
        } catch(InventarioException e) {
            System.out.println("[ERROR DE VALIDACION] " +e.getMessage());
        }
    }

    private void listarInventarioUI() {
        System.out.println("\n--- INVENTARIO ACTUAL (VALORACION POLIMORFICA) ---");
        List<Producto> lista= gestor.listarTodos();
        if(lista.isEmpty()){
            System.out.println("El inventario esta vacio.");
            return;
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
        System.out.printf("TOTAL UNIDADES: %d | VALOR TOTAL INVENTARIO: $%.2f%n",
                gestor.calcularTotalUnidades(),gestor.calcularValorTotalInventario());
    }

    private void actualizarStockUI(){
        try {
            System.out.print("Codigo del producto: ");
            String codigo= scanner.nextLine();
            System.out.print("Variacion de cantidad (+ingreso / -egreso): ");
            int delta =Integer.parseInt(scanner.nextLine().trim());
            gestor.actualizarStock(codigo,delta);
            System.out.println("[EXITO] Stock actualizado. Nuevo estado: "+ gestor.buscarPorCodigo(codigo));
        } catch (NumberFormatException e){
            System.out.println("[ERROR] Ingrese un numero entero valido.");
        } catch(InventarioException e) {
            System.out.println("[ERROR] " +e.getMessage());
        }
    }

    private void mostrarAlertasUI() {
        System.out.println("\n--- ALERTAS OPERATIVAS (VENCIMIENTOS Y STOCK CRITICO) ---");
        List<Producto> alertas =gestor.obtenerAlertas();
        if(alertas.isEmpty()) {
            System.out.println("Sin alertas pendientes. Todo el inventario esta en estado OPTIMO.");
        } else {
            for(Producto p : alertas){
                System.out.println("(!) "+ p);
            }
        }
    }

    private void eliminarProductoUI(){
        try {
            System.out.print("Ingrese el codigo del producto a eliminar: ");
            String codigo =scanner.nextLine();
            gestor.eliminarProducto(codigo);
            System.out.println("[EXITO] Producto "+ codigo.toUpperCase()+ " eliminado del inventario.");
        } catch(InventarioException e) {
            System.out.println("[ERROR] " +e.getMessage());
        }
    }

    private void cargarDatosIniciales() {
        try {
            gestor.registrarProducto(new ProductoNoPerecedero(
                    "NP-001", "Taladro Percutor 750W",15, 85.00, 5,24, "A-01"));
            gestor.registrarProducto(new ProductoPerecedero(
                    "PR-001", "Sellador Silicona Ind.", 20,12.50, 8, LocalDate.now().plusDays(10),25.0));
            gestor.registrarProducto(new ProductoPerecedero(
                    "PR-002","Resina Epoxica A/B", 12, 28.00,5, LocalDate.now().plusDays(120), 22.0));
        } catch(InventarioException ignored){
        }
    }

    public void ejecutarSuitePruebas(){
        System.out.println("\n======================================================================");
        System.out.println("  EJECUCION DE CASOS DE PRUEBA AUTOMATIZADOS (CP1 - CP6)");
        System.out.println("======================================================================");
        IGestorInventario gestorPrueba= new GestorInventario();

        System.out.println("\n[CP1] Registro con campo cantidad vacio (Nombre: \"Tornillo\", Cantidad: \"\", Precio: \"0.50\"):");
        try {
            String cantInput ="";
            if(cantInput.trim().isEmpty()) {
                throw new InventarioException("Error de validacion: La cantidad es obligatoria y no puede estar vacia.");
            }
            gestorPrueba.registrarProducto(new ProductoNoPerecedero("NP-100","Tornillo", Integer.parseInt(cantInput), 0.50,10, 12, "B-02"));
        } catch(InventarioException e){
            System.out.println("  -> Resultado Obtenido: [BLOQUEADO] "+ e.getMessage());
        }

        System.out.println("\n[CP2] Registro valido de Producto No Perecedero (Codigo: NP-101, Nombre: \"Tornillo Hexagonal\", Cant: 100, Precio: $0.50):");
        try {
            Producto p1= new ProductoNoPerecedero("NP-101", "Tornillo Hexagonal",100, 0.50, 20,12, "B-02");
            gestorPrueba.registrarProducto(p1);
            System.out.println("  -> Resultado Obtenido: [REGISTRADO] " +p1);
        } catch (InventarioException e) {
            System.out.println("  -> Error inesperado: "+ e.getMessage());
        }

        System.out.println("\n[CP3] Calculo polimorfico en Producto Perecedero proximo a vencer (10 dias restantes, 10 uds x $20.00):");
        try {
            Producto p2 =new ProductoPerecedero("PR-201","Adhesivo Industrial", 10, 20.00,5, LocalDate.now().plusDays(10), 20.0);
            gestorPrueba.registrarProducto(p2);
            System.out.printf("  -> Resultado Obtenido: Valor sin descuento = $200.00 | Valor Polimorfico (20%% desc.) = $%.2f | Estado: %s%n",
                    p2.calcularValorTotal(),p2.verificarEstado());
        } catch(InventarioException e){
            System.out.println("  -> Error inesperado: " +e.getMessage());
        }

        System.out.println("\n[CP4] Validacion de restriccion de presupuesto (Intento registrar 200 equipos x $100.00 = $20,000.00):");
        try {
            Producto p3= new ProductoNoPerecedero("NP-102", "Compresor Industrial",200, 100.00, 2,36, "C-01");
            gestorPrueba.registrarProducto(p3);
        } catch (InventarioException e){
            System.out.println("  -> Resultado Obtenido: [BLOQUEADO] "+ e.getMessage());
        }

        System.out.println("\n[CP5] Egreso de stock en NP-101 (-85 unidades -> quedan 15 uds, stock minimo = 20):");
        try {
            gestorPrueba.actualizarStock("NP-101",-85);
            Producto actualizado =gestorPrueba.buscarPorCodigo("NP-101");
            System.out.println("  -> Resultado Obtenido: Stock actualizado a "+ actualizado.getCantidad()+ " uds | Alerta: " +actualizado.verificarEstado());
        } catch(InventarioException e) {
            System.out.println("  -> Error inesperado: "+ e.getMessage());
        }

        System.out.println("\n[CP6] Intento de egreso mayor al stock disponible en NP-101 (-50 unidades cuando hay 15):");
        try {
            gestorPrueba.actualizarStock("NP-101", -50);
        } catch (InventarioException e){
            System.out.println("  -> Resultado Obtenido: [BLOQUEADO] " +e.getMessage());
        }
        System.out.println("======================================================================\n");
    }
}
