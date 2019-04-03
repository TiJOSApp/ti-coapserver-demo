package com.tijos.coapserver.resource;

import com.tijos.coapserver.service.DeviceCommandCacheService;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the command resource "cmd" , only accept GET request from device
 */
public class TiCoAPCommandResourceController extends CoapResource{

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(TiCoAPCommandResourceController.class.getName());

  //  String content ="{\"valve\":0, \"interval\":14400,\"clean\",90}";

    public TiCoAPCommandResourceController(){
        // set resource identifier
        super("cmd");

        // set display name
        getAttributes().setTitle("device-cmd Resource");

    }

    @Override
    public void handleGET(CoapExchange exchange) {

        LOGGER.log(Level.INFO, "handleGET");
        OptionSet options =  exchange.getRequestOptions();

        List<String> uriPath = options.getUriPath();
        if(uriPath.size() < 3) {
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
            return ;
        }

        try {
            String productKey = uriPath.get(1);
            String deviceKey = uriPath.get(2);

            String devPath = "/" + productKey + "/" + deviceKey;

            //Fetch a cached command
            String command = DeviceCommandCacheService.getInstance().pollDeviceCommand(devPath);

            LOGGER.log(Level.INFO, "devicePath " + devPath + " response : " + command);

            Response response = new Response(CoAP.ResponseCode.CONTENT);
            response.setPayload(command);

            exchange.respond(response);
        }
        catch (Exception ex) {
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
            LOGGER.log(Level.SEVERE, ex.toString());

        }
    }

}

