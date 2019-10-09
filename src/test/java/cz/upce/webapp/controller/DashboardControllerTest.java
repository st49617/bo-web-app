package cz.upce.webapp.controller;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.OrderForm;
import cz.upce.webapp.dao.stock.model.OrderState;
import cz.upce.webapp.dao.stock.model.OrderedProducts;
import cz.upce.webapp.service.OrderedProductServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;

import java.util.Arrays;

public class DashboardControllerTest {


    @Test
    public void getDashBoard() {
        OrderedProductServiceImpl orderedProductService = Mockito.mock(OrderedProductServiceImpl.class);
        DashboardController dashboardController = new DashboardController(orderedProductService, null);
        ConcurrentModel testModel = new ConcurrentModel();
        Item itemDTO = new Item("testShowCart", null, 20.0, null, null);


        OrderedProducts orderedProductsDTO1 = new OrderedProducts();
        orderedProductsDTO1.setAmount(5);
        orderedProductsDTO1.setItem(itemDTO);
        orderedProductsDTO1.setOrder(null);
        orderedProductsDTO1.setOrderedProductsId(20);
        OrderForm orderForm = new OrderForm();
        orderForm.setState(OrderState.CANCELED);
        orderedProductsDTO1.setOrderForm(orderForm);

        Mockito.when(orderedProductService.findAllOrderedProducts()).thenReturn(Arrays.asList(orderedProductsDTO1));

        dashboardController.getDashBoard(testModel);

        DashboardController.Order order = new DashboardController.Order(itemDTO, orderForm.getState(), 5);

        Assert.assertEquals(testModel.get("items"), Arrays.asList(order));

    }


}