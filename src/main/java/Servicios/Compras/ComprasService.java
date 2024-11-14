package Servicios.Compras;

import Modelos.Compra;
import Repositorios.Compras.ComprasRepo;

import java.util.List;

public class ComprasService {

    // Instancia de la clase ComprasRepo, que maneja las operaciones de la base de datos
    private final ComprasRepo comprasRepo;

    // Constructor que inicializa ComprasService con un objeto ComprasRepo
    public ComprasService(ComprasRepo comprasRepo) {
        this.comprasRepo = comprasRepo;
    }

    // Obtiene la lista de compras realizadas por un usuario específico
    public List<Compra> obtenerComprasUsuario(int idUsuario) {
        return comprasRepo.obtenerComprasPorUsuario(idUsuario); // Llama al método del repositorio para obtener las compras del usuario
    }

    // Verifica si el usuario tiene al menos una compra realizada
    public boolean usuarioTieneCompras(int idUsuario) {
        // Cuenta las compras del usuario y devuelve true si hay al menos una
        return comprasRepo.contarComprasPorUsuario(idUsuario) > 0;
    }
}
