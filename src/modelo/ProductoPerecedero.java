package modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductoPerecedero extends Producto {
    private LocalDate fechaVencimiento;
    private double temperaturaMaxC;

    public ProductoPerecedero(String codigo, String nombre,int cantidad, double precioUnitario,
                              int stockMinimo,LocalDate fechaVencimiento, double temperaturaMaxC)
            throws InventarioException {
        super(codigo, nombre,cantidad, precioUnitario, stockMinimo);
        setFechaVencimiento(fechaVencimiento);
        this.temperaturaMaxC= temperaturaMaxC;
    }

    public LocalDate getFechaVencimiento(){
        return fechaVencimiento;
    }

    public final void setFechaVencimiento(LocalDate fechaVencimiento) throws InventarioException {
        if(fechaVencimiento ==null) {
            throw new InventarioException("La fecha de vencimiento es obligatoria para productos perecederos.");
        }
        this.fechaVencimiento =fechaVencimiento;
    }

    public double getTemperaturaMaxC() {
        return temperaturaMaxC;
    }

    public void setTemperaturaMaxC(double temperaturaMaxC){
        this.temperaturaMaxC=temperaturaMaxC;
    }

    public long calcularDiasParaVencer() {
        long dias= ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
        return dias;
    }

    @Override
    public double calcularValorTotal(){
        long dias =calcularDiasParaVencer();
        double subtotal= getCantidad()* getPrecioUnitario();
        if(dias< 0) {
            return 0.0;
        } else if (dias <=15){
            double conDescuento= subtotal*0.80;
            return conDescuento;
        }
        return subtotal;
    }

    @Override
    public String verificarEstado() {
        long dias= calcularDiasParaVencer();
        if(dias <0) {
            return "VENCIDO (Hace "+ Math.abs(dias)+ " dias - Retirar)";
        } else if(dias<= 15){
            return "PROXIMO A VENCER (" +dias + " dias - Liquidar 20% desc.)";
        } else if (getCantidad() <=getStockMinimo()) {
            return "STOCK BAJO (Vence: " +fechaVencimiento+ ")";
        }
        return "OPTIMO (Vence: "+ fechaVencimiento + ", Temp <= "+temperaturaMaxC + "C)";
    }

    @Override
    public String getTipoProducto(){
        return "Perecedero";
    }
}
