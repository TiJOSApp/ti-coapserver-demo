package com.tijos.coapserver.server;

import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.network.Exchange;
import org.eclipse.californium.core.server.ServerMessageDeliverer;
import org.eclipse.californium.core.server.resources.Resource;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Custom message deliverer which parse the device path and real resource name.
 */
public class TiServerMessageDeliverer extends ServerMessageDeliverer {

    private static final Logger LOGGER = Logger.getLogger(ServerMessageDeliverer.class.getCanonicalName());

    /**
     * Constructs a default message deliverer that delivers requests to the
     * resources rooted at the specified root.
     *
     * @param root the root resource
     */
    public TiServerMessageDeliverer(Resource root) {
        super(root);
    }


    /**
     * Invoked by the <em>deliverRequest</em> before the request gets processed.
     * <p>
     * Subclasses may override this method in order to replace the default request handling logic
     * or to modify or add headers etc before the request gets processed.
     * <p>
     * This default implementation returns {@code false}.
     *
     * @param exchange The exchange for the incoming request.
     * @return {@code true} if the request has already been processed by this method and thus
     *         should not be delivered to a matching resource anymore.
     */
    protected boolean preDeliverRequest(final Exchange exchange) {
        if (exchange == null) {
            throw new NullPointerException("exchange must not be null");
        }
        Request request = exchange.getRequest();

        List<String> path = request.getOptions().getUriPath();

        if(path.size() < 3){
            return false;
        }

        if(!path.get(0).equals("topic")) {
            return false;
        }

        List<String> subPath = path.subList(3, path.size());

        LOGGER.log(Level.INFO, "subpath " + subPath.get(0));

        final Resource resource = findResource(subPath);

        if (resource != null) {
            checkForObserveOption(exchange, resource);

            // Get the executor and let it process the request
            Executor executor = resource.getExecutor();
            if (executor != null) {
                executor.execute(() -> resource.handleRequest(exchange));
            } else {
                resource.handleRequest(exchange);
            }
        } else {
            LOGGER.info ("did not find resource " + path);
            exchange.sendResponse(new Response(CoAP.ResponseCode.NOT_FOUND));
        }

        return true;
    }

}
