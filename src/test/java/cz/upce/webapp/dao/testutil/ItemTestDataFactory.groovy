package cz.upce.webapp.dao.testutil;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.ItemRepository
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemTestDataFactory implements InitializingBean{
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private SupplierRepository supplierRepository

    private Supplier supplier


    public Item save(String itemName) {
        save(supplier, itemName)
    }
    public Item save(Supplier supplier, String itemName) {
        itemRepository.save(
                new Item(itemName: itemName, itemPrice: 1, itemQuantity: 1, itemTax: 15, supplier: supplier)
        )
    }

    @Override
    void afterPropertiesSet() throws Exception {
        supplier = supplierRepository.save(new Supplier(name: "test"))

    }
}
