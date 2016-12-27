package com.warsztat.restjpa.view.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.warsztat.restjpa.model.Category;
import com.warsztat.restjpa.model.Action;
import com.warsztat.restjpa.util.Loggable;


@Named
@Stateful
@ConversationScoped
@Loggable
public class ActionBean implements Serializable
{

   private static final long serialVersionUID = 1L;

   /*
    * Support creating and retrieving Action entities
    */

   private Long id;

   public Long getId()
   {
      return this.id;
   }

   public void setId(Long id)
   {
      this.id = id;
   }

   private Action action;

   public Action getAction()
   {
      return this.action;
   }

   public void setAction(Action action)
   {
      this.action = action;
   }

   @Inject
   private Conversation conversation;

   @PersistenceContext(unitName = "WarsztatSamochodowyPU", type = PersistenceContextType.EXTENDED)
   private EntityManager entityManager;

   public String create()
   {

      this.conversation.begin();
      this.conversation.setTimeout(1800000L);
      return "create?faces-redirect=true";
   }

   public void retrieve()
   {

      if (FacesContext.getCurrentInstance().isPostback())
      {
         return;
      }

      if (this.conversation.isTransient())
      {
         this.conversation.begin();
         this.conversation.setTimeout(1800000L);
      }

      if (this.id == null)
      {
         this.action = this.example;
      }
      else
      {
         this.action = findById(getId());
      }
   }

   public Action findById(Long id)
   {

      return this.entityManager.find(Action.class, id);
   }

   /*
    * Support updating and deleting Action entities
    */

   public String update()
   {
      this.conversation.end();

      try
      {
         if (this.id == null)
         {
            this.entityManager.persist(this.action);
            return "search?faces-redirect=true";
         }
         else
         {
            this.entityManager.merge(this.action);
            return "view?faces-redirect=true&id=" + this.action.getId();
         }
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   public String delete()
   {
      this.conversation.end();

      try
      {
         Action deletableEntity = findById(getId());

         this.entityManager.remove(deletableEntity);
         this.entityManager.flush();
         return "search?faces-redirect=true";
      }
      catch (Exception e)
      {
         FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
         return null;
      }
   }

   /*
    * Support searching Action entities with pagination
    */

   private int page;
   private long count;
   private List<Action> pageItems;

   private Action example = new Action();

   public int getPage()
   {
      return this.page;
   }

   public void setPage(int page)
   {
      this.page = page;
   }

   public int getPageSize()
   {
      return 10;
   }

   public Action getExample()
   {
      return this.example;
   }

   public void setExample(Action example)
   {
      this.example = example;
   }

   public String search()
   {
      this.page = 0;
      return null;
   }

   public void paginate()
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

      // Populate this.count

      CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
      Root<Action> root = countCriteria.from(Action.class);
      countCriteria = countCriteria.select(builder.count(root)).where(
            getSearchPredicates(root));
      this.count = this.entityManager.createQuery(countCriteria)
            .getSingleResult();

      // Populate this.pageItems

      CriteriaQuery<Action> criteria = builder.createQuery(Action.class);
      root = criteria.from(Action.class);
      TypedQuery<Action> query = this.entityManager.createQuery(criteria
            .select(root).where(getSearchPredicates(root)));
      query.setFirstResult(this.page * getPageSize()).setMaxResults(
            getPageSize());
      this.pageItems = query.getResultList();
   }

   private Predicate[] getSearchPredicates(Root<Action> root)
   {

      CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
      List<Predicate> predicatesList = new ArrayList<Predicate>();

      String name = this.example.getName();
      if (name != null && !"".equals(name))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("name")), '%' + name.toLowerCase() + '%'));
      }
      String description = this.example.getDescription();
      if (description != null && !"".equals(description))
      {
         predicatesList.add(builder.like(builder.lower(root.<String> get("description")), '%' + description.toLowerCase() + '%'));
      }
      Category category = this.example.getCategory();
      if (category != null)
      {
         predicatesList.add(builder.equal(root.get("category"), category));
      }

      return predicatesList.toArray(new Predicate[predicatesList.size()]);
   }

   public List<Action> getPageItems()
   {
      return this.pageItems;
   }

   public long getCount()
   {
      return this.count;
   }

   /*
    * Support listing and POSTing back Action entities (e.g. from inside an
    * HtmlSelectOneMenu)
    */

   public List<Action> getAll()
   {

      CriteriaQuery<Action> criteria = this.entityManager
            .getCriteriaBuilder().createQuery(Action.class);
      return this.entityManager.createQuery(
            criteria.select(criteria.from(Action.class))).getResultList();
   }

   @Resource
   private SessionContext sessionContext;

   public Converter getConverter()
   {

      final ActionBean ejbProxy = this.sessionContext.getBusinessObject(ActionBean.class);

      return new Converter()
      {

         @Override
         public Object getAsObject(FacesContext context,
               UIComponent component, String value)
         {

            return ejbProxy.findById(Long.valueOf(value));
         }

         @Override
         public String getAsString(FacesContext context,
               UIComponent component, Object value)
         {

            if (value == null)
            {
               return "";
            }

            return String.valueOf(((Action) value).getId());
         }
      };
   }

   /*
    * Support adding children to bidirectional, one-to-many tables
    */

   private Action add = new Action();

   public Action getAdd()
   {
      return this.add;
   }

   public Action getAdded()
   {
      Action added = this.add;
      this.add = new Action();
      return added;
   }
}
