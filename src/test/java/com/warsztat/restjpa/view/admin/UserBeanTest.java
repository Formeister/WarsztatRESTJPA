package com.warsztat.restjpa.view.admin;

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
import com.warsztat.restjpa.view.admin.UserBean;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserBeanTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private UserBean userbean;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(UserBean.class)
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
      Assert.assertNotNull(userbean);
   }

   @Test
   public void should_crud()
   {
      // Creates an object
      Address address = new Address("Dummy value", "Dummy value", "DV");
      User user = new User("Dummy value", "Dummy value", "Dummy", "Dummy value", "Dummy value", address);

      // Inserts the object into the database
      userbean.setUser(user);
      userbean.create();
      userbean.update();
      user = userbean.getUser();
      assertNotNull(user.getId());

      // Finds the object from the database and checks it's the right one
      user = userbean.findById(user.getId());
      assertEquals("Dummy value", user.getFirstName());


      // Deletes the object from the database and checks it's not there anymore
      userbean.setId(user.getId());
      userbean.create();
      userbean.delete();
      user = userbean.findById(user.getId());
      assertNull(user);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      User example = new User();

      // Paginates through the example
      userbean.setExample(example);
      userbean.paginate();
      assertTrue((userbean.getPageItems().size() == userbean.getPageSize()) || (userbean.getPageItems().size() == userbean.getCount()));
   }
}
