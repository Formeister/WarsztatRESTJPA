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
import com.warsztat.restjpa.model.OrderLine;
import com.warsztat.restjpa.util.Loggable;

@Stateless
@LocalBean
@Loggable
public class OrderLineService extends AbstractService<OrderLine> implements Serializable
{

   // ======================================
   // =            Constructors            =
   // ======================================

	private static final long serialVersionUID = 1L;

public OrderLineService()
   {
      super(OrderLine.class);
   }

   // ======================================
   // =         Protected methods          =
   // ======================================

   @Override
   protected Predicate[] getSearchPredicates(Root<OrderLine> root, OrderLine example)
   {
      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      Integer quantity = example.getQuantity();
      if (quantity != null && quantity.intValue() != 0)
      {
         predicatesList.add(builder.equal(root.get("quantity"), quantity));
      }
      Item item = example.getItem();
      if (item != null)
      {
         predicatesList.add(builder.equal(root.get("item"), item));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }
}