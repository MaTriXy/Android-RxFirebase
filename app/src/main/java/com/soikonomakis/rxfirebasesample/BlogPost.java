package com.soikonomakis.rxfirebasesample;

public class BlogPost {

  private String author;

  private String title;

  public BlogPost() {
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override public String toString() {
    final StringBuilder sb = new StringBuilder("BlogPost{");
    sb.append("author='").append(author).append('\'');
    sb.append(", title='").append(title).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
