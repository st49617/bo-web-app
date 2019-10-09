package cz.upce.webapp.dao.stock.model;

import javax.persistence.*;
import javax.transaction.Transactional;

/**
 * @author Tomas Kodym
 */

@Entity(name = "OrderedProducts")
@Table(name = "ordered_products")
@Transactional
public class OrderedProducts
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_products_id", nullable = false, unique = true, updatable = false)
    private Integer orderedProductsId;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Item item;

    @ManyToOne(cascade = CascadeType.ALL)
    private OrderForm orderForm;

    @Column(name = "amount")
    private Integer amount;

    public Integer getOrderedProductsId()
    {
        return orderedProductsId;
    }

    public void setOrderedProductsId(Integer orderedProductsId)
    {
        this.orderedProductsId = orderedProductsId;
    }

    public OrderForm getOrderForm()
    {
        return orderForm;
    }

    public void setOrderForm(OrderForm orderForm)
    {
        this.orderForm = orderForm;
    }

    public void setOrder(OrderForm orderForm)
    {
        this.orderForm = orderForm;
    }

    public Item getItem()
    {
        return item;
    }

    public void setItem(Item item)
    {
        this.item = item;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }
}
