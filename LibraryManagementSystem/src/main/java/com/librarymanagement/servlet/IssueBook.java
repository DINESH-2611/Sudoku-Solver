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

@WebServlet("/IssueBook")
public class IssueBook extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = LibraryManagementDatabase.getInstance().getSession();
        int bookId = Integer.parseInt(req.getParameter("bookId"));
        int userId = (int) session.getAttribute("userid");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DB.getCon();

            String checkQuantitySql = "SELECT quantity FROM Book WHERE id = ?";
            preparedStatement = connection.prepareStatement(checkQuantitySql);
            preparedStatement.setInt(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int quantity = resultSet.getInt("quantity");

                if (quantity > 0) {
                    String getBookname = "SELECT name from Book where id=?";
                    preparedStatement = connection.prepareStatement(getBookname);
                    preparedStatement.setInt(1, bookId);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        String bookname = resultSet.getString("name");
                        String issueBookSql = "INSERT INTO issuebook (userid, bookid,status,bookname) VALUES (?, ?,?,?)";
                        preparedStatement = connection.prepareStatement(issueBookSql);
                        preparedStatement.setInt(1, userId);
                        preparedStatement.setInt(2, bookId);
                        preparedStatement.setString(3, "Not returned");
                        preparedStatement.setString(4, bookname);
                        preparedStatement.executeUpdate();

                        String updateUserSql = "UPDATE user SET bookcount = bookcount + 1 WHERE id = ?";
                        preparedStatement = connection.prepareStatement(updateUserSql);
                        preparedStatement.setInt(1, userId);
                        preparedStatement.executeUpdate();

                        String updateBookSql = "UPDATE Book SET quantity = quantity - 1 WHERE id = ?";
                        preparedStatement = connection.prepareStatement(updateBookSql);
                        preparedStatement.setInt(1, bookId);
                        preparedStatement.executeUpdate();

                        String message = "Book issued successfully!";
                        String redirectUrl = "userpage.html";
                        String encodedMessage = URLEncoder.encode(message, "UTF-8");
                        String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                        resp.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);
                    }
                } else {
                    String message = "Sorry, this book is out of stock!";
                    String redirectUrl = "userpage.html";
                    String encodedMessage = URLEncoder.encode(message, "UTF-8");
                    String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                    resp.sendRedirect("success.html?message=" + encodedMessage + "&redirectUrl=" + encodedRedirectUrl);
                }
            } else {
                String message = "Book not found!";
                String redirectUrl = "userpage.html";
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
