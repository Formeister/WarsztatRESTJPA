package com.warsztat.restjpa.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Cacheable
@NamedQueries({
         // TODO fetch doesn't work with GlassFish
         // @NamedQuery(name = Action.FIND_BY_CATEGORY_NAME, query =
         // "SELECT p FROM Action p LEFT JOIN FETCH p.items LEFT JOIN FETCH p.category WHERE p.category.name =
         // :pname"),
         @NamedQuery(name = Action.FIND_BY_CATEGORY_NAME, query = "SELECT p FROM Action p WHERE p.category.name = :pname"),
         @NamedQuery(name = Action.FIND_ALL, query = "SELECT p FROM Action p")
})
@XmlRootElement
public class Action implements Serializable
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

   @ManyToOne(cascade = CascadeType.MERGE)
   @JoinColumn(name = "category_fk", nullable = false)
   @XmlTransient
   private Category category;

   // ======================================
   // = Constants =
   // ======================================

   public static final String FIND_BY_CATEGORY_NAME = "Action.findByCategoryName";
   public static final String FIND_ALL = "Action.findAll";

   // ======================================
   // = Constructors =
   // ======================================

   public Action()
   {
   }

   public Action(String name, String description, Category category)
   {
      this.name = name;
      this.description = description;
      this.category = category;
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

   public Category getCategory()
   {
      return this.category;
   }

   public void setCategory(final Category category)
   {
      this.category = category;
   }

   // ======================================
   // = Methods hash, equals, toString =
   // ======================================

   @Override
   public final boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (!(o instanceof Action))
         return false;
      Action action = (Action) o;
      return Objects.equals(name, action.name) &&
               Objects.equals(description, action.description);
   }

   @Override
   public final int hashCode()
   {
      return Objects.hash(name, description);
   }

   @Override
   public String toString()
   {
      return name;
   }
}