package cz.upce.webapp.service;

import java.util.*;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.OrderForm;
import cz.upce.webapp.dao.stock.model.OrderState;
import cz.upce.webapp.dao.stock.model.OrderedProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Tomas Kodym
 */

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartServiceImpl
{
    private OrderFormServiceImpl orderFormService;

    private OrderedProductServiceImpl orderedProductService;

    @Autowired
    public CartServiceImpl(OrderFormServiceImpl orderFormService, OrderedProductServiceImpl orderedProductService) {
        this.orderFormService = orderFormService;
        this.orderedProductService = orderedProductService;
    }

    private Map<Item, Integer> cart = new HashMap<>();

    public void addItem(Item item)
    {
        if (cart.containsKey(item))
            cart.replace(item, cart.get(item) + 1);
        else
            cart.put(item, 1);
    }

    public void removeItem(Item item)
    {
        if (cart.containsKey(item))
        {
            if (cart.get(item) > 1)
                cart.replace(item, cart.get(item) - 1);
            else if (cart.get(item) == 1)
                cart.remove(item);
        }
    }

    public Map<Item, Integer> getItemsInCart()
    {
        return Collections.unmodifiableMap(cart);
    }

    public Map<Item, Integer> getNutItemsOnly()
    {
        Map<Item, Integer> nutItems = new HashMap<>();

        getItemsInCart().forEach((item, count) -> {
            if(item.getSupplier().getId() == 1){
                nutItems.put(item, count);
            }
        });

        return nutItems;
    }

    public Map<Item, Integer> getCountryLifeItemsOnly()
    {
        Map<Item, Integer> countryLifeItems = new HashMap<>();

        getItemsInCart().forEach((item, count) -> {
            if(item.getSupplier().getId() == 2){
                countryLifeItems.put(item, count);
            }
        });

        return countryLifeItems;
    }

    public Map<Item, Integer> getBionebioItemsOnly()
    {
        Map<Item, Integer> bioNebioItems = new HashMap<>();

        getItemsInCart().forEach((item, count) -> {
            if(item.getSupplier().getId() == 4){
                bioNebioItems.put(item, count);
            }
        });

        return bioNebioItems;
    }

    public void checkout()
    {
        OrderForm order = new OrderForm();
        order.setState(OrderState.NEW);
        orderFormService.addNewOrder(order);

        List<OrderedProducts> list = new ArrayList<>();

        cart.forEach((product, integer) -> {
            OrderedProducts orderedProduct = new OrderedProducts();
            orderedProduct.setAmount(integer);
            orderedProduct.setOrder(order);
            orderedProduct.setItem(product);

            list.add(orderedProduct);
        });

        orderedProductService.addAll(list);

        cart.clear();
    }

    public Double getTotal()
    {
        return cart.entrySet().stream()
                .map(entry -> entry.getKey().getItemPrice() * entry.getValue())
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public Double getTotalNuts(boolean withTax)
    {
        double sum = 0;
        Map<Item, Integer> nutItemsOnly = getNutItemsOnly();
        for (Map.Entry<Item, Integer> itemIntegerEntry : nutItemsOnly.entrySet()) {
            Item item = itemIntegerEntry.getKey();
            sum += (withTax?(1+0.01*item.getItemTax()):1)*item.getItemQuantity()*item.getItemPrice()*itemIntegerEntry.getValue();

        }
        return sum;
    }

    public Double getTotalCountryLife(boolean withTax)
    {
        return getCountryLifeItemsOnly().entrySet().stream()
                .map(entry ->countPrice(entry.getKey(), entry.getValue(), withTax))
                .reduce(Double::sum)
                .orElse(0.0);
    }
    public Double getTotalBionebio(boolean withTax)
    {
        return getBionebioItemsOnly().entrySet().stream()
                .map(entry ->countPrice(entry.getKey(), entry.getValue(), withTax))
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public Double getWeightNuts()
    {
        return getNutItemsOnly().entrySet().stream()
                .map(entry -> Double.valueOf(entry.getKey().getItemQuantity()) * entry.getValue())
                .reduce(Double::sum)
                .orElse(0.0);
    }

    public Double getWeightCountryLife()
    {
        return getCountryLifeItemsOnly().entrySet().stream()
                .map(entry -> Double.valueOf(entry.getKey().getItemQuantity()) * entry.getValue())
                .reduce(Double::sum)
                .orElse(0.0);
    }
    public Double getWeightBionebio()
    {
        return getBionebioItemsOnly().entrySet().stream()
                .map(entry -> Double.valueOf(entry.getKey().getItemQuantity()) * entry.getValue())
                .reduce(Double::sum)
                .orElse(0.0);
    }

    private Double countPrice(Item item, Integer value, boolean withTax)
    {
        Double price;
        Double pricePerQuantity = item.getItemPrice() * item.getItemQuantity();
        if(withTax){
            Double tax = pricePerQuantity * (item.getItemTax() / 100.0);
            price = (pricePerQuantity + tax) * value;
        }else {
            price =  pricePerQuantity * value;
        }

        return price;
    }
}