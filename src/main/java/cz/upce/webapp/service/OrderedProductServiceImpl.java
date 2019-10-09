package cz.upce.webapp.service;

import java.util.List;

import cz.upce.webapp.dao.stock.model.OrderedProducts;
import cz.upce.webapp.dao.stock.repository.OrderedProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tomas Kodym
 */

@Service
public class OrderedProductServiceImpl
{

    @Autowired
    OrderedProductsRepository orderedProductsRepository;

    public void addAll(List<OrderedProducts> orderedProductList)
    {
        orderedProductsRepository.saveAll(orderedProductList);
    }

    public List<OrderedProducts> findAllOrderedProducts()
    {
        return orderedProductsRepository.findAll();
    }
}
