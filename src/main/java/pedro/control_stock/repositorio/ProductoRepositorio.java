package pedro.control_stock.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import pedro.control_stock.modelo.Producto;

public interface ProductoRepositorio extends JpaRepository<Producto, Integer> {
}
