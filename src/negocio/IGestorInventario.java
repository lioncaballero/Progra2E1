package negocio;

import java.util.List;
import modelo.InventarioException;
import modelo.Producto;

public interface IGestorInventario {
    void registrarProducto(Producto producto) throws InventarioException;
    Producto buscarPorCodigo(String codigo) throws InventarioException;
    void actualizarStock(String codigo, int deltaCantidad) throws InventarioException;
    boolean eliminarProducto(String codigo) throws InventarioException;
    List<Producto> listarTodos();
    List<Producto> obtenerAlertas();
    double calcularValorTotalInventario();
    int calcularTotalUnidades();
}
