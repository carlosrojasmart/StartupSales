package Servicios.Tienda;

import Modelos.Tienda;
import Repositorios.Tienda.MostrarTiendas;
import Repositorios.Tienda.CrearTienda;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class TiendaService {

    private final MostrarTiendas mostrarTiendas;
    private final CrearTienda crearTienda;  // Incluir el repositorio CrearTienda

    public TiendaService(MostrarTiendas mostrarTiendas, CrearTienda crearTienda) {
        this.mostrarTiendas = mostrarTiendas;
        this.crearTienda = crearTienda;
    }

    // Crear tienda con imagen
    public boolean crearTienda(String nombre, String descripcion, int idUsuario, String categoria, File archivoImagen) {
        try {
            crearTienda.crearTienda(nombre, descripcion, idUsuario, categoria, archivoImagen);
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener tiendas por ID de usuario
    public List<Tienda> obtenerTiendas(int idUsuario) {
        return mostrarTiendas.obtenerTiendas(idUsuario);
    }

    // Obtener tienda por ID de tienda
    public Tienda obtenerTiendaPorId(int idTienda) {
        return mostrarTiendas.obtenerTiendaPorId(idTienda);
    }

    // Buscar tiendas por nombre
    public List<Tienda> buscarTiendasPorNombre(String nombreTienda) {
        return mostrarTiendas.buscarTiendasPorNombre(nombreTienda);
    }

    // Eliminar tienda
    public boolean eliminarTienda(int idTienda) {
        return mostrarTiendas.eliminarTienda(idTienda);
    }

    // Actualizar información de una tienda
    public void actualizarTienda(Tienda tienda) {
        mostrarTiendas.actualizarTienda(tienda);
    }

    // Obtener categorías predefinidas
    public List<String> obtenerCategorias() {
        return mostrarTiendas.obtenerCategorias();
    }
}
