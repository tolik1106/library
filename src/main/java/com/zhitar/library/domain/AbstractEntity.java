package com.zhitar.library.domain;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity<PK extends Serializable> implements Serializable{

    private static final long serialVersionUID = 1L;

    protected PK id;

    protected AbstractEntity() {
    }

    protected AbstractEntity(PK id) {
        this.id = id;
    }

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity<?> that = (AbstractEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
