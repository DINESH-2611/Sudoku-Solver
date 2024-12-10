package com.librarymanagement.servlet;

import com.librarymanagement.Database.DB;
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

@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String mobile = request.getParameter("mobile");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection= DB.getCon();

            String sql = "SELECT * FROM User WHERE email = ? OR mobile = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, mobile);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String message = "Email or Phone No already Exist!";
                String redirectUrl = "adminadduser.html";
                String encodedMessage = URLEncoder.encode(message, "UTF-8");
                String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                response.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);

            } else {
                String sql1="INSERT INTO user (name, email, mobile,bookcount) VALUES (?, ?, ?,?)";
                preparedStatement=connection.prepareStatement(sql1);
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,email);
                preparedStatement.setLong(3,Long.parseLong(mobile));
                preparedStatement.setInt(4,0);
                preparedStatement.executeUpdate();

                String sql2="INSERT INTO credentials (username, password,role) VALUES (?, ?,?)";
                preparedStatement=connection.prepareStatement(sql2);
                preparedStatement.setString(1,email);
                preparedStatement.setString(2,password);
                preparedStatement.setInt(3,1);
                preparedStatement.executeUpdate();
                String message = "User added successfully";
                String redirectUrl = "adminpage.html";
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

