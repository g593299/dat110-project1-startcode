package no.hvl.dat110.rpc;

import no.hvl.dat110.TODO;
import no.hvl.dat110.messaging.*;

public class RPCClient {

	private MessagingClient msgclient;
	private Connection connection;
	
	public RPCClient(String server, int port) {
	
		msgclient = new MessagingClient(server,port);
	}
	
	public void connect() {
		
		if (connection == null) {
			try {
				connection = msgclient.connect();
			} catch (Exception e) {
				System.out.println("An error occurred: " + e);
			}
		}
	}
	
	public void disconnect() {
		
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
			System.out.println("An error occurred: " + e);
		}
	}
	
	public byte[] call(byte rpcid, byte[] params) {
		
		
		
		// TODO - START 
		
		/* 
		 * 
		Make a remote call on the RPC server by sending an RPC request message
		and receive an RPC reply message
		
		params is the marshalled parameters from the client-stub
				
		The rpcid, params, and return value must be encapsulated/decapsulated
		according to the RPC message format
			
		*/
		
		if(connection == null) {
			connect();
		}
		
		byte[] bytes = RPCUtils.encapsulate(rpcid, params);
		
		Message reqMes = new Message(bytes);
		connection.send(reqMes);
		
		Message recivedMsg = connection.receive();
		
		byte[] payload = RPCUtils.decapsulate(recivedMsg.getData());
		
		
		return payload;
		
	}

}
