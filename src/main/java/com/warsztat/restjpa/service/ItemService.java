package com.warsztat.restjpa.service;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.warsztat.restjpa.model.Item;
import com.warsztat.restjpa.model.Action;
import com.warsztat.restjpa.util.Loggable;

@Stateless
@LocalBean
@Loggable
public class ItemService extends AbstractService<Item> implements Serializable
{


   // ======================================
   // =            Constructors            =
   // ======================================

	private static final long serialVersionUID = 1L;

public ItemService()
   {
      super(Item.class);
   }

   // ======================================
   // =         Protected methods          =
   // ======================================

   @Override
   protected Predicate[] getSearchPredicates(Root<Item> root, Item example)
   {
      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String name = example.getName();
      if (name != null && !"".equals(name))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("name")), '%' + name.toLowerCase() + '%'));
      }
      String description = example.getDescription();
      if (description != null && !"".equals(description))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("description")), '%' + description.toLowerCase() + '%'));
      }
      String imagePath = example.getImagePath();
      if (imagePath != null && !"".equals(imagePath))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("imagePath")), '%' + imagePath.toLowerCase() + '%'));
      }
      Action action = example.getAction();
      if (action != null)
      {
         predicatesList.add(builder.equal(root.get("action"), action));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }
}