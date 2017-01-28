package com.warsztat.restjpa.view.admin;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.warsztat.restjpa.exceptions.ValidationException;
import com.warsztat.restjpa.model.*;
import com.warsztat.restjpa.view.actions.ShoppingCartItem;
import com.warsztat.restjpa.view.admin.PurchaseOrderBean;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class PurchaseOrderBeanTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private PurchaseOrderBean purchaseorderbean;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(PurchaseOrderBean.class)
            .addClass(PurchaseOrder.class)
            .addClass(Address.class)
            .addClass(User.class)
            .addClass(OrderLine.class)
            .addClass(Category.class)
            .addClass(Action.class)
            .addClass(Item.class)
            .addClass(ShoppingCartItem.class)
            .addClass(UserRole.class)
            .addClass(ValidationException.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   // ======================================
   // =             Test Cases             =
   // ======================================

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(purchaseorderbean);
   }

   @Test @Ignore
   public void should_crud()
   {
      // Creates an object
      Address address = new Address("78 Gnu Rd", "Texas", "666");
      User user = new User("Paul", "Mc Cartney", "pmac", "pmac", "paul@beales.com", address);
      Set<OrderLine> orderLines = new HashSet<>();
      PurchaseOrder purchaseOrder = new PurchaseOrder(user, address);
      purchaseOrder.setOrderLines(orderLines);
      purchaseOrder.setDiscount(12.5F);

      // Inserts the object into the database
      purchaseorderbean.setPurchaseOrder(purchaseOrder);
      purchaseorderbean.create();
      purchaseorderbean.update();
      purchaseOrder = purchaseorderbean.getPurchaseOrder();
      assertNotNull(purchaseOrder.getId());

      // Finds the object from the database and checks it's the right one
      purchaseOrder = purchaseorderbean.findById(purchaseOrder.getId());
      assertEquals(new Float(12.5F), purchaseOrder.getDiscountRate());

      // Deletes the object from the database and checks it's not there anymore
      purchaseorderbean.setId(purchaseOrder.getId());
      purchaseorderbean.create();
      purchaseorderbean.delete();
      purchaseOrder = purchaseorderbean.findById(purchaseOrder.getId());
      assertNull(purchaseOrder);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      PurchaseOrder example = new PurchaseOrder();

      // Paginates through the example
      purchaseorderbean.setExample(example);
      purchaseorderbean.paginate();
      assertTrue((purchaseorderbean.getPageItems().size() == purchaseorderbean.getPageSize()) || (purchaseorderbean.getPageItems().size() == purchaseorderbean.getCount()));
   }
}
