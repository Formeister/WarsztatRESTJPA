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
import com.warsztat.restjpa.model.Action;
import com.warsztat.restjpa.service.AbstractService;
import com.warsztat.restjpa.service.ActionService;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class ActionServiceTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private ActionService actionservice;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(AbstractService.class)
            .addClass(ActionService.class)
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
      Assert.assertNotNull(actionservice);
   }

   @Test
   public void should_crud()
   {
      // Gets all the objects
      int initialSize = actionservice.listAll().size();

      // Creates an object
      Category category = new Category("Dummy value", "Dummy value");
      Action action = new Action("Dummy value", "Dummy value", category);

      // Inserts the object into the database
      action = actionservice.persist(action);
      assertNotNull(action.getId());
      assertEquals(initialSize + 1, actionservice.listAll().size());

      // Finds the object from the database and checks it's the right one
      action = actionservice.findById(action.getId());
      assertEquals("Dummy value", action.getName());

      // Updates the object
      action.setName("A new value");
      action = actionservice.merge(action);

      // Finds the object from the database and checks it has been updated
      action = actionservice.findById(action.getId());
      assertEquals("A new value", action.getName());

      // Deletes the object from the database and checks it's not there anymore
      actionservice.remove(action);
      assertEquals(initialSize, actionservice.listAll().size());
   }
}
