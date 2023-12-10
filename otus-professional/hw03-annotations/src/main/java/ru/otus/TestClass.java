package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestClass {
    @Before
    private void firstBeforeMethod() {
        System.out.println("Run first before method");
        //throw new RuntimeException();
    }

    @Before
    public void secondBeforeMethod() {
        System.out.println("Run second before method");
    }

    @Test
    private void firstTestMethod() {
        System.out.println("Run first test method");
        //throw new RuntimeException();
    }

    @Test
    public void secondTestMethod() {
        System.out.println("Run second test method");
        //throw new RuntimeException();
    }

    @After
    private void firstAfterMethod() {
        System.out.println("Run first after method");
        //throw new RuntimeException();
    }

    @After
    public void secondAfterMethod() {
        System.out.println("Run second after method");
    }
}
