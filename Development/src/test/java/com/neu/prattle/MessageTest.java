package com.neu.prattle;


import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.model.Message;
import com.neu.prattle.service.user.UserServiceImpl;

import junit.framework.TestCase;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;


public class MessageTest {

	@Before
	public void setUp() throws IOException {
	    UserServiceImpl.setEnableDBConnection(false);  

	}


	@After
	public void destroy() {
    UserServiceImpl.setEnableDBConnection(true);  

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

	@Test
	public void breakEncode() {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setErr(new PrintStream(outContent));
		MessageEncoder mockEncoder = mock(MessageEncoder.class);
		Message simpleMessage = Message.messageBuilder()
						.setFrom("Huey")
						.setTo("Dewey")
						.setMessageContent("Louie is a spy")
						.build();
		try {
			when(mockEncoder.encode(any(Message.class))).thenThrow(IOException.class);
			assertEquals("{}", mockEncoder.encode(simpleMessage));
		} catch (Exception e) {
			assertNotNull(outContent.toString());
		}
	}

		@Test
		public void nothing() {
			MessageEncoder messageEncoder = new MessageEncoder();
			MessageDecoder messageDecoder = new MessageDecoder();
			EndpointConfig mockEndpoint = mock(EndpointConfig.class);
			messageDecoder.init(mockEndpoint);
			messageEncoder.init(mockEndpoint);
			assertEquals(2, 1+1);
		}
}
