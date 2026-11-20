package negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.InventarioException;
import modelo.Producto;

public class GestorInventario implements IGestorInventario {
    public static final double PRESUPUESTO_MAXIMO= 15000.00;
    public static final int CAPACIDAD_MAXIMA_UNIDADES =500;

    private final List<Producto> inventario;

    public GestorInventario(){
        this.inventario =new ArrayList<>();
    }

    @Override
    public void registrarProducto(Producto producto) throws InventarioException {
        if(producto ==null) {
            throw new InventarioException("El producto no puede ser nulo.");
        }
        if (existeCodigo(producto.getCodigo())){
            throw new InventarioException("Ya existe un producto registrado con el codigo: "+ producto.getCodigo());
        }

        int nuevasUnidades= calcularTotalUnidades()+ producto.getCantidad();
        if(nuevasUnidades >CAPACIDAD_MAXIMA_UNIDADES) {
            throw new InventarioException(String.format(
                    "Restriccion de espacio excedida: capacidad maxima %d uds (intento: %d uds).",
                    CAPACIDAD_MAXIMA_UNIDADES,nuevasUnidades));
        }

        double nuevoValorTotal =calcularValorTotalInventario()+ producto.calcularValorTotal();
        if (nuevoValorTotal> PRESUPUESTO_MAXIMO){
            throw new InventarioException(String.format(
                    "Restriccion presupuestaria excedida: limite $%.2f (intento: $%.2f).",
                    PRESUPUESTO_MAXIMO, nuevoValorTotal));
        }

        inventario.add(producto);
    }

    private boolean existeCodigo(String codigo){
        for (Producto p : inventario) {
            if(p.getCodigo().equalsIgnoreCase(codigo.trim())){
                return true;
            }
        }
        return false;
    }

    @Override
    public Producto buscarPorCodigo(String codigo) throws InventarioException {
        if(codigo== null || codigo.trim().isEmpty()) {
            throw new InventarioException("Debe ingresar un codigo valido para buscar.");
        }
        for(Producto p : inventario){
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return p;
            }
        }
        throw new InventarioException("No se encontro ningun producto con el codigo: " +codigo.toUpperCase());
    }

    @Override
    public void actualizarStock(String codigo,int deltaCantidad) throws InventarioException {
        Producto p= buscarPorCodigo(codigo);
        int cantidadResultante =p.getCantidad()+ deltaCantidad;

        if(cantidadResultante< 0){
            throw new InventarioException(String.format(
                    "Stock insuficiente para salida. Disponible: %d uds, Solicitado: %d uds.",
                    p.getCantidad(),Math.abs(deltaCantidad)));
        }

        int totalUnidadesBodega= calcularTotalUnidades() +deltaCantidad;
        if (totalUnidadesBodega >CAPACIDAD_MAXIMA_UNIDADES) {
            throw new InventarioException(String.format(
                    "Capacidad de bodega excedida (%d/%d uds).",
                    totalUnidadesBodega, CAPACIDAD_MAXIMA_UNIDADES));
        }

        int cantidadAnterior =p.getCantidad();
        double valorAnterior= calcularValorTotalInventario();
        p.setCantidad(cantidadResultante);

        if(calcularValorTotalInventario()> PRESUPUESTO_MAXIMO){
            p.setCantidad(cantidadAnterior);
            throw new InventarioException(String.format(
                    "Operacion rechazada: supera el presupuesto maximo de $%.2f (actual: $%.2f).",
                    PRESUPUESTO_MAXIMO,valorAnterior));
        }
    }

    @Override
    public boolean eliminarProducto(String codigo) throws InventarioException {
        Producto p= buscarPorCodigo(codigo);
        return inventario.remove(p);
    }

    @Override
    public List<Producto> listarTodos(){
        return Collections.unmodifiableList(inventario);
    }

    @Override
    public List<Producto> obtenerAlertas() {
        List<Producto> alertas= new ArrayList<>();
        for(Producto p : inventario){
            String estado =p.verificarEstado();
            if (!estado.startsWith("OPTIMO")) {
                alertas.add(p);
            }
        }
        return alertas;
    }

    @Override
    public double calcularValorTotalInventario(){
        double total= 0.0;
        for (Producto p : inventario) {
            total+= p.calcularValorTotal();
        }
        return total;
    }

    @Override
    public int calcularTotalUnidades() {
        int unidades =0;
        for(Producto p : inventario){
            unidades +=p.getCantidad();
        }
        return unidades;
    }
}
