package com.neu.prattle;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import com.neu.prattle.websocket.ChatEndpoint;
import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.main.PrattleApplication;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;

import javax.websocket.EncodeException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import javax.ws.rs.core.Response;
import javax.websocket.RemoteEndpoint;


@RunWith(MockitoJUnitRunner.class)
public class MessageProcessorTest {

	private UserService as;
	private ChatEndpoint chat;

	@Before
	public void setUp() throws IOException {
		as = UserServiceImpl.getInstance();
		chat = new ChatEndpoint();
	}


	@After
	public void destroy() {
		UserServiceImpl.clear();
		chat = null;
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
