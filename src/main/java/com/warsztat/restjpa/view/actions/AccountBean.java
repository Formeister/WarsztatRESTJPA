package com.warsztat.restjpa.view.actions;

import com.warsztat.restjpa.model.User;
import com.warsztat.restjpa.service.UserService;
import com.warsztat.restjpa.util.Loggable;
import com.warsztat.restjpa.view.AbstractBean;
import com.warsztat.restjpa.view.CatchException;
import com.warsztat.restjpa.view.LoggedIn;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.login.LoginException;
import java.io.Serializable;


@Named
@SessionScoped
@Loggable
@CatchException
public class AccountBean extends AbstractBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

	private static final long serialVersionUID = 1L;

	@Inject
    private UserService userService;

    @Inject
    private CredentialsBean credentials;

    @Inject
    private Conversation conversation;

    @Produces
    @LoggedIn
    private User loggedinUser;

    // ======================================
    // =              Public Methods        =
    // ======================================

    public String doLogin() throws LoginException {
        if (credentials.getLogin() == null || "".equals(credentials.getLogin())) {
            addWarningMessage("id_filled");
            return null;
        }
        if (credentials.getPassword() == null || "".equals(credentials.getPassword())) {
            addWarningMessage("pwd_filled");
            return null;
        }

        loggedinUser = userService.findUser(credentials.getLogin());
        return "main.faces";
    }

    public String doCreateNewAccount() {

        // Login has to be unique
        if (userService.doesLoginAlreadyExist(credentials.getLogin())) {
            addWarningMessage("login_exists");
            return null;
        }

        // Id and password must be filled
        if ("".equals(credentials.getLogin()) || "".equals(credentials.getPassword()) || "".equals(credentials.getPassword2())) {
            addWarningMessage("id_pwd_filled");
            return null;
        } else if (!credentials.getPassword().equals(credentials.getPassword2())) {
            addWarningMessage("both_pwd_same");
            return null;
        }

        // Login and password are ok
        loggedinUser = new User();
        loggedinUser.setLogin(credentials.getLogin());
        loggedinUser.setPassword(loggedinUser.digestPassword(credentials.getPassword()));

        return "createaccount.faces";
    }

    public String doCreateUser() {
        loggedinUser = userService.createUser(loggedinUser);
        return "main.faces";
    }

    public String doLogout() {
        loggedinUser = null;
        // Stop conversation
        if (!conversation.isTransient()) {
            conversation.end();
        }
        addInformationMessage("been_loggedout");
        return "main.faces";
    }

    public String doUpdateAccount() {
        loggedinUser = userService.updateUser(loggedinUser);
        addInformationMessage("account_updated");
        return "showaccount.faces";
    }

    public boolean isLoggedIn() {
        return loggedinUser != null;
    }

    public User getLoggedinUser() {
        return loggedinUser;
    }

    public void setLoggedinUser(User loggedinUser) {
        this.loggedinUser = loggedinUser;
    }
}
