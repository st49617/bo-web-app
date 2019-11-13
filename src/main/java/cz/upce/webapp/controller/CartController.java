package cz.upce.webapp.controller;

import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.service.CartServiceImpl;
import cz.upce.webapp.service.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Tomas Kodym
 */

@Controller
public class CartController
{

    private CartServiceImpl cartService;

    private ItemServiceImpl itemService;

    @Autowired
    public CartController(CartServiceImpl cartService, ItemServiceImpl itemService) {
        this.cartService = cartService;
        this.itemService = itemService;
    }

    @GetMapping("/cart")
    public String showCart(Model model)
    {
        Map<Item, Integer> itemsInCart = cartService.getItemsInCart();

        model.addAttribute("totalItemsCount", itemsInCart.size());

        model.addAttribute("nutItems", cartService.getNutItemsOnly());
        model.addAttribute("totalNuts", cartService.getTotalNuts(false));
        model.addAttribute("totalNutsTax", cartService.getTotalNuts(true));
        model.addAttribute("weightNuts", cartService.getWeightNuts());

        model.addAttribute("countryLifeItems", cartService.getCountryLifeItemsOnly());
        model.addAttribute("totalCountryLife", cartService.getTotalCountryLife(false));
        model.addAttribute("totalCountryLifeTax", cartService.getTotalCountryLife(true));
        model.addAttribute("weightCountryLife", cartService.getWeightCountryLife());
        return "cart/cart";
    }

    @GetMapping("/cart/addItem/{itemId}")
    public String addToCart(@PathVariable Integer itemId, Model model)
    {
        itemService.findById(itemId).ifPresent(cartService::addItem);
        return showCart(model);
    }

    @GetMapping("/cart/removeItem/{itemId}")
    public String removeFromCart(@PathVariable Integer itemId, Model model)
    {
        itemService.findById(itemId).ifPresent(cartService::removeItem);
        return showCart(model);
    }

    @GetMapping("/cart/checkout")
    public void checkout(Model model, HttpServletResponse response) throws IOException {
        cartService.checkout();
        response.sendRedirect("/dashboard");
    }
}
