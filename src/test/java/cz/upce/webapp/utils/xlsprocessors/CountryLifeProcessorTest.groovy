package cz.upce.webapp.utils.xlsprocessors


import cz.upce.webapp.dao.stock.model.Supplier
import cz.upce.webapp.dao.stock.repository.SupplierRepository
import spock.lang.Specification

class CountryLifeProcessorTest extends Specification {

    SupplierRepository supplierRepo = Mock()

    def "IterateSheetValues"() {
        def f = getClass().getResource("/CountryLife_Objednavkovy_cenik_VO.xls").getFile()
        supplierRepo.getOne(_) >> new Supplier()

        when:
        def items = new CountrySheetProcessor(supplierRepository: supplierRepo).parseItemsAsMap(new File(f))
        then:

        items.size() > 0
        def jahlyBio = items["Jáhly   COUNTRY LIFE_5000_BIO"]
        jahlyBio.itemQuantity == 5000
        jahlyBio.itemTax == 15
        jahlyBio.itemPrice == 0.0462
        def jahly = items["Jáhly   COUNTRY LIFE_5000"]
        jahly.itemQuantity == 5000
        jahly.itemTax == 15
        jahly.itemPrice == 0.0349
        def merunky = items["Meruňky sušené_12500_BIO"]
        merunky.itemQuantity == 12500
        merunky.itemTax == 15
        merunky.itemPrice == 0.129




    }
}
