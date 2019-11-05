package cz.upce.webapp.utils.xlsprocessors

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.repository.ItemRepository
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import spock.lang.Specification

class CountrySheetProcessorTest extends Specification {

    SupplierRepository s = Mock()
    ItemRepository itemRepository = Mock()
    CountrySheetProcessor processor = new CountrySheetProcessor(supplierRepository: s,
    itemRepository: itemRepository)

    def "IterateSheetValues"() {
        List<Item> savedItems
        when:
            processor.importItemsFromFile(new File(getClass().getResource("/objednavka_country.xls").getFile()))
        then:

            1 * itemRepository.saveAll(_) >> {arguments -> savedItems=arguments[0]}

            savedItems.size()>0
    }

}


