package modelo;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private int cantidad;
    private double precioUnitario;
    private int stockMinimo;

    public Producto(String codigo,String nombre, int cantidad, double precioUnitario,int stockMinimo) throws InventarioException {
        setCodigo(codigo);
        setNombre(nombre);
        setCantidad(cantidad);
        setPrecioUnitario(precioUnitario);
        setStockMinimo(stockMinimo);
    }

    public String getCodigo() {
        return codigo;
    }

    public final void setCodigo(String codigo) throws InventarioException{
        if(codigo== null || codigo.trim().isEmpty()) {
            throw new InventarioException("El codigo del producto es obligatorio.");
        }
        this.codigo= codigo.trim().toUpperCase();
    }

    public String getNombre(){
        return nombre;
    }

    public final void setNombre(String nombre) throws InventarioException {
        if (nombre ==null || nombre.trim().isEmpty()){
            throw new InventarioException("El nombre del producto es obligatorio.");
        }
        this.nombre =nombre.trim();
    }

    public int getCantidad() {
        return cantidad;
    }

    public final void setCantidad(int cantidad) throws InventarioException {
        if(cantidad< 0) {
            throw new InventarioException("La cantidad no puede ser negativa.");
        }
        this.cantidad=cantidad;
    }

    public double getPrecioUnitario(){
        return precioUnitario;
    }

    public final void setPrecioUnitario(double precioUnitario) throws InventarioException {
        if(precioUnitario <=0){
            throw new InventarioException("El precio unitario debe ser mayor a $0.00.");
        }
        this.precioUnitario= precioUnitario;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public final void setStockMinimo(int stockMinimo) throws InventarioException{
        if (stockMinimo<0) {
            throw new InventarioException("El stock minimo no puede ser negativo.");
        }
        this.stockMinimo =stockMinimo;
    }

    public abstract double calcularValorTotal();
    public abstract String verificarEstado();
    public abstract String getTipoProducto();

    @Override
    public String toString() {
        return String.format("[%s] %-22s | Tipo: %-13s | Cant: %3d | P.Unit: $%6.2f | Valor Total: $%8.2f | Estado: %s",
                codigo,nombre, getTipoProducto(),cantidad, precioUnitario, calcularValorTotal(),verificarEstado());
    }
}
