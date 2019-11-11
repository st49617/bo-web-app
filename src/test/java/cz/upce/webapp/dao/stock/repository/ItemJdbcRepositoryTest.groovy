package cz.upce.webapp.dao.stock.repository

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.testutil.Creator
import org.junit.Rule
import org.junit.rules.TestName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Unroll

@DataJpaTest
@Import([Creator.class, ItemJdbcRepository.class])
class ItemJdbcRepositoryTest extends Specification {

    @Rule TestName testName = new TestName()

    @Autowired ItemJdbcRepository itemJdbcRepository;
    @Autowired Creator creator

    def "test"() {

        given:
            // Prepare test data
            creator.save(new Item(itemName: "BIO mandle uzené"))
            creator.save(new Item(itemName: "CountryLife mandle uzené"))
            creator.save(new Item(itemName: "mandle"))

        expect:
            itemJdbcRepository.findAllUsingSearch("-CountryLife uzené mandle").size() == 1
            itemJdbcRepository.findAllUsingSearch("mandle").size() == 3
            itemJdbcRepository.findAllUsingSearch("mandle uzené").size() == 2
            itemJdbcRepository.findAllUsingSearch("uzené mandle").size() == 2
            itemJdbcRepository.findAllUsingSearch("uzené MANDLE").size() == 2
            itemJdbcRepository.findAllUsingSearch("MANDLE BIO").size() == 1
    }


    @Unroll
    def "When #items in db 'mandle' search returns #expectedItemsSize items"() {

        given:
        def itemsToCreate = items.split(",")
        for (String item  : itemsToCreate) {
            creator.save(new Item(itemName: item))
        }

        when:
            def itemsBy = itemJdbcRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle")
        then:
            itemsBy.size() == expectedItemsSize

        where:
            items | expectedItemsSize
            "mandle A,mandle C" | 2
            "mandle,kešu" | 1
            "loupané mandle,kešu,rozinky" | 1

    }
    def "mandle A,mandle C=2"() { expect: oneTest()}
    def "mandle,kešu=1"() { expect: oneTest()}
    def "loupané mandle,kešu,rozinky=1"() { expect: oneTest()}

    def oneTest() {
        def testArguments = testName.methodName.split("=")
        def itemsToCreate = testArguments[0].split(",")
        def expectedSizeStr = testArguments[1]
        def expectedItemsSize = Integer.valueOf(expectedSizeStr)

        for (String item  : itemsToCreate) {
            creator.save(new Item(itemName: item))
        }

        def itemsBy = itemJdbcRepository.findAllByItemNameIgnoreCaseContainingOrderByItemName("mandle")
        return (itemsBy.size() == expectedItemsSize)

    }

}
