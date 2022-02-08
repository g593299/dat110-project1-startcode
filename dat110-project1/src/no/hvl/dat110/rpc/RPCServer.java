package no.hvl.dat110.rpc;

import java.util.HashMap;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.Connection;
import no.hvl.dat110.messaging.Message;
import no.hvl.dat110.messaging.MessagingServer;

public class RPCServer {

	private MessagingServer msgserver;
	private Connection connection;
	
	// hashmap to register RPC methods which are required to extend RPCRemoteImpl	
	private HashMap<Byte,RPCRemoteImpl> services;
	
	public RPCServer(int port) {
		
		this.msgserver = new MessagingServer(port);
		this.services = new HashMap<Byte,RPCRemoteImpl>();
		
	}
	
	public void run() {
		
		// the stop RPC method is built into the server
		RPCRemoteImpl rpcstop = new RPCServerStopImpl(RPCCommon.RPIDSTOP,this);
		
		System.out.println("RPC SERVER RUN - Services: " + services.size());
			
		connection = msgserver.accept(); 
		
		System.out.println("RPC SERVER ACCEPTED");
		
		boolean stop = false;
		
		while (!stop) {
	    
		  
		   Message requestmsg,replymsg;
		   
		   // - receive Message containing RPC request
		   // - find the identifier for the RPC method to invoke
		   // - lookup the method to be invoked
		   // - invoke the method
		   // - send back message containing RPC reply
		   
		   byte rpcid = 0;
			
		    requestmsg = connection.receive();
		    rpcid = requestmsg.getData()[0];
			byte[] bytes = RPCUtils.decapsulate(requestmsg.getData());
			
			RPCRemoteImpl finn = services.get(rpcid);
			byte[] svar = finn.invoke(bytes); 
			
			
			byte[] svarmsg = RPCUtils.encapsulate(rpcid, svar);
			
			replymsg = new Message(svarmsg); 
			
			connection.send(replymsg);
		   
		  
		   
		   if (rpcid == RPCCommon.RPIDSTOP) {
			   stop = true;
		   }
		}
	
	}
	
	// used by server side implementation to register themselves in the RPC server
	public void register(byte rpcid, RPCRemoteImpl impl) {
		services.put(rpcid, impl);
	}
	
	public void stop() {
		connection.close();
		msgserver.stop();
		
	}
}
