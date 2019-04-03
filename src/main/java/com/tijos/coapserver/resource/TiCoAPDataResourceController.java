package com.tijos.coapserver.resource;

import com.alibaba.fastjson.JSONObject;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Definition of the data Resource, only accept POST command
 */
public class TiCoAPDataResourceController extends CoapResource {

    /** The logger. */
    private static final Logger LOGGER = Logger.getLogger(TiCoAPDataResourceController.class.getName());


    public TiCoAPDataResourceController() {

        // set resource identifier
        super("data");

        // set display name
        getAttributes().setTitle("date-update Resource");

    }

    @Override
    public void handlePOST(CoapExchange exchange){

        OptionSet options =  exchange.getRequestOptions();

        List<String> uriPath = options.getUriPath();
        if(uriPath.size() < 3) {
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
            return ;
        }

        try {
            String productKey = uriPath.get(1);
            String deviceKey = uriPath.get(2);
            String payload = new String(exchange.getRequestPayload());

            LOGGER.log(Level.INFO, "handlePOST payload " + payload);

            //JSON Data from the remote
            JSONObject jsonObject = JSONObject.parseObject(payload);

            exchange.respond(CoAP.ResponseCode.CHANGED);
        }
        catch (Exception ex) {
            exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
            LOGGER.log(Level.SEVERE, ex.toString());
        }

    }

}