package cz.upce.webapp.spock

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.model.Supplier
import cz.upce.webapp.dao.stock.repository.ItemRepository
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import;
import spock.lang.Specification;

@DataJpaTest
@Import(Creator)
public class HelloWorldTest extends Specification {

    @Autowired
    SupplierRepository supplierRepository;

/*    @Autowired
    ItemTestDataFactory itemTestDataFactory;*/

    @Autowired
    Creator creator;

    @Autowired
    ItemRepository itemRepository;

    def "test"(){

        when:
            def pow = Math.pow(2, 4)
        then:
            pow == 16

    }

    def "testItemRepository"(){
        expect:
            creator != null;
    }
    def "testItemRepositorySort"(){
        given:
            creator.save(getItem("C Mandle uzené"))
            creator.save(getItem("A Mandle"))
            creator.save(getItem("B Mandle"))
         when:
            def items = creator.findAllSorted(0,10)
        then:
           items.size() == 3
           items.get(1).itemName.equals("B Mandle")
    }

    private Item getItem(String name) {
        Supplier s = supplierRepository.save(new Supplier(name: "Dodavatel A"))
        new Item(itemName: name, itemQuantity: 10, itemPrice: 100, itemTax: 15,supplier: s)
    }


    def "testItemFactoryCreator"(){
        given:
        creator.save(
                new Item(itemName:  "C Mandle uzené"),
                new Item(itemName:  "A Mandle", supplier: creator.save(new Supplier(name: "A orisek"))),
                new Item(itemName:  "B Mandle")
        )
        when:
        def items = itemRepository.findAllSorted(0,10)
        then:
        items.size() == 3
        items.get(1).itemName.equals("B Mandle")
    }



}
