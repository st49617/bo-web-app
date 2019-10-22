package cz.upce.webapp.dao.stock.repository;

import cz.upce.webapp.dao.stock.model.Item;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.OrderBy;
import java.util.List;
import java.util.Optional;

/**
 * @author Tomas Kodym
 */

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>
{
    Optional<Item> findByItemId(Integer integer);

    @OrderBy
    List<Item> findAllByItemNameIgnoreCaseContainingOrderByItemName(String name);

    default List<Item> findAllSorted(int page, int itemsPerPage) {
        Pageable firstPageWithTwoElements = PageRequest.of(page, itemsPerPage, Sort.by(Sort.Direction.ASC, "itemName"));
        List<Item> items = findAll(firstPageWithTwoElements).getContent();
        return items;
    }


}
