package com.warsztat.restjpa.service;

import com.warsztat.restjpa.model.User;
import com.warsztat.restjpa.util.Loggable;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Stateless
@LocalBean
@Loggable
public class UserService extends AbstractService<User> implements Serializable
{

   // ======================================
   // =            Constructors            =
   // ======================================

	private static final long serialVersionUID = 1L;

public UserService()
   {
      super(User.class);
   }

   // ======================================
   // =             Attributes             =
   // ======================================


   // ======================================
   // =              Public Methods        =
   // ======================================

   public boolean doesLoginAlreadyExist(@NotNull String login)
   {

      // Login has to be unique
      TypedQuery<User> typedQuery = entityManager.createNamedQuery(User.FIND_BY_LOGIN, User.class);
      typedQuery.setParameter("login", login);
      try
      {
         typedQuery.getSingleResult();
         return true;
      }
      catch (NoResultException e)
      {
         return false;
      }
   }

   public User createUser(@NotNull User user)
   {
      entityManager.persist(user);
      return user;
   }

   public User findUser(@NotNull String login)
   {
      TypedQuery<User> typedQuery = entityManager.createNamedQuery(User.FIND_BY_LOGIN, User.class);
      typedQuery.setParameter("login", login);

      try
      {
         return typedQuery.getSingleResult();
      }
      catch (NoResultException e)
      {
         return null;
      }
   }

   public User findUser(@NotNull String login, @NotNull String password)
   {
      TypedQuery<User> typedQuery = entityManager.createNamedQuery(User.FIND_BY_LOGIN_PASSWORD, User.class);
      typedQuery.setParameter("login", login);
      typedQuery.setParameter("password", password);

      return typedQuery.getSingleResult();
   }

   public List<User> findAllUsers()
   {
      TypedQuery<User> typedQuery = entityManager.createNamedQuery(User.FIND_ALL, User.class);
      return typedQuery.getResultList();
   }

   public User updateUser(@NotNull User user)
   {
      entityManager.merge(user);
      return user;
   }

   public void removeUser(@NotNull User user)
   {
      entityManager.remove(entityManager.merge(user));
   }

   // ======================================
   // =         Protected methods          =
   // ======================================

   @Override
   protected Predicate[] getSearchPredicates(Root<User> root, User example)
   {
      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String firstName = example.getFirstName();
      if (firstName != null && !"".equals(firstName))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("firstName")), '%' + firstName.toLowerCase() + '%'));
      }
      String lastName = example.getLastName();
      if (lastName != null && !"".equals(lastName))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("lastName")), '%' + lastName.toLowerCase() + '%'));
      }
      String telephone = example.getTelephone();
      if (telephone != null && !"".equals(telephone))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("telephone")), '%' + telephone.toLowerCase() + '%'));
      }
      String email = example.getEmail();
      if (email != null && !"".equals(email))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("email")), '%' + email.toLowerCase() + '%'));
      }
      String login = example.getLogin();
      if (login != null && !"".equals(login))
      {
         predicatesList.add(builder.like(builder.lower(root.<String>get("login")), '%' + login.toLowerCase() + '%'));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }
}
