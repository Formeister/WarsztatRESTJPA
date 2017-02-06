package com.warsztat.restjpa.service;

import com.warsztat.restjpa.exceptions.ValidationException;
import com.warsztat.restjpa.model.User;
import com.warsztat.restjpa.model.OrderLine;
import com.warsztat.restjpa.model.PurchaseOrder;
import com.warsztat.restjpa.util.Loggable;
import com.warsztat.restjpa.view.actions.ShoppingCartItem;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Stateless
@LocalBean
@Loggable
public class PurchaseOrderService extends AbstractService<PurchaseOrder>implements Serializable
{

   // ======================================
   // =             Attributes             =
   // ======================================


   // ======================================
   // =              Public Methods        =
   // ======================================

	private static final long serialVersionUID = 1L;

public PurchaseOrder createOrder(@NotNull User user, final List<ShoppingCartItem> cartItems)
   {

      if (cartItems == null || cartItems.size() == 0)
         throw new ValidationException("Koszyk jest pusty.");

      PurchaseOrder order = new PurchaseOrder(entityManager.merge(user), user.getHomeAddress());

      Set<OrderLine> orderLines = new HashSet<>();

      for (ShoppingCartItem cartItem : cartItems)
      {
         orderLines.add(new OrderLine(cartItem.getQuantity(), entityManager.merge(cartItem.getItem())));
      }
      order.setOrderLines(orderLines);

      entityManager.persist(order);

      return order;
   }

   public PurchaseOrder findOrder(@NotNull Long orderId)
   {
      return entityManager.find(PurchaseOrder.class, orderId);
   }

   public List<PurchaseOrder> findAllOrders()
   {
      TypedQuery<PurchaseOrder> typedQuery = entityManager.createNamedQuery(PurchaseOrder.FIND_ALL, PurchaseOrder.class);
      return typedQuery.getResultList();
   }

   public void removeOrder(@NotNull PurchaseOrder order)
   {
      entityManager.remove(entityManager.merge(order));
   }

   // ======================================
   // =         Protected methods          =
   // ======================================

   @Override
   protected Predicate[] getSearchPredicates(Root<PurchaseOrder> root, PurchaseOrder example)
   {
      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String street = example.getUser().getHomeAddress().getStreet();
      if (street != null && !"".equals(street))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("street")), '%' + street.toLowerCase() + '%'));
      }
      String city = example.getUser().getHomeAddress().getCity();
      if (city != null && !"".equals(city))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("city")), '%' + city.toLowerCase() + '%'));
      }
      String zipcode = example.getUser().getHomeAddress().getZipcode();
      if (zipcode != null && !"".equals(zipcode))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("zipcode")), '%' + zipcode.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }
}
