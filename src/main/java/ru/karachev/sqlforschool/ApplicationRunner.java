package ru.karachev.sqlforschool;

import ru.karachev.sqlforschool.context.ContextInjector;
import ru.karachev.sqlforschool.controller.Controller;

public class ApplicationRunner {
    
    public static void main(String[] args) {
        
        ContextInjector contextInjector = ContextInjector.getInstance();
        Controller controller = contextInjector.getController();
        controller.load();
        controller.run();
    }
}
