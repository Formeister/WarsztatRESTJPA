package com.warsztat.restjpa.view;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

import com.warsztat.restjpa.util.Loggable;

import java.util.ArrayList;
import java.util.List;


@Named
@RequestScoped
@Loggable
@CatchException
public class DebugBean extends AbstractBean {

    // ======================================
    // =              Public Methods        =
    // ======================================

    public List<String> getThreadStack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        List<String> elements = new ArrayList<>();
        for (StackTraceElement stackTraceElement : stackTraceElements) {
            elements.add(stackTraceElement.getClassName() + "." +
                    stackTraceElement.getMethodName() +
                    "(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ")");
        }
        return elements;
    }

    public String getWorkingDirectory() {
        return new java.io.File(".").getAbsolutePath();
    }

    public String getTotalMemory() {
        return String.valueOf(Runtime.getRuntime().totalMemory());
    }

    public String getFreeMemory() {
        return String.valueOf(Runtime.getRuntime().freeMemory());
    }
}