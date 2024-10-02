package Servicios;

import Modelos.*;

import java.util.List;

public class NegocioStartupSales {
    private List<Cliente> clientes;
    private List<Venta> ventas;
    private List<Producto> productos;
    private List<Envio> envios;
    private List<Inventario> inventarios;

    public NegocioStartupSales(List<Cliente> clientes, List<Venta> ventas, List<Producto> productos,
                               List<Envio> envios, List<Inventario> inventarios) {
        this.clientes = clientes;
        this.ventas = ventas;
        this.productos = productos;
        this.envios = envios;
        this.inventarios = inventarios;
    }

    public void addCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public void removeCliente(Cliente cliente) {
        clientes.remove(cliente);
    }

    public void addVenta(Venta venta) {
        ventas.add(venta);
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void addProducto(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void addEnvio(Envio envio) {
        envios.add(envio);
    }

    public List<Envio> getEnvios() {
        return envios;
    }

    public void addInventario(Inventario inventario) {
        inventarios.add(inventario);
    }

    public List<Inventario> getInventarios() {
        return inventarios;
    }
}
