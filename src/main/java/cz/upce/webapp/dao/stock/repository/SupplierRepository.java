package cz.upce.webapp.dao.stock.repository;

import cz.upce.webapp.dao.stock.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jan Houžvička
 */

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> { }