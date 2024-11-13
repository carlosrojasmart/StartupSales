package Servicios.Compras;

import Modelos.Compra;
import Repositorios.Compras.ComprasRepo;

import java.util.List;

public class ComprasService {

    private final ComprasRepo comprasRepo;

    public ComprasService(ComprasRepo comprasRepo) {
        this.comprasRepo = comprasRepo;
    }

    public List<Compra> obtenerComprasUsuario(int idUsuario) {
        return comprasRepo.obtenerComprasPorUsuario(idUsuario);
    }

    public boolean usuarioTieneCompras(int idUsuario) {
        return comprasRepo.contarComprasPorUsuario(idUsuario) > 0;
    }
}
