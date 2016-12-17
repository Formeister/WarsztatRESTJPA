package com.warsztat.restjpa.util;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;
import java.util.logging.Logger;


@Loggable
@Interceptor
public class LoggingInterceptor implements Serializable 
{

    // ======================================
    // =             Attributes             =
    // ======================================

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
    private transient Logger logger;

    // ======================================
    // =          Business methods          =
    // ======================================

    @AroundInvoke
    private Object intercept(InvocationContext ic) throws Exception
    {
        logger.entering(ic.getTarget().getClass().getName(), ic.getMethod().getName());
        logger.info(">>> " + ic.getTarget().getClass().getName() + "-" + ic.getMethod().getName());
        try 
        {
            return ic.proceed();
        } 
        finally 
        {
            logger.exiting(ic.getTarget().getClass().getName(), ic.getMethod().getName());
            logger.info("<<< " + ic.getTarget().getClass().getName() + "-" + ic.getMethod().getName());
        }
    }
}
