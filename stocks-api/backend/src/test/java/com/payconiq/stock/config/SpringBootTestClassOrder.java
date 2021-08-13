package com.payconiq.stock.config;

import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;

/**
 * @author Saeid Noroozi
 * To prevent multiple load spring context, customize juint class test order
 * this feature defined in new version of junit
 * it is very helpful to order junit test scenario
 */
public class SpringBootTestClassOrder implements ClassOrderer {
    @Override
    public void orderClasses(ClassOrdererContext classOrdererContext) {
        classOrdererContext.getClassDescriptors().sort(Comparator.comparingInt(SpringBootTestClassOrder::getOrder));
    }

    /**
     *
     * @param classDescriptor to access definition of class
     * @return int value that represent order of item
     *
     * to customize ordering,define custom "TestOrder" annotation and
     * , using it to assign order value to any unit test class
     */
    private static int getOrder(ClassDescriptor classDescriptor) {
        return classDescriptor.findAnnotation(TestOrder.class)
                .map(TestOrder::value)
                .orElseGet(classDescriptor.getDisplayName()::hashCode);
    }
}
