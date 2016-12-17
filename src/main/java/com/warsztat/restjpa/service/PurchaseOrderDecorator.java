package com.warsztat.restjpa.service;

import javax.decorator.Decorator;
import javax.inject.Inject;

import com.warsztat.restjpa.service.ComputablePurchaseOrder;

import javax.decorator.Delegate;

@Decorator
public abstract class PurchaseOrderDecorator implements ComputablePurchaseOrder
{

   @Inject
   @Delegate
   private ComputablePurchaseOrder delegate;
}