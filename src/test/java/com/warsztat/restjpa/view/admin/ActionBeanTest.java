package com.warsztat.restjpa.view.admin;

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
import com.warsztat.restjpa.view.admin.ActionBean;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class ActionBeanTest
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @Inject
   private ActionBean actionbean;

   // ======================================
   // =             Deployment             =
   // ======================================

   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
            .addClass(ActionBean.class)
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
      Assert.assertNotNull(actionbean);
   }

   @Test
   public void should_crud()
   {
      // Creates an object
      Category category = new Category("Dummy value", "Dummy value");
      Action action = new Action("Dummy value", "Dummy value", category);

      // Inserts the object into the database
      actionbean.setAction(action);
      actionbean.create();
      actionbean.update();
      action = actionbean.getAction();
      assertNotNull(action.getId());

      // Finds the object from the database and checks it's the right one
      action = actionbean.findById(action.getId());
      assertEquals("Dummy value", action.getName());

      // Deletes the object from the database and checks it's not there anymore
      actionbean.setId(action.getId());
      actionbean.create();
      actionbean.delete();
      action = actionbean.findById(action.getId());
      assertNull(action);
   }

   @Test
   public void should_paginate()
   {
      // Creates an empty example
      Action example = new Action();

      // Paginates through the example
      actionbean.setExample(example);
      actionbean.paginate();
      assertTrue((actionbean.getPageItems().size() == actionbean.getPageSize()) || (actionbean.getPageItems().size() == actionbean.getCount()));
   }
}
