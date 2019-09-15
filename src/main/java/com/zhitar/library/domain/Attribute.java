package com.zhitar.library.domain;

import com.zhitar.library.annotation.Table;
import com.zhitar.library.validation.annotation.Length;

@Table("attribute")
public class Attribute extends AbstractEntity<Integer> {
    private static final long serialVersionUID = 1L;

    @Length(min = 3, max = 30, message = "message.attributename.error")
    private String name;

    public Attribute() {
    }

    public Attribute(Integer id, String name) {
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
        return "Attribute{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
