/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bro.servlets;

import com.bro.Connector.MessageReciever;
import com.bro.Connector.StaffConnector;
import com.bro.utils.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author arshed
 */
@WebServlet(name = "ClientReciever", urlPatterns = {"/clientreciever"}, initParams = {
    @WebInitParam(name = "tenant", value = "Value")})
public class ClientSend extends HttpServlet {

    
    static Map<String,StaffConnector> staffMap = new HashMap<String,StaffConnector>();
    public static StaffConnector getStaff(String id){
       return staffMap.get(id);
    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        new Thread(new MessageReciever()).start();
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Object isOld= request.getSession().getAttribute("isOld");
        String id = request.getSession().getId();
        StaffConnector staff=getStaff(id);
        if(staff==null ){
            staff = StaffConnector.getStaff(id);
            staffMap.put(id, staff);
            staff.sendInitmation();
        }
        String text = request.getParameter("data");
        staff.sendMessage(text);
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
