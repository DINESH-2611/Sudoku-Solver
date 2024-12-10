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

@WebServlet("/AddBook")
public class AddBook extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        name = name.toUpperCase();
        String author = request.getParameter("author");
        author=author.toUpperCase();
        String publisher = request.getParameter("publisher");
        publisher=publisher.toUpperCase();
        String quantity = request.getParameter("quantity");


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DB.getCon();

            String sql = "SELECT * FROM Book WHERE name = ? AND author = ? AND publisher=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, publisher);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String message = "Book already Exist!";
                String redirectUrl = "adminaddbook.html";
                String encodedMessage = URLEncoder.encode(message, "UTF-8");
                String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                response.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);

            } else {
                String sql1 = "INSERT INTO book (name, author, publisher,quantity) VALUES (?, ?, ?,?)";
                preparedStatement = connection.prepareStatement(sql1);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, publisher);
                preparedStatement.setInt(4, Integer.parseInt(quantity));
                preparedStatement.executeUpdate();

                String message = "Book added successfully";
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
