package cz.upce.webapp.utils.xlsprocessors


import cz.upce.webapp.dao.stock.model.Supplier
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import spock.lang.Specification

class KServisSheetProcessorTest extends Specification {

    SupplierRepository supplierRepo = Mock()

    def "IterateSheetValues"() {
        def f = getClass().getResource("/k-servis_cenik_listopad.xlsx").getFile()
        supplierRepo.getOne(_) >> new Supplier()

        when:
        def items = new KServisSheetProcessor(supplierRepository: supplierRepo).parseItemsAsMap(new File(f))
        then:

        items.size() > 0
        def item1 = items["Aloe Vera plátky_20000"]
        item1.itemTax == 15
        item1.itemPrice == 0.204

        def item2 = items["Zelený meloun plátky_20000"]
        item2.itemTax == 15
        item2.itemPrice == 0.159

        def item3 = items["Ananas kousky_1000"]
        item3.itemTax == 15
        item3.itemPrice == 0.715

        def item4 = items["Zahrádka (směs ovoce)_12000"]
        item4.itemTax == 15
        item4.itemPrice == 0.087


    }
}
