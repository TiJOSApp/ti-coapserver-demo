package com.tijos.coapserver;

import com.tijos.coapserver.server.TiCoAPServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Application entry point.
 */
@Component
public class CoAPServerStarter implements CommandLineRunner {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(CoAPServerStarter.class.getName());


    public  void run(String... args) {

        LOGGER.log(Level.INFO, "TiCoAP Server is starting...");

        TiCoAPServer.getInstance().startUp();
    }
}
