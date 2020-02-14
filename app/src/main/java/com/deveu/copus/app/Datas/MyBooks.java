package com.deveu.copus.app.Datas;

public class MyBooks {
    private String bookid;
    private String bookname;
    private String bookphoto;
    private String pdflink;


    public MyBooks() {
    }

    public MyBooks(String bookid, String bookname, String bookphoto,String pdflink) {
        this.bookid = bookid;
        this.bookname = bookname;
        this.bookphoto = bookphoto;
        this.pdflink = pdflink;
    }


    public String getPdflink() {
        return pdflink;
    }

    public void setPdflink(String pdflink) {
        this.pdflink = pdflink;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookphoto() {
        return bookphoto;
    }

    public void setBookphoto(String bookphoto) {
        this.bookphoto = bookphoto;
    }
}
