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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author arshed
 */
public class StaffConnector {
    private final String clientID;
    /*
    Should auto-select a staff and set his phone no
    */
    final String SEND_MESSAGE_URL = "http://localhost:8000/sendMessage.php";
    private final String mobileNo = "919449336312";
    private static final String INTIMATION_MESSAGE = "New client is waiting for you";
    private  StringBuilder message = new StringBuilder();
    private StaffConnector(String clientID){
        this.clientID = clientID;
    }
    public void sendMessage(String message) throws MalformedURLException, IOException{
        URL obj = new URL(formMessage(message));
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.disconnect();
    }
    public void sendInitmation() throws IOException{
        sendMessage(INTIMATION_MESSAGE);
    }
    private String formMessage(String message){
       return SEND_MESSAGE_URL+"?"+"no="+mobileNo+"&msg="+message; 
    }
    public synchronized void addMessage(String newMessage){
        message.append(newMessage);
        this.notify();
    }
    public synchronized String readMessage(){
        String result="";
        if(message.length()>0){
            String string = message.toString();
            message = null;
            return string;
        } 
        
        try {
            this.wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(StaffConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(message.length()>0){
            String string = message.toString();
            message = null;
            return string;
        } 
        return result;
    }
    public static StaffConnector getStaff(String clientID){
        if(onlyStaff==null){
            onlyStaff = new StaffConnector(clientID);
            MessageReciever.staff.put("9194499336312",onlyStaff);
        }
        return onlyStaff;
    }
    static StaffConnector onlyStaff;
}
