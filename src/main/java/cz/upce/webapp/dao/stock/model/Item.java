package cz.upce.webapp.dao.stock.model;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

/**
 * @author Tomas Kodym
 */

@Entity(name = "Item")
@Table(name = "item")
@Transactional
public class Item implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)

    private Integer itemId;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Column(name = "item_quantity", nullable = false, length = 50)
    private Double itemQuantity;

    @Column(name = "item_price", nullable = false)
    private Double itemPrice;

    @Column(name = "item_tax", nullable = false)
    private Integer itemTax;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false)
    private Supplier supplier;

    public Item() { }

    public Item(String itemName, Double itemQuantity, Double itemPrice, Integer itemTax, Supplier supplier)
    {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemPrice = itemPrice;
        this.itemTax = itemTax;
        this.supplier = supplier;
    }

    public Integer getItemId()
    {
        return itemId;
    }

    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public Double getItemQuantity()
    {
        return itemQuantity;
    }

    public void setItemQuantity(Double itemQuantity)
    {
        this.itemQuantity = itemQuantity;
    }

    public Double getItemPrice()
    {
        return itemPrice;
    }

    public void setItemPrice(Double itemPrice)
    {
        this.itemPrice = itemPrice;
    }

    public Integer getItemTax()
    {
        return itemTax;
    }

    public void setItemTax(Integer itemTax)
    {
        this.itemTax = itemTax;
    }

    public Supplier getSupplier()
    {
        return supplier;
    }

    public void setSupplier(Supplier supplier)
    {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId.equals(item.itemId) &&
                itemName.equals(item.itemName) &&
                itemQuantity.equals(item.itemQuantity) &&
                itemPrice.equals(item.itemPrice) &&
                itemTax.equals(item.itemTax);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(itemId, itemName, itemQuantity, itemPrice, itemTax);
    }

    @Override
    public String toString()
    {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemQuantity='" + itemQuantity + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemTax=" + itemTax +
                '}';
    }
}