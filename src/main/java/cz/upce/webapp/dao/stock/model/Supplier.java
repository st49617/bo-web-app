package cz.upce.webapp.dao.stock.model;

import org.hibernate.ejb.HibernateEntityManagerFactory;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.sql.Blob;
import javax.persistence.*;

/**
 * @author Tomáš Kodym
 */

@Entity(name = "Suppliers")
@Table(name = "suppliers")
@Transactional
public class Supplier implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = StoredFile.class, fetch = FetchType.LAZY)
    private StoredFile pricelist;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public StoredFile getPricelist() {
        return pricelist;
    }

    public void setPricelist(StoredFile pricelist) {
        this.pricelist = pricelist;
    }
}
