package cz.upce.webapp.dao

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.repository.ItemRepository
import cz.upce.webapp.dao.testutil.Creator
import cz.upce.webapp.dao.testutil.ItemTestDataFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

@DataJpaTest
@Import(Creator.class)
class ItemRepositoryTest extends Specification {

    @Autowired ItemRepository itemRepository;
    @Autowired Creator creator

    def "test"() {

        when:
        creator.save(new Item(itemName: "C mandle uzené"))
        creator.save(new Item(itemName: "A mandle uzené"))
        creator.save(new Item(itemName: "B mandle"))

        def items = itemRepository.findAllSorted(0, 10)
        def itemsBy = itemRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle uzené")
        then:
        items.size() == 3
        items.get(1).itemName == "B mandle"
        itemsBy.size() == 2
        itemsBy.get(1).itemName == "C mandle uzené"
    }

}
