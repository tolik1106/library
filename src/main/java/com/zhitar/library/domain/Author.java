package com.zhitar.library.domain;

import com.zhitar.library.validation.annotation.Length;

public class Author extends AbstractEntity<Integer> {

    private static final long serialVersionUID = 1L;

    @Length(min = 3, message = "message.authorname.error")
    private String name;

    public Author() {
    }

    public Author(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
