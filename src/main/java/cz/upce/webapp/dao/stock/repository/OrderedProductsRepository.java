package cz.upce.webapp.dao.stock.repository;

import cz.upce.webapp.dao.stock.model.OrderedProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tomas Kodym
 */
@Repository
public interface OrderedProductsRepository extends JpaRepository<OrderedProducts, Long> { }