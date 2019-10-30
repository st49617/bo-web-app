package cz.upce.webapp.services;

import static org.junit.Assert.*;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.service.OrderFormServiceImpl;
import cz.upce.webapp.service.OrderedProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import cz.upce.webapp.service.CartServiceImpl;

/**
 * @author Tomas Kodym
 */
public class CartServiceImplTest {

    private Supplier orisek = new Supplier();

    private Supplier county = new Supplier();

    private Item item = new Item("Aloe", 22.0, 250.0, 10, orisek);

    private Item item1 = new Item("Mandle", 22.0, 250.0, 10, county);

    @Autowired
    private OrderFormServiceImpl orderFormService;

    @Autowired
    private OrderedProductServiceImpl orderedProductService;

    private CartServiceImpl cartService = new CartServiceImpl(orderFormService, orderedProductService);

    @Before
    public void setUp() {
        orisek.setId(1);
        county.setId(2);
        orisek.setName("Orisek");
        county.setName("Country");
        cartService.addItem(item);
        cartService.addItem(item);
        cartService.addItem(item1);
    }

    @Test
    public void addItem() {
        assertEquals(2, this.cartService.getItemsInCart().size());
    }

    @Test
    public void removeSameItem() {
        this.cartService.removeItem(item);
        assertEquals(2, this.cartService.getItemsInCart().size());
    }

    @Test
    public void removeDifferentItem() {
        this.cartService.removeItem(item1);
        assertEquals(1, this.cartService.getItemsInCart().size());
    }

    @Test
    public void getItemsInCart() {
        assertEquals(2, this.cartService.getItemsInCart().size());
    }

    @Test
    public void getNutItemsOnly() {

        assertEquals(1, this.cartService.getNutItemsOnly().size());
    }

    @Test
    public void getCountryLifeItemsOnly() {
        assertEquals(1, this.cartService.getCountryLifeItemsOnly().size());
    }

    @Test
    public void getTotal() {
        assertEquals(750.0, this.cartService.getTotal(), 2);
    }

    @Test
    public void getTotalNuts() {
        assertEquals(11000, this.cartService.getTotalNuts(false), 2);
    }

    @Test
    public void getTotalNutsTax() {
        assertEquals(12100, this.cartService.getTotalNuts(true), 2);
    }

    @Test
    public void getTotalCountryLife() {
        assertEquals(5500, this.cartService.getTotalCountryLife(false), 2);
    }

    @Test
    public void getTotalCountryLifeTax() {
        assertEquals(6050, this.cartService.getTotalCountryLife(true), 2);
    }

    @Test
    public void getWeightNuts() {
        assertEquals(44, this.cartService.getWeightNuts(), 2);
    }

    @Test
    public void getWeightCountryLife() {
        assertEquals(22, this.cartService.getWeightCountryLife(), 2);
    }
}