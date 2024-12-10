package com.librarymanagement.servlet;

import com.librarymanagement.Database.DB;
import com.librarymanagement.Database.LibraryManagementDatabase;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/AdminLogin")
public class AdminLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        LibraryManagementDatabase.getInstance().setSession(request.getSession());
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setContentType("text/html");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = DB.getCon();

            String sql = "SELECT role FROM credentials WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int role=resultSet.getInt("role");
                LibraryManagementDatabase.getInstance().getSession().setAttribute("role",role);
                if(role==0) {
                    LibraryManagementDatabase.getInstance().getSession().setAttribute("admin", email);
                    response.sendRedirect("adminpage.html");
                }else{
                    LibraryManagementDatabase.getInstance().getSession().setAttribute("user", email);
                    String sql1 = "SELECT id FROM user WHERE email = ?";
                    preparedStatement = connection.prepareStatement(sql1);
                    preparedStatement.setString(1, email);

                    ResultSet resultSet1=preparedStatement.executeQuery();
                    if(resultSet1.next()){
                        int userId=resultSet1.getInt("id");
                        LibraryManagementDatabase.getInstance().getSession().setAttribute("userid",userId);
                    }
                    response.sendRedirect("userpage.html");
                }

            } else {
                String message = "Invalid email or password!";
                String redirectUrl = "index.html";

                String encodedMessage = URLEncoder.encode(message, "UTF-8");
                String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                response.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
