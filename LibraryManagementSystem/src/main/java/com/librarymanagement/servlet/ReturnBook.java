package com.librarymanagement.servlet;


import com.librarymanagement.Database.DB;
import com.librarymanagement.Database.LibraryManagementDatabase;
import com.librarymanagement.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/ReturnBook")
public class ReturnBook extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session =LibraryManagementDatabase.getInstance().getSession();
        int userId = (int) session.getAttribute("userid");
        String bookName=req.getParameter("name");
        bookName=bookName.toUpperCase();

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DB.getCon();
            String getBookCount="SELECT bookcount from user where id=?";
            preparedStatement=connection.prepareStatement(getBookCount);
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                if(resultSet.getInt("bookcount")<=0){
                    String message = "You have no book to return!";
                    String redirectUrl = "userreturnbook.html";
                    String encodedMessage = URLEncoder.encode(message, "UTF-8");
                    String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                    resp.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);
                    return;
                }
            }
            String checkQuantitySql = "Select bookid from issuebook WHERE userid=? AND bookname LIKE ? AND status=?";
            preparedStatement = connection.prepareStatement(checkQuantitySql);
            preparedStatement.setInt(1,userId);
            preparedStatement.setString(2,"%"+bookName+"%");
            preparedStatement.setString(3,"Not returned");
             resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int bookId = resultSet.getInt("bookid");

                String updateStatus = "UPDATE issuebook set status='Returned'  WHERE userid=? AND bookid=? AND status=?";
                preparedStatement = connection.prepareStatement(updateStatus);
                preparedStatement.setInt(1,userId);
                preparedStatement.setInt(2,bookId);
                preparedStatement.setString(3,"Not returned");
                preparedStatement.executeUpdate();

                    String updateUserSql = "UPDATE user SET bookcount = bookcount - 1 WHERE id = ?";
                    preparedStatement = connection.prepareStatement(updateUserSql);
                    preparedStatement.setInt(1, userId);
                    preparedStatement.executeUpdate();

                    String updateBookSql = "UPDATE Book SET quantity = quantity + 1 WHERE id = ?";
                    preparedStatement = connection.prepareStatement(updateBookSql);
                    preparedStatement.setInt(1, bookId);
                    preparedStatement.executeUpdate();
                    String message = "Book Returned successfully!";
                    String redirectUrl = "userpage.html";
                    String encodedMessage = URLEncoder.encode(message, "UTF-8");
                    String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                    resp.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);

                } else {
                String message = "Book name doesn't match!";
                String redirectUrl = "returnbook.html";
                String encodedMessage = URLEncoder.encode(message, "UTF-8");
                String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                resp.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String message = "An error occurred while issuing the book. Please try again later.";
            String redirectUrl = "userpage.html";
            String encodedMessage = URLEncoder.encode(message, "UTF-8");
            String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
            resp.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}




