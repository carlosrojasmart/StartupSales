package Modelos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Factura {
    private int numeroFactura;
    private Date fecha;
    private BigDecimal total; // Cambiado a BigDecimal
    private int idVenta;
    private int cantidad_total;
    private List<Venta> detalleVenta;

    // Getters y Setters
    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public List<Venta> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<Venta> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public BigDecimal getTotal() { // Cambiado a BigDecimal
        return total;
    }

    public void setTotal(BigDecimal total) { // Cambiado a BigDecimal
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getCantidad_total() {
        return cantidad_total;
    }

    public void setCantidad_total(int cantidad_total) {
        this.cantidad_total = cantidad_total;
    }
}

