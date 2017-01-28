package com.warsztat.restjpa.service;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.warsztat.restjpa.model.Category;
import com.warsztat.restjpa.model.Item;
import com.warsztat.restjpa.model.Action;
import com.warsztat.restjpa.service.AbstractService;
import com.warsztat.restjpa.service.ItemService;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class ItemServiceTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private ItemService itemservice;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(AbstractService.class)
            .addClass(ItemService.class)
            .addClass(Item.class)
            .addClass(Action.class)
            .addClass(Category.class)
            .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   // ======================================
   // =             Test Cases             =
   // ======================================

   @Test
   public void should_be_deployed()
   {
      Assert.assertNotNull(itemservice);
   }

   @Test
   public void should_crud()
   {
      // Gets all the objects
      int initialSize = itemservice.listAll().size();

      // Creates an object
      Category category = new Category("Dummy value", "Dummy value");
      Action action = new Action("Dummy value", "Dummy value", category);
      Item item = new Item("Dummy value", 10f, "Dummy value", "Dummy value", action);

      // Inserts the object into the database
      item = itemservice.persist(item);
      assertNotNull(item.getId());
      assertEquals(initialSize + 1, itemservice.listAll().size());

      // Finds the object from the database and checks it's the right one
      item = itemservice.findById(item.getId());
      assertEquals("Dummy value", item.getName());

      // Updates the object
      item.setName("A new value");
      item = itemservice.merge(item);

      // Finds the object from the database and checks it has been updated
      item = itemservice.findById(item.getId());
      assertEquals("A new value", item.getName());

      // Deletes the object from the database and checks it's not there anymore
      itemservice.remove(item);
      assertEquals(initialSize, itemservice.listAll().size());
   }
}
