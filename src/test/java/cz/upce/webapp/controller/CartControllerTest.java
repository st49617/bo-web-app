package cz.upce.webapp.controller;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.service.CartServiceImpl;
import cz.upce.webapp.service.ItemServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private CartServiceImpl cartService;
    private ItemServiceImpl itemService;
    private Item itemDTO = null;

    @Before
    public void init() {

        itemDTO = new Item("testShowCart", null, 20.0, null, null);

        cartService = Mockito.mock(CartServiceImpl.class);

        itemService = Mockito.mock(ItemServiceImpl.class);

        cartController = new CartController(cartService, itemService);
    }

    @Test
    public void showCart() {

        Map<Item, Integer> nutItems = new HashMap<>();

        nutItems.put(itemDTO, 15);

        when(cartService.getNutItemsOnly()).thenReturn(nutItems);

        when(cartService.getTotalNuts(ArgumentMatchers.anyBoolean())).thenReturn(2.0);

        when(cartService.getWeightNuts()).thenReturn(2.0);

        when(cartService.getCountryLifeItemsOnly()).thenReturn(nutItems);

        when(cartService.getTotalCountryLife(ArgumentMatchers.anyBoolean())).thenReturn(5.0);

        when(cartService.getWeightCountryLife()).thenReturn(5.0);

        ConcurrentModel testModel = new ConcurrentModel();

        cartController.showCart(testModel);

        assertEquals(testModel.get("nutItems"), nutItems);
        assertEquals(testModel.get("totalNuts"), 2.0);
        assertEquals(testModel.get("weightNuts"), 2.0);
        assertEquals(testModel.get("countryLifeItems"), nutItems);
        assertEquals(testModel.get("totalCountryLife"), 5.0);
        assertEquals(testModel.get("weightCountryLife"), 5.0);

    }

    @Test
    public void addToCart() {
        ConcurrentModel testModel = new ConcurrentModel();
        Optional<Item> itemDTOOptional = Optional.of(itemDTO);
        when(itemService.findById(ArgumentMatchers.anyInt())).thenReturn(itemDTOOptional);

        assertEquals(cartController.addToCart(ArgumentMatchers.anyInt(),testModel), "cart/cart");
    }

    @Test
    public void removeFromCart() {
        ConcurrentModel testModel = new ConcurrentModel();
        Optional<Item> itemDTOOptional = Optional.of(itemDTO);
        when(itemService.findById(ArgumentMatchers.anyInt())).thenReturn(itemDTOOptional);

        assertEquals(cartController.removeFromCart(ArgumentMatchers.anyInt(),testModel), "cart/cart");
    }
}