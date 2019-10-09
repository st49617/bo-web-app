package cz.upce.webapp.service;

import cz.upce.webapp.dao.stock.model.OrderForm;
import cz.upce.webapp.dao.stock.repository.OrderFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tomas Kodym
 */

@Service
public class OrderFormServiceImpl
{
    @Autowired
    OrderFormRepository orderFormRepository;

    public void addNewOrder(OrderForm orderForm)
    {
        orderFormRepository.save(orderForm);
    }
}