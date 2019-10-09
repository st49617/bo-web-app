package cz.upce.webapp.dao.stock.repository;

import cz.upce.webapp.dao.stock.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Tomas Kodym
 */

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>
{
    Optional<Item> findByItemId(Integer integer);

    List<Item> findAllByItemNameIgnoreCaseContaining(String name);
}
