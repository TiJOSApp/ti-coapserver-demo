
package com.tijos.coapserver.server;

import com.tijos.coapserver.resource.TiCoAPCommandResourceController;
import com.tijos.coapserver.resource.TiCoAPDataResourceController;
import com.tijos.coapserver.service.DeviceCommandCacheService;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * TiCoAP Server
 */
public class TiCoAPServer extends CoapServer {

	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

    private static TiCoAPServer server = null;
    private TiCoAPDataResourceController dataResource = new TiCoAPDataResourceController();
    private TiCoAPCommandResourceController commandResource = new  TiCoAPCommandResourceController();

	public static  TiCoAPServer getInstance() {
        if(server == null) {
            server = new TiCoAPServer();
        }

        return server;
    }

    /*
     * Application entry point.
     */
    public void startUp() {
        setMessageDeliverer(new TiServerMessageDeliverer(getRoot()));
        // add endpoints on all IP addresses
        addEndpoints();
        start();
    }

    /**
     * if new command arrives.
     * @param devicePath device path
     * @param command  command in json
     */
    public void sendCommand(String devicePath, String command){

        //add to command cache
        DeviceCommandCacheService.getInstance().addDeviceCommand(devicePath,command);

    }

    /**
     * Add individual endpoints listening on default CoAP port on all IPv4 addresses of all network interfaces.
     */
    private void addEndpoints() {

    	for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
    		// only binds to IPv4 addresses and localhost
			if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
    }

    /*
     * Constructor for a new Hello-World server. Here, the resources
     * of the server are initialized.
     */
    public TiCoAPServer() {

        add(dataResource);
        add(commandResource);
    }


}
