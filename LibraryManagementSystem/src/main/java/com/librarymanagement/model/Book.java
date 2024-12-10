package com.librarymanagement.model;

public class Book {
	private int id;
private String name,author,publisher;
private int quantity;
public Book() {
	super();
	// TODO Auto-generated constructor stub
}
public Book(int callno, String name, String author, String publisher, int quantity) {
	super();
	this.id = callno;
	this.name = name;
	this.author = author;
	this.publisher = publisher;
	this.quantity = quantity;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public String getPublisher() {
	return publisher;
}
public void setPublisher(String publisher) {
	this.publisher = publisher;
}
public int getQuantity() {
	return quantity;
}
public void setQuantity(int quantity) {
	this.quantity = quantity;
}
//public int getIssued() {
//	return issued;
//}
//public void setIssued(int issued) {
//	this.issued = issued;
//}

	@Override
	public String toString() {
		return "Book{" +
				"ISBN='" + id + '\'' +
				", name='" + name + '\'' +
				", author='" + author + '\'' +
				", publisher='" + publisher + '\'' +
				", quantity=" + quantity +
				'}';
	}
}
