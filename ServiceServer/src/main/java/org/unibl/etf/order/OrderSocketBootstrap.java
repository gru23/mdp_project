package org.unibl.etf.order;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class OrderSocketBootstrap implements ServletContextListener {
	private static final Logger LOGGER = Logger.getLogger(OrderSocketBootstrap.class.getName());

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
                    LOGGER.log(Level.SEVERE, "", e);
                }
            });
            serverThread.start();

            // dostupno REST-u
            sce.getServletContext().setAttribute("orderServer", orderServer);
            LOGGER.info("Order socket server stopped");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    	LOGGER.info("Turning down order socket server");
    }
}
