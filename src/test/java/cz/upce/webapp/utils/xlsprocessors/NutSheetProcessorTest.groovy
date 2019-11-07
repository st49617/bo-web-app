package cz.upce.webapp.utils.xlsprocessors

import cz.upce.webapp.dao.stock.model.Item
import cz.upce.webapp.dao.stock.model.Supplier
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import spock.lang.Specification

class NutSheetProcessorTest extends Specification {

    SupplierRepository supplierRepo = Mock()

    def "IterateSheetValues"() {
        def f = getClass().getResource("/orisek_01.03.2019.xls").getFile()
        supplierRepo.getOne(_) >> new Supplier()

        when:
        def items = new NutSheetProcessor(supplierRepository: supplierRepo).parseItemsAsMap(new File(f))
        then:

        items.size() > 0
        def aloeVera = items["Aloe Vera_1000"]
        aloeVera.itemQuantity == 1000
        aloeVera.itemTax == 15
        aloeVera.itemPrice == 0.239

        def zazvor = items["Zázvor v hořké čokoládě - Anglie_3000"]
        zazvor.itemQuantity == 3000
        zazvor.itemTax == 15
        zazvor.itemPrice == 0.121

        def pinie = items["Piniové oříšky_1000"]
        pinie.itemQuantity == 1000
        pinie.itemTax == 15
        pinie.itemPrice == 0.732


    }
}
