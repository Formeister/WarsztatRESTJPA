package com.warsztat.restjpa.util;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.warsztat.restjpa.util.Discount;
import com.warsztat.restjpa.util.Vat;

public class NumberProducer
{
   @Produces
   @Vat
   @Named
   private Float vatRate;
   @Produces
   @Discount
   @Named
   private Float discountRate;
}