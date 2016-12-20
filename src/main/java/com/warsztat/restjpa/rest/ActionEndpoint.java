package com.warsztat.restjpa.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.warsztat.restjpa.model.Action;
import com.warsztat.restjpa.util.Loggable;


@Stateless
@Path("/actions")
@Loggable
public class ActionEndpoint
{

   // ======================================
   // =             Attributes             =
   // ======================================

   @PersistenceContext(unitName = "WarsztatSamochodowyPU")
   private EntityManager em;

   // ======================================
   // =          Business methods          =
   // ======================================

   @POST
   @Consumes( {"application/xml", "application/json"})
   public Response create(Action entity)
   {
      em.persist(entity);
      return Response.created(UriBuilder.fromResource(ActionEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
   }

   @DELETE
   @Path("/{id:[0-9][0-9]*}")
   public Response deleteById(@PathParam("id") Long id)
   {
      Action entity = em.find(Action.class, id);
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      em.remove(entity);
      return Response.noContent().build();
   }

   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces( {"application/xml", "application/json"})
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<Action> findByIdQuery = em.createQuery("SELECT DISTINCT p FROM Action p LEFT JOIN FETCH p.category WHERE p.id = :entityId ORDER BY p.id", Action.class);
      findByIdQuery.setParameter("entityId", id);
      Action entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      return Response.ok(entity).build();
   }

   @GET
   @Produces( {"application/xml", "application/json"})
   public List<Action> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<Action> findAllQuery = em.createQuery("SELECT DISTINCT p FROM Action p LEFT JOIN FETCH p.category ORDER BY p.id", Action.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<Action> results = findAllQuery.getResultList();
      return results;
   }

   @PUT
   @Path("/{id:[0-9][0-9]*}")
   @Consumes( {"application/xml", "application/json"})
   public Response update(Action entity)
   {
      try
      {
         entity = em.merge(entity);
      }
      catch (OptimisticLockException e)
      {
         return Response.status(Response.Status.CONFLICT).entity(e.getEntity()).build();
      }

      return Response.noContent().build();
   }
}
