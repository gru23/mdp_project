package org.unibl.etf.order;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class OrderSocketBootstrap implements ServletContextListener {

    private OrderServer orderServer;
    private Thread serverThread;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            orderServer = new OrderServer();
            serverThread = new Thread(() -> {
                try {
                    orderServer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            serverThread.start();

            // dostupno REST-u
            sce.getServletContext().setAttribute("orderServer", orderServer);

            System.out.println("Order socket server pokrenut.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Gašenje socket servera...");
        // opciono: graceful shutdown
    }
}
