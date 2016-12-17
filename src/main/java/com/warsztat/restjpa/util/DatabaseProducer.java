package com.warsztat.restjpa.util;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class DatabaseProducer 
{

    // ======================================
    // =             Attributes             =
    // ======================================

    @Produces
    @PersistenceContext(unitName = "WarsztatSamochodowyPU")
    private EntityManager em;
}
