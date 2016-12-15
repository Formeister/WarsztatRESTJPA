package com.warsztat.restjpa.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.warsztat.restjpa.constraints.NotEmpty;
import com.warsztat.restjpa.constraints.Price;


@Entity
@Cacheable
@NamedQueries({
         @NamedQuery(name = Item.FIND_BY_ACTION_ID, query = "SELECT i FROM Item i WHERE i.action.id = :actionId"),
         @NamedQuery(name = Item.SEARCH, query = "SELECT i FROM Item i WHERE UPPER(i.name) LIKE :keyword OR UPPER(i.action.name) LIKE :keyword ORDER BY i.action.category.name, i.action.name"),
         @NamedQuery(name = Item.FIND_ALL, query = "SELECT i FROM Item i")
})
@XmlRootElement
public class Item implements Serializable
{

   // ======================================
   // = Attributes =
   // ======================================

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   @Version
   @Column(name = "version")
   private int version;

   @Column(length = 30, nullable = false)
   @NotNull
   @Size(min = 1, max = 30)
   private String name;

   @Column(length = 3000, nullable = false)
   @NotNull
   @Size(max = 3000)
   private String description;

   @Column(name = "image_path")
   @NotEmpty
   private String imagePath;

   @Column(name = "unit_cost", nullable = false)
   @NotNull
   @Price
   private Float unitCost;

   @ManyToOne(cascade = CascadeType.MERGE)
   @JoinColumn(name = "action_fk", nullable = false)
   @XmlTransient
   private Action action;

   // ======================================
   // = Constants =
   // ======================================

   public static final String FIND_BY_ACTION_ID = "Item.findByActionId";
   public static final String SEARCH = "Item.search";
   public static final String FIND_ALL = "Item.findAll";

   // ======================================
   // = Constructors =
   // ======================================

   public Item()
   {
   }

   public Item(String name, Float unitCost, String imagePath, String description, Action action)
   {
      this.name = name;
      this.unitCost = unitCost;
      this.imagePath = imagePath;
      this.description = description;
      this.action = action;
   }

   // ======================================
   // = Getters & setters =
   // ======================================

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getImagePath()
   {
      return imagePath;
   }

   public void setImagePath(String imagePath)
   {
      this.imagePath = imagePath;
   }

   public Float getUnitCost()
   {
      return unitCost;
   }

   public void setUnitCost(Float unitCost)
   {
      this.unitCost = unitCost;
   }

   public Action getAction()
   {
      return this.action;
   }

   public void setAction(final Action action)
   {
      this.action = action;
   }

   // ======================================
   // = Methods hash, equals, toString =
   // ======================================

   @Override
   public final boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (!(o instanceof Item))
         return false;
      Item item = (Item) o;
      return Objects.equals(name, item.name) &&
               Objects.equals(description, item.description);
   }

   @Override
   public final int hashCode()
   {
      return Objects.hash(name, description);
   }

   @Override
   public String toString()
   {
      return "Produkt{" +
               "id=" + id +
               ", version=" + version +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", imagePath='" + imagePath + '\'' +
               ", unitCost=" + unitCost +
               ", action=" + action +
               '}';
   }
}
