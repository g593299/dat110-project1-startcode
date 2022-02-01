package no.hvl.dat110.messaging;

import java.util.Arrays;

import no.hvl.dat110.TODO;

public class MessageUtils {

	public static final int SEGMENTSIZE = 128;

	public static final int MESSAGINGPORT = 8080;
	public static final String MESSAGINGHOST = "localhost";
	
	public static byte[] encapsulate(Message message) {
		
		byte[] segment = null;
		byte[] data = message.getData();
		
		
		segment = new byte[MessageConfig.SEGMENTSIZE];
		
		Integer datalengde = data.length;
		segment[0] = datalengde.byteValue();
		
		for(int i = 0; i < data.length; i++) {
			
			segment[i + 1] = data[i];
			
		}
		
		
		return segment;
		
	}

	public static Message decapsulate(byte[] segment) {

		Message message = null;
		
		byte[] mottatt = new byte[segment[0]];
		
		
		for(int i = 0; i<segment[0]; i++) {
			
			mottatt[i] = segment[i+1];
			
		}
		
		message = new Message(mottatt);
		
		return message;
		
	}
	
}
