package com.pinyougou.pojo;

import java.io.Serializable;

public class Brand implements Serializable{

    private Long id;
    private String name;
    private String firstchar;

    public Brand(Long id, String name, String firstchar) {
        this.id = id;
        this.name = name;
        this.firstchar = firstchar;
    }

    public Brand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstchar() {
        return firstchar;
    }

    public void setFirstchar(String firstchar) {
        this.firstchar = firstchar;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstchar='" + firstchar + '\'' +
                '}';
    }
}
