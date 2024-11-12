package Servicios.Productos;

import Modelos.Producto;
import Repositorios.Productos.BusquedaProductos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BusquedaProductosService {

    private final BusquedaProductos busquedaProductos;

    public BusquedaProductosService(BusquedaProductos busquedaProductos) {
        this.busquedaProductos = busquedaProductos;
    }

    public List<Producto> buscarProductosPorNombre(String nombreProducto, Connection connection) {
        try {
            return busquedaProductos.buscarProductosPorNombre(nombreProducto);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Producto> buscarConFiltros(String nombreProducto, String categoria, BigDecimal precioMin, BigDecimal precioMax) {
        try {
            return busquedaProductos.buscarConFiltros(nombreProducto, categoria, precioMin, precioMax);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
