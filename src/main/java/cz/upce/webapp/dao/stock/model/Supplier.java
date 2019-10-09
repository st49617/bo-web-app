package cz.upce.webapp.dao.stock.model;

import javax.transaction.Transactional;
import java.io.Serializable;
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
}
