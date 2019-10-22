package cz.upce.webapp.utils.xlsprocessors

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.repository.ItemRepository
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import spock.lang.Specification

import javax.persistence.EntityManager

class CountrySheetProcessorTest extends Specification {

    SupplierRepository s = Mock()
    ItemRepository itemRepository = Mock()
    CountrySheetProcessor processor = new CountrySheetProcessor(supplierRepository: s,
    itemRepository: itemRepository)

    def "IterateSheetValues"() {
        List<Item> savedItems
        when:
            processor.parseExcelFile(new File(getClass().getResource("/objednavka_country.xls").getFile()))
        then:

            1 * itemRepository.saveAll(_) >> {arguments -> savedItems=arguments[0]}

            savedItems.size()>0
    }

}


