package com.warsztat.restjpa.service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.warsztat.restjpa.exceptions.ValidationException;
import com.warsztat.restjpa.model.Address;
import com.warsztat.restjpa.model.User;
import com.warsztat.restjpa.model.UserRole;
import com.warsztat.restjpa.service.AbstractService;
import com.warsztat.restjpa.service.UserService;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class UserServiceTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private UserService userservice;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(AbstractService.class)
            .addClass(UserService.class)
            .addClass(User.class)
            .addClass(Address.class)
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
      Assert.assertNotNull(userservice);
   }

   @Test
   public void should_crud()
   {
      // Gets all the objects
      int initialSize = userservice.listAll().size();

      // Creates an object
      Address address = new Address("Dummy value", "Dummy value", "DV");
      User user = new User("Dummy value", "Dummy value", "Dummy", "Dummy value", "Dummy value", address);

      // Inserts the object into the database
      user = userservice.persist(user);
      assertNotNull(user.getId());
      assertEquals(initialSize + 1, userservice.listAll().size());

      // Finds the object from the database and checks it's the right one
      user = userservice.findById(user.getId());
      assertEquals("Dummy value", user.getFirstName());

      // Updates the object
      user.setFirstName("A new value");
      user = userservice.merge(user);

      // Finds the object from the database and checks it has been updated
      user = userservice.findById(user.getId());
      assertEquals("A new value", user.getFirstName());

      // Deletes the object from the database and checks it's not there anymore
      userservice.remove(user);
      assertEquals(initialSize, userservice.listAll().size());
   }
}
