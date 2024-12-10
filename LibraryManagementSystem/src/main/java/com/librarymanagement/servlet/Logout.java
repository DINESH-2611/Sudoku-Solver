package com.librarymanagement.servlet;

import com.librarymanagement.Database.LibraryManagementDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/Logout")
public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = LibraryManagementDatabase.getInstance().getSession();

        String referer = req.getHeader("Referer");

        if (referer != null && referer.contains("userpage.html")) {
            if(session.getAttribute("admin")==null){
                LibraryManagementDatabase.getInstance().setSession(null);
                session.invalidate();}
            else
            session.removeAttribute("user");
        }

        else if (referer != null && referer.contains("adminpage.html")) {
            if(session.getAttribute("user")==null){
                session.invalidate();
                LibraryManagementDatabase.getInstance().setSession(null);
            }
            else
                session.removeAttribute("admin");
        }

        resp.setHeader("Cache-Control", "no-store");  // HTTP 1.1
        resp.setHeader("Pragma", "no-cache");         // HTTP 1.0
        resp.setDateHeader("Expires", 0);              // Proxies

        resp.sendRedirect("index.html");
    }
}
