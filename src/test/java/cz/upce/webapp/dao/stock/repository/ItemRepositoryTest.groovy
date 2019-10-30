package cz.upce.webapp.dao

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.repository.ItemRepository
import cz.upce.webapp.dao.testutil.Creator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Unroll

@DataJpaTest
@Import(Creator.class)
class ItemRepositoryTest extends Specification {

    @Autowired ItemRepository itemRepository;
    @Autowired Creator creator

    def "test"() {

        given:
            // Prepare test data
            creator.save(new Item(itemName: "C mandle uzené"))
            creator.save(new Item(itemName: "A mandle uzené"))
            creator.save(new Item(itemName: "B mandle"))

        when:
            def items = itemRepository.findAllSorted(0, 10)
            def itemsBy = itemRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle uzené")
        then:
            items.size() == 3
            items.get(1).itemName == "B mandle"
            itemsBy.size() == 2
            itemsBy.get(1).itemName == "C mandle uzené"
    }


    @Unroll("When #items in db 'mandle' search returns #expectedItemsSize items")
    def "findAllByItemNameIgnoreCaseContainingOrderByItemName"() {

        given:
        def itemsToCreate = items.split(",")
        for (String item  : itemsToCreate) {
            creator.save(new Item(itemName: item))
        }

        when:
            def itemsBy = itemRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle")
        then:
            itemsBy.size() == expectedItemsSize

        where:
            items | expectedItemsSize
            "mandle A,mandle C" | 2
            "mandle,kešu" | 1
            "loupané mandle,kešu,rozinky" | 1

    }

}
