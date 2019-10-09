package cz.upce.webapp.dao.stock.model;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Set;

/**
 * @author Tomas Kodym
 */

@Entity(name = "OrderForm")
@Table(name = "order_form")
@Transactional
public class OrderForm
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Integer orderFormId;

    @OneToMany(mappedBy = "orderedProductsId", cascade = CascadeType.ALL)
    private Set<OrderedProducts> orderedProducts;

    @Enumerated(EnumType.STRING)
    private OrderState state;


    public Integer getOrderFormId()
    {
        return orderFormId;
    }

    public void setOrderFormId(Integer orderFormId)
    {
        this.orderFormId = orderFormId;
    }

    public Set<OrderedProducts> getOrderedProducts()
    {
        return orderedProducts;
    }

    public void setOrderedProducts(Set<OrderedProducts> orderedProducts)
    {
        this.orderedProducts = orderedProducts;
    }

    public OrderState getState()
    {
        return state;
    }

    public void setState(OrderState state)
    {
        this.state = state;
    }
}

