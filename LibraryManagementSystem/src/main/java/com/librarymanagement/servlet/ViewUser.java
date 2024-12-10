package com.librarymanagement.servlet;

import com.google.gson.Gson;
import com.librarymanagement.Database.DB;
import com.librarymanagement.Database.LibraryManagementDatabase;
import com.librarymanagement.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/ViewUser")
public class ViewUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out=response.getWriter();
        List<User> users=new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DB.getCon();

            String sql = "SELECT * FROM User";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
               User user=new User();
               user.setId(resultSet.getInt("id"));
               user.setName(resultSet.getString("name"));
               user.setEmail(resultSet.getString("email"));
               user.setMobile(resultSet.getLong("mobile"));
               user.setBookCount(resultSet.getInt("bookcount"));
               users.add(user);
            }
//                System.out.println(users.size());
            if(users.size()!=0){
//                System.out.println(users.size());
//                System.out.println(users);
                response.setContentType("application/json");
                String json = new Gson().toJson(Map.of("users", users));

                out.write(json);
                out.flush();
            }else{
                System.out.println("else");
                String encodedMessage = URLEncoder.encode("User list is empty", "UTF-8");
                String encodedRedirectUrl = URLEncoder.encode("adminpage.html", "UTF-8");
                response.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);
            }
        }catch (Exception e) {
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

