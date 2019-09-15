package com.zhitar.library.domain;

import com.zhitar.library.annotation.Table;

import java.io.Serializable;
import java.util.Objects;

@Table("book_attribute")
public class BookAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer bookId;
    private Integer attributeId;

    public BookAttribute() {
    }

    public BookAttribute(Integer bookId, Integer attributeId) {
        this.bookId = bookId;
        this.attributeId = attributeId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Integer attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAttribute attribute = (BookAttribute) o;
        return Objects.equals(bookId, attribute.bookId) &&
                Objects.equals(attributeId, attribute.attributeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, attributeId);
    }

    @Override
    public String toString() {
        return "BookAttribute{" +
                "bookId=" + bookId +
                ", attributeId=" + attributeId +
                '}';
    }
}
