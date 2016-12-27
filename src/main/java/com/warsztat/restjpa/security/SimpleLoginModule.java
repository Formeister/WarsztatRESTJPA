package com.warsztat.restjpa.security;

import com.warsztat.restjpa.model.User;
import com.warsztat.restjpa.service.UserService;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.util.Map;


public class SimpleLoginModule implements LoginModule
{

   // ======================================
   // =             Attributes             =
   // ======================================

   private CallbackHandler callbackHandler;

   private UserService userService;

   private BeanManager beanManager;

   // ======================================
   // =          Business methods          =
   // ======================================

   private UserService getUserService()
   {
      if (userService != null)
      {
         return userService;
      }
      try
      {
         Context context = new InitialContext();
         beanManager = (BeanManager) context.lookup("java:comp/BeanManager");
         Bean<?> bean = beanManager.getBeans(UserService.class).iterator().next();
         CreationalContext<?> cc = beanManager.createCreationalContext(bean);
         userService = (UserService) beanManager.getReference(bean, UserService.class, cc);

      }
      catch (NamingException e)
      {
         e.printStackTrace();
         throw new RuntimeException(e);
      }

      return userService;

   }

   @Override
   public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> stringMap, Map<String, ?> stringMap1)
   {
      this.callbackHandler = callbackHandler;
      getUserService();
   }

   @Override
   public boolean login() throws LoginException
   {

      NameCallback nameCallback = new NameCallback("Name : ");
      PasswordCallback passwordCallback = new PasswordCallback("Password : ", false);
      try
      {
         callbackHandler.handle(new Callback[]{nameCallback, passwordCallback});
         String username = nameCallback.getName();
         String password = new String(passwordCallback.getPassword());
         nameCallback.setName("");
         passwordCallback.clearPassword();
         User user = userService.findUser(username, password);

         if (user == null)
         {
            throw new LoginException("Authentication failed");
         }

         return true;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new LoginException(e.getMessage());
      }
   }

   @Override
   public boolean commit() throws LoginException
   {
      return true;
   }

   @Override
   public boolean abort() throws LoginException
   {
      return true;
   }

   @Override
   public boolean logout() throws LoginException
   {
      return true;
   }
}
