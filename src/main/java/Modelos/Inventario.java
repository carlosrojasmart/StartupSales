package Modelos;

import java.util.List;

public class Inventario {
    // Atributos de la clase Inventario
    private int idInventario;
    private List<Producto> productos;
    private int cantidadTotal;
    private String tipoProducto;
    private String caracteristicaProducto;

    // getters y setters
    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public String getCaracteristicaProducto() {
        return caracteristicaProducto;
    }

    public void setCaracteristicaProducto(String caracteristicaProducto) {
        this.caracteristicaProducto = caracteristicaProducto;
    }
}
