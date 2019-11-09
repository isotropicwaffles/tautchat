package com.neu.prattle;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

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



}
