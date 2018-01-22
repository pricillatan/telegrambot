package com.pricillatan.telegrambot.bots;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.jersey.client.ClientConfig;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


public class EchoBot extends TelegramLongPollingBot {
	private static WebTarget telegramService; 
	private static String telegramServer = "https://api.telegram.org/bot";
	
	private String botUsername = "", botToken = "" , botChannel="";
	
	public EchoBot() {
	
        Properties prop = new Properties();
        try {
			prop.load(new FileInputStream("app.properties"));
			
			
			this.botUsername=prop.getProperty("botUsername");
			this.botToken=prop.getProperty("botToken");
			this.botChannel=prop.getProperty("botChannel");
        }
        catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ClientConfig clientConfig = new ClientConfig();
		Client client = ClientBuilder.newClient(clientConfig);
		telegramService = client.target(telegramServer);
	}
	
    public void onUpdateReceived(Update update) {
    	
    	// We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            
            String[] parts = message_text.trim().split(" ");
            String command = parts[0];
            
            SendMessage message=null;
            switch(command) {
            case "/start":{
            	String instructions = "Hello there! Welcome to an echo bot!\n" 
            			+ "This is an echo bot. It repeats everything you typed! \n"
            			+ " For some fun: \n Please enter one of the following commands: \n"
    					+ "*/check_status* to check on Google's status. This will also send a reponse to the channel.\n";
            	
            	message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(instructions);

            	break;
            }
	            case "/help":{
	            	String instructions = "Hi! This is an echo bot. It repeats everything you typed! \n"
	            			+ " For some fun: \n Please enter one of the following commands: \n"
	    					+ "*/check_status* to check on Google's status. This will also send a reponse to the channel.\n";
	            	
	            	message = new SendMessage() // Create a message object object
	                        .setChatId(chat_id)
	                        .setText(instructions);

	            	break;
	            }
	            case "/check_status":{
	            	
	            	try {
	            		String ipAddress="www.google.com";
						InetAddress inet = InetAddress.getByName(ipAddress);
						System.out.println("Sending Ping Request to " + ipAddress);
						
						String result = inet.isReachable(5000) ? "Google Host is reachable" : "Google Host is NOT reachable";
						message = new SendMessage() // Create a message object object
		                        .setChatId(chat_id)
		                        .setText(result);
						executeMessage(message);
						message = new SendMessage() // Create a message object object
		                        .setChatId(botChannel)
		                        .setText(result);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	 
	            	break;
	            }
	            default:{
	            	message = new SendMessage() // Create a message object object
	                        .setChatId(chat_id)
	                        .setText(message_text);
	            	break;
	            }
            }//end of switch

			executeMessage(message);
        }
    }
    public void executeMessage(SendMessage message) {

        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public String getBotUsername() {
    	return this.botUsername;
    }

    @Override
    public String getBotToken() {
    	return this.botToken;
    }
}