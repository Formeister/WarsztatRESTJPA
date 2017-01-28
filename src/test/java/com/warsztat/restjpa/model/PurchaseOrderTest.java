package com.warsztat.restjpa.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import com.warsztat.restjpa.model.PurchaseOrder;


public class PurchaseOrderTest {

    // ======================================
    // =              Methods               =
    // ======================================

    @Test
    public void shouldCheckEqualsAndHashCode() {

        // Checks equals and hashCode is valid
        EqualsVerifier.forClass(PurchaseOrder.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}