package modelo;

public class ProductoNoPerecedero extends Producto {
    private int mesesGarantia;
    private String ubicacionPasillo;

    public ProductoNoPerecedero(String codigo, String nombre, int cantidad,double precioUnitario,
                                int stockMinimo, int mesesGarantia,String ubicacionPasillo)
            throws InventarioException {
        super(codigo,nombre, cantidad, precioUnitario,stockMinimo);
        setMesesGarantia(mesesGarantia);
        setUbicacionPasillo(ubicacionPasillo);
    }

    public int getMesesGarantia() {
        return mesesGarantia;
    }

    public final void setMesesGarantia(int mesesGarantia) throws InventarioException{
        if(mesesGarantia< 0) {
            throw new InventarioException("Los meses de garantia no pueden ser negativos.");
        }
        this.mesesGarantia= mesesGarantia;
    }

    public String getUbicacionPasillo(){
        return ubicacionPasillo;
    }

    public final void setUbicacionPasillo(String ubicacionPasillo) throws InventarioException {
        if (ubicacionPasillo== null || ubicacionPasillo.trim().isEmpty()){
            throw new InventarioException("La ubicacion en bodega es obligatoria.");
        }
        this.ubicacionPasillo =ubicacionPasillo.trim().toUpperCase();
    }

    @Override
    public double calcularValorTotal(){
        double total= getCantidad()* getPrecioUnitario();
        return total;
    }

    @Override
    public String verificarEstado() {
        if(getCantidad() ==0) {
            return "AGOTADO - Reabastecer urgente (Pasillo "+ ubicacionPasillo+ ")";
        } else if (getCantidad()<= getStockMinimo()){
            return "STOCK CRITICO (<= " +getStockMinimo() + " uds - Pasillo "+ubicacionPasillo + ")";
        }
        return "OPTIMO (Garantia: "+ mesesGarantia + "m | Pasillo " +ubicacionPasillo+ ")";
    }

    @Override
    public String getTipoProducto(){
        return "No Perecedero";
    }
}
