package cz.upce.webapp.dao

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.model.Supplier
import cz.upce.webapp.dao.stock.repository.ItemRepository
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import spock.lang.Specification

@DataJpaTest
class ItemRepositoryTest extends Specification {

    @Autowired ItemRepository itemRepository;
    @Autowired SupplierRepository supplierRepository;

    def "test"() {
        given:
        Supplier supplier = supplierRepository.save(new Supplier(name: "test"))

        when:
        saveItem(supplier, "C mandle uzené")
        saveItem(supplier, "A mandle uzené")
        saveItem(supplier, "B mandle")

        def items = itemRepository.findAllSorted(0, 10)
        def itemsBy = itemRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle uzené")
        then:
        items.size() == 3
        items.get(1).itemName == "B mandle"
        itemsBy.size() == 2
        itemsBy.get(1).itemName == "C mandle uzené"
    }

    private Item saveItem(Supplier supplier, String itemName) {
        itemRepository.save(
                new Item(itemName: itemName, itemPrice: 1, itemQuantity: 1, itemTax: 15, supplier: supplier)
        )
    }

}
