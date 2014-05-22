/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bro.Connector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arshed
 */
public class MessageReciever implements Runnable{

    public static Map<String,StaffConnector> staff = new HashMap<String,StaffConnector>();
    private static final String READ_URL = "http://[::1]:8000/recieveMessage.php";
    private static final String MESSAGE_START = "message start~";
    private static final String MESSAGE_END= "~message end";
    private static final String LINE_DELIMETER= "##~";
    
    public MessageReciever() {
        new Thread(this).start();
    }
    static{
        
    }
    @Override
    public void run() {
        while(true){
            try {
                read();
                Thread.sleep(500);
            } catch (IOException ex) {
                Logger.getLogger(MessageReciever.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(MessageReciever.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void read() throws MalformedURLException, IOException{
        URL obj = new URL(READ_URL);
	
     
        Scanner scanner = new Scanner(obj.openConnection().getInputStream());
        StringBuilder line = new StringBuilder();
        while(scanner.hasNextLine()){
            line.append(scanner.nextLine());
        }
       // System.out.println(line);
        int start = line.indexOf(MESSAGE_START);
        int end = line.indexOf(MESSAGE_END);
        while(start!=-1&&end!=-1){
            String message = line.substring(start+MESSAGE_START.length(),end);
            parseMessage(message);
             start = line.indexOf(MESSAGE_START,end);
             end = line.indexOf(MESSAGE_END,start);
        }
        
    }
    private  void parseMessage(String message){
        String tokens[] = message.split(LINE_DELIMETER);
        if(tokens.length<3){
            return;
        }
        String name=tokens[0].trim();
        String no=tokens[1].trim().split("@")[0];
        String data = tokens[2].trim();
        StaffConnector conn = staff.get(no);
        conn.addMessage(message);
    }
   public static void main(String[] args){
        new MessageReciever();
    }
   
}


