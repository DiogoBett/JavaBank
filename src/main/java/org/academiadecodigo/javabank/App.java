package org.academiadecodigo.javabank;

import org.academiadecodigo.javabank.controller.LoginController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityManagerFactory;

public class App {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");

        LoginController loginController = context.getBean("logincontroller", LoginController.class);

        loginController.init();

    }

}
