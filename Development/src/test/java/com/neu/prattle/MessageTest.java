package com.neu.prattle;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.model.Message;

import javax.websocket.EncodeException;


public class MessageTest {
	;

	@Before
	public void setUp() throws IOException {

	}


	@After
	public void destroy() {
	}



	// This method tests encoding and decoding a message
	@Test
	public void encodeDecodeTest() throws EncodeException{
		MessageDecoder decoder = new MessageDecoder();
		MessageEncoder encoder = new MessageEncoder();
		Message simpleMessage = Message.messageBuilder()
				.setFrom("Tom")
				.setTo("Mark")
				.setMessageContent("Simple")
				.build();

		String encodedMessage = encoder.encode(simpleMessage);

		Message decodedMessage = decoder.decode(encodedMessage);

		encoder.destroy();
		decoder.destroy();
		assertEquals(simpleMessage.getContent(),decodedMessage.getContent());
		assertEquals(simpleMessage.getFrom(),decodedMessage.getFrom());
		assertEquals(simpleMessage.getTo(),decodedMessage.getTo());
		assertEquals(simpleMessage.toString(),decodedMessage.toString());

	}


	// This method if decode check works
	@Test
	public void willDecodeTest(){
		MessageDecoder decoder = new MessageDecoder();

		assertFalse(decoder.willDecode(null));
		assertTrue(decoder.willDecode("test"));

	}
	
	/**
	 * This function tests whether date setting works as expected
	 * @throws InterruptedException
	 */
	@Test
	public void dataSetTest() throws InterruptedException {
		
		Message noInitialDateSet = Message.messageBuilder()
				.setFrom("Tom")
				.setTo("Mark")
				.setMessageContent("Simple")
				.build();
		
		//This should have a data set automatically during the build function
		assertNotEquals(null,noInitialDateSet.getDateSent());

		//Current Time
		Date aDate = new Date(System.currentTimeMillis());

		//Wait some time
		TimeUnit.SECONDS.sleep(1);
		
		Message SetInitialDate = Message.messageBuilder()
				.setFrom("Tom")
				.setTo("Mark")
				.setMessageContent("Simple")
				.setDateSent(aDate)
				.build();
		
		
		//The time should match
		assertEquals(aDate,SetInitialDate.getDateSent());

		
	}



}
