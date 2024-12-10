package com.librarymanagement.Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    public static Connection getCon(){
        Connection con=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/librarymanagement","root","2103");

        }catch(Exception e){System.out.println(e);}
        return con;
    }
}
