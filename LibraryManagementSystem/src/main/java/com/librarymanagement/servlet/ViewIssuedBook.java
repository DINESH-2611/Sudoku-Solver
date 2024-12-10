package com.librarymanagement.servlet;

import com.google.gson.Gson;
import com.librarymanagement.Database.DB;
import com.librarymanagement.model.IssueBook;

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

@WebServlet("/ViewIssuedBook")
public class ViewIssuedBook extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            PrintWriter out=response.getWriter();
            List<IssueBook> books=new ArrayList<>();
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);

            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null,resultSet1=null;

            try {
                connection = DB.getCon();



                String sql = "SELECT * FROM issuebook";
                preparedStatement = connection.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int userId=resultSet.getInt("userid");
                    String sql1 = "SELECT email FROM user where id=?";
                    preparedStatement = connection.prepareStatement(sql1);
                    preparedStatement.setInt(1,userId);
                    resultSet1 = preparedStatement.executeQuery();
                    if(resultSet1.next()) {
                        String email = resultSet1.getString("email");
                        IssueBook issueBook = new IssueBook();
                        issueBook.setId(resultSet.getInt("id"));
                        issueBook.setBookName(resultSet.getString("bookname"));
                        issueBook.setStatus(resultSet.getString("status"));
                        issueBook.setEmail(email);
                        books.add(issueBook);
                    }
                }
                if(books.size()!=0) {
                    response.setContentType("application/json");
                    String json = new Gson().toJson(Map.of("books", books));

                    out.write(json);
                    out.flush();
                }else{
                    String encodedMessage = URLEncoder.encode("No book is issued yet!", "UTF-8");
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




