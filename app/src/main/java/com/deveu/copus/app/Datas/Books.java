package com.deveu.copus.app.Datas;

public class Books {
    private String bookid;
    private String bookname;
    private String authorname;
    private String bookdescription;
    private String bookphoto;
    private long viewcount;
    private long downcount;
    private long likecount;
    private String categoryid;
    private long count;
    private long bookpage;
    private String pdflink;

    public Books() {
    }

    public Books(String bookid, String bookname, String bookdescription, String bookphoto, long viewcount, long downcount, long likecount, String categoryid, long count, long bookpage, String authorname,
                 String pdflink) {
        this.bookid = bookid;
        this.bookname = bookname;
        this.bookdescription = bookdescription;
        this.bookphoto = bookphoto;
        this.viewcount = viewcount;
        this.downcount = downcount;
        this.likecount = likecount;
        this.categoryid = categoryid;
        this.count = count;
        this.authorname=authorname;
        this.bookpage = bookpage;
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

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getBookdescription() {
        return bookdescription;
    }

    public void setBookdescription(String bookdescription) {
        this.bookdescription = bookdescription;
    }

    public String getBookphoto() {
        return bookphoto;
    }

    public void setBookphoto(String bookphoto) {
        this.bookphoto = bookphoto;
    }

    public long getViewcount() {
        return viewcount;
    }

    public void setViewcount(long viewcount) {
        this.viewcount = viewcount;
    }

    public long getDowncount() {
        return downcount;
    }

    public void setDowncount(long downcount) {
        this.downcount = downcount;
    }

    public long getLikecount() {
        return likecount;
    }

    public void setLikecount(long likecount) {
        this.likecount = likecount;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getBookpage() {
        return bookpage;
    }

    public void setBookpage(long bookpage) {
        this.bookpage = bookpage;
    }
}
