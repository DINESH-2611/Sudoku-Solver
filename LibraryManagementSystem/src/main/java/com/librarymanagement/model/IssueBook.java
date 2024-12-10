package com.librarymanagement.model;

import java.sql.Date;

public class IssueBook {
    private int id, bookId, userId;
    String bookName, email,status;

    public IssueBook(int id, int bookId, int userId, String bookName, String email,String status) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.bookName = bookName;
        this.email = email;
        this.status=status;
    }

    public IssueBook() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "IssueBook{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", bookName='" + bookName + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
