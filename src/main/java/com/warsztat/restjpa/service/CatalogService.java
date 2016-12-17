package com.warsztat.restjpa.service;

import com.warsztat.restjpa.model.Category;
import com.warsztat.restjpa.model.Item;
import com.warsztat.restjpa.model.Action;
import com.warsztat.restjpa.util.Loggable;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Stateless
@Loggable
public class CatalogService implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

	private static final long serialVersionUID = 1L;
	@Inject
    private EntityManager em;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public Category findCategory(@NotNull Long categoryId) {
        return em.find(Category.class, categoryId);
    }

    public Category findCategory(@NotNull String categoryName) {
        TypedQuery<Category> typedQuery = em.createNamedQuery(Category.FIND_BY_NAME, Category.class);
        typedQuery.setParameter("pname", categoryName);
        return typedQuery.getSingleResult();
    }

    public List<Category> findAllCategories() {
        TypedQuery<Category> typedQuery = em.createNamedQuery(Category.FIND_ALL, Category.class);
        return typedQuery.getResultList();
    }

    public Category createCategory(@NotNull Category category) {
        em.persist(category);
        return category;
    }

    public Category updateCategory(@NotNull Category category) {
        return em.merge(category);
    }

    public void removeCategory(@NotNull Category category) {
        em.remove(em.merge(category));
    }

    public void removeCategory(@NotNull Long categoryId) {
        removeCategory(findCategory(categoryId));
    }

    public List<Action> findActions(@NotNull String categoryName) {
        TypedQuery<Action> typedQuery = em.createNamedQuery(Action.FIND_BY_CATEGORY_NAME, Action.class);
        typedQuery.setParameter("pname", categoryName);
        return typedQuery.getResultList();
    }

    public Action findAction(@NotNull Long actionId) {
        Action action = em.find(Action.class, actionId);
        return action;
    }

    public List<Action> findAllActions() {
        TypedQuery<Action> typedQuery = em.createNamedQuery(Action.FIND_ALL, Action.class);
        return typedQuery.getResultList();
    }

    public Action createAction(@NotNull Action action) {
        if (action.getCategory() != null && action.getCategory().getId() == null)
            em.persist(action.getCategory());

        em.persist(action);
        return action;
    }

    public Action updateAction(@NotNull Action action) {
        return em.merge(action);
    }

    public void removeAction(@NotNull Action action) {
        em.remove(em.merge(action));
    }

    public void removeAction(@NotNull Long actionId) {
        removeAction(findAction(actionId));
    }

    public List<Item> findItems(@NotNull Long actionId) {
        TypedQuery<Item> typedQuery = em.createNamedQuery(Item.FIND_BY_ACTION_ID, Item.class);
        typedQuery.setParameter("actionId", actionId);
        return typedQuery.getResultList();
    }

    public Item findItem(@NotNull Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> searchItems(String keyword) {
        if (keyword == null)
            keyword = "";

        TypedQuery<Item> typedQuery = em.createNamedQuery(Item.SEARCH, Item.class);
        typedQuery.setParameter("keyword", "%" + keyword.toUpperCase() + "%");
        return typedQuery.getResultList();
    }

    public List<Item> findAllItems() {
        TypedQuery<Item> typedQuery = em.createNamedQuery(Item.FIND_ALL, Item.class);
        return typedQuery.getResultList();
    }

    public Item createItem(@NotNull Item item) {
        if (item.getAction() != null && item.getAction().getId() == null) {
            em.persist(item.getAction());
            if (item.getAction().getCategory() != null && item.getAction().getCategory().getId() == null)
                em.persist(item.getAction().getCategory());
        }

        em.persist(item);
        return item;
    }

    public Item updateItem(@NotNull Item item) {
        return em.merge(item);
    }

    public void removeItem(@NotNull Item item) {
        em.remove(em.merge(item));
    }

    public void removeItem(@NotNull Long itemId) {
        removeItem(findItem(itemId));
    }
}
