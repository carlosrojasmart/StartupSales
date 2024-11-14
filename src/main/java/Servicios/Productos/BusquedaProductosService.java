package Servicios.Productos;

import Modelos.Producto;
import Repositorios.Productos.BusquedaProductos;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BusquedaProductosService {

    private final BusquedaProductos busquedaProductos;

    // Constructor que recibe una instancia de BusquedaProductos
    public BusquedaProductosService(BusquedaProductos busquedaProductos) {
        this.busquedaProductos = busquedaProductos;
    }

    // Método para buscar productos por nombre
    public List<Producto> buscarProductosPorNombre(String nombreProducto) {
        try {
            // Llama al repositorio para obtener productos que coincidan con el nombre dado
            return busquedaProductos.buscarProductosPorNombre(nombreProducto);
        } catch (SQLException e) {
            // Maneja excepciones SQL e imprime la traza de error
            e.printStackTrace();
            // Retorna una lista vacía si ocurre un error
            return Collections.emptyList();
        }
    }

    // Método para buscar productos aplicando filtros adicionales
    public List<Producto> buscarConFiltros(String nombreProducto, String categoria, BigDecimal precioMin, BigDecimal precioMax) {
        try {
            // Llama al repositorio para obtener productos aplicando los filtros: nombre, categoría, precio mínimo y máximo
            return busquedaProductos.buscarConFiltros(nombreProducto, categoria, precioMin, precioMax);
        } catch (SQLException e) {
            // Maneja excepciones SQL e imprime la traza de error
            e.printStackTrace();
            // Retorna una lista vacía si ocurre un error
            return Collections.emptyList();
        }
    }
}
