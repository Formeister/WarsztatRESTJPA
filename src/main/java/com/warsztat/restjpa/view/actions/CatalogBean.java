package com.warsztat.restjpa.view.actions;

import com.warsztat.restjpa.model.Item;
import com.warsztat.restjpa.model.Action;
import com.warsztat.restjpa.service.CatalogService;
import com.warsztat.restjpa.util.Loggable;
import com.warsztat.restjpa.view.AbstractBean;
import com.warsztat.restjpa.view.CatchException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named
@SessionScoped
@Loggable
@CatchException
public class CatalogBean extends AbstractBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

	private static final long serialVersionUID = 1L;

	@Inject
    private CatalogService catalogService;

    private String categoryName;
    private Long actionId;
    private Long itemId;

    private String keyword;
    private Action action;
    private Item item;
    private List<Action> actions;
    private List<Item> items;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public String doFindActions() {
        actions = catalogService.findActions(categoryName);
        return "showactions.faces";
    }

    public String doFindItems() {
        action = catalogService.findAction(actionId);
        items = catalogService.findItems(actionId);
        return "showitems.faces";
    }

    public String doFindItem() {
        item = catalogService.findItem(itemId);
        return "showitem.faces";
    }

    public String doSearch() {
        items = catalogService.searchItems(keyword);
        return "searchresult.faces&faces-redirect=true";
    }

    // ======================================
    // =         Getters & setters          =
    // ======================================

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Action> getActions() {
        return actions;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }
}