package cz.upce.webapp.dao


import cz.upce.webapp.dao.stock.repository.ItemRepository

import cz.upce.webapp.dao.testutil.ItemTestDataFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

@DataJpaTest
@Import(ItemTestDataFactory.class)
class ItemRepositoryTest extends Specification {

    @Autowired ItemRepository itemRepository;
    @Autowired ItemTestDataFactory itemTestDataFactory

    def "test"() {

        when:
        itemTestDataFactory.save("C mandle uzené")
        itemTestDataFactory.save("A mandle uzené")
        itemTestDataFactory.save("B mandle")

        def items = itemRepository.findAllSorted(0, 10)
        def itemsBy = itemRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle uzené")
        then:
        items.size() == 3
        items.get(1).itemName == "B mandle"
        itemsBy.size() == 2
        itemsBy.get(1).itemName == "C mandle uzené"
    }

}
