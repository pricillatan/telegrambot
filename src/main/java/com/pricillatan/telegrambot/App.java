package com.pricillatan.telegrambot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.pricillatan.telegrambot.bots.EchoBot;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ApiContextInitializer.init();
        
        try {
			
			TelegramBotsApi botsApi = new TelegramBotsApi();
			 
			botsApi.registerBot(new EchoBot());
			 
			 
		}catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
