package com.warsztat.restjpa.model;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.warsztat.restjpa.model.Address;

import javax.inject.Inject;
import javax.validation.Validator;

import static org.junit.Assert.assertEquals;


@RunWith(Arquillian.class)
public class AddressIT {

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
                .addClass(Address.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // =              Methods               =
    // ======================================

    @Test
    public void shouldCreateAValidAddress() {

        // Creates an object
        Address address = new Address("Street", "City", "Zipcode");

        // Checks the object is valid
        assertEquals("Should have not constraint violation", 0, validator.validate(address).size());
    }
}