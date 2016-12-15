package com.warsztat.restjpa.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Embeddable
public class Address implements Serializable
{

   // ======================================
   // = Attributes =
   // ======================================

   private static final long serialVersionUID = 1L;

   @Column(length = 50, nullable = false)
   @Size(min = 5, max = 50)
   @NotNull
   private String street;

   @Column(length = 50, nullable = false)
   @Size(min = 2, max = 50)
   @NotNull
   private String city;

   @Column(length = 10, name = "zip_code", nullable = false)
   @Size(min = 1, max = 10)
   @NotNull
   private String zipcode;

   // ======================================
   // = Constructors =
   // ======================================

   public Address()
   {
   }

   public Address(String street, String city, String zipcode)
   {
      this.street = street;
      this.city = city;
      this.zipcode = zipcode;
   }

   // ======================================
   // = Getters & setters =
   // ======================================

   public String getStreet()
   {
      return street;
   }

   public void setStreet(String street)
   {
      this.street = street;
   }

   public String getCity()
   {
      return city;
   }

   public void setCity(String city)
   {
      this.city = city;
   }

   public String getZipcode()
   {
      return zipcode;
   }

   public void setZipcode(String zipcode)
   {
      this.zipcode = zipcode;
   }

   // ======================================
   // = Methods hash, equals, toString =
   // ======================================

   @Override
   public final boolean equals(Object o)
   {
      if (this == o)
         return true;
      if (!(o instanceof Address))
         return false;
      Address address = (Address) o;
      return Objects.equals(street, address.street) &&
               Objects.equals(city, address.city) &&
               Objects.equals(zipcode, address.zipcode);
   }

   @Override
   public final int hashCode()
   {
      return Objects.hash(street, city, zipcode);
   }

   @Override
   public String toString()
   {
      return "Adres{" +
               "ulica='" + street + '\'' +
               ", miasto='" + city + '\'' +
               ", kod pocztowy='" + zipcode + '\'' +
               '}';
   }
}
