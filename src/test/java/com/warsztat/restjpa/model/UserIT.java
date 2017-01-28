package com.warsztat.restjpa.model;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.warsztat.restjpa.model.Address;
import com.warsztat.restjpa.model.Category;
import com.warsztat.restjpa.model.User;

import javax.inject.Inject;
import javax.validation.Validator;

import static org.junit.Assert.assertEquals;


@RunWith(Arquillian.class)
public class UserIT {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Inject
    private Validator validator;

    // ======================================
    // =          Lifecycle Methods         =
    // ======================================

    @Deployment
    public static JavaArchive jar() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Category.class, Address.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // =              Methods               =
    // ======================================

    @Test
    public void shouldCreateAValidUser() {

        // Creates an object
        Address address = new Address("78 Gnu Rd", "Texas", "666");
        User user = new User("Paul", "Mc Cartney", "pmac", "pmac", "paul@beales.com", address);

        // Checks the object is valid
        assertEquals("Should have not constraint violation", 0, validator.validate(user).size());
    }
}