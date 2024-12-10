package com.librarymanagement.Database;


import jakarta.servlet.http.HttpSession;


public class LibraryManagementDatabase {
    private static LibraryManagementDatabase libraryManagementDatabase;
    private HttpSession session;

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public static LibraryManagementDatabase getInstance() {
        if (libraryManagementDatabase == null) {
            libraryManagementDatabase = new LibraryManagementDatabase();

        }
        return libraryManagementDatabase;
    }


}
