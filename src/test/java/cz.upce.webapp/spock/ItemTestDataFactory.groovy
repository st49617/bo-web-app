package cz.upce.webapp.spock;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component;

@Component
public class ItemTestDataFactory {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    SupplierRepository supplierRepository;

    public void save(String name) {
        Supplier s = supplierRepository.save(new Supplier(name: "Dodavatel A"))
        itemRepository.save(new Item(itemName: name, itemQuantity: 10, itemPrice: 100, itemTax: 15,supplier: s))
    }

}
