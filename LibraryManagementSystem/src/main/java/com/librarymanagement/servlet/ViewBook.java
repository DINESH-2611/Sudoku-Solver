package com.librarymanagement.servlet;

import com.google.gson.Gson;
import com.librarymanagement.Database.DB;
import com.librarymanagement.Database.LibraryManagementDatabase;
import com.librarymanagement.model.Book;
import com.librarymanagement.model.User; // Assuming User class exists

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.RequestDispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/ViewBook")
public class ViewBook extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out=response.getWriter();
        List<Book> books=new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DB.getCon();

            String sql = "SELECT * FROM Book";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    Book book = new Book();
                    book.setId(resultSet.getInt("id"));
                    book.setName(resultSet.getString("name"));
                    book.setAuthor(resultSet.getString("author"));
                    book.setPublisher(resultSet.getString("publisher"));
                    book.setQuantity(resultSet.getInt("quantity"));
                    books.add(book);
                }
            if(books.size()!=0) {
                response.setContentType("application/json");
                String json = new Gson().toJson(Map.of("books", books));

                out.write(json);
                out.flush();
            }else{
                String message="Book list is empty!",redirectUrl;
                if((int)request.getSession().getAttribute("role")==0) {
                    System.out.println("ifffffff");
                     redirectUrl = "adminpage.html";
                }else {
                    redirectUrl = "userpage.html";
                }
                    String encodedMessage = URLEncoder.encode(message, "UTF-8");
                    String encodedRedirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
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


