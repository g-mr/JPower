package com.wlcb.module.dbs.entity.tiwen;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Data
public class Notices {
    private String id;
    private String author;
    private Integer views;
    private String title;
    private Timestamp createdAt;
    private Timestamp updateAt;
    private String content;
    private String img;
    private String href;
    private Integer isShow;
    private Integer type;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notices notices = (Notices) o;
        return Objects.equals(id, notices.id) &&
                Objects.equals(author, notices.author) &&
                Objects.equals(views, notices.views) &&
                Objects.equals(title, notices.title) &&
                Objects.equals(createdAt, notices.createdAt) &&
                Objects.equals(updateAt, notices.updateAt) &&
                Objects.equals(content, notices.content) &&
                Objects.equals(img, notices.img) &&
                Objects.equals(href, notices.href) &&
                Objects.equals(isShow, notices.isShow) &&
                Objects.equals(type, notices.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, views, title, createdAt, updateAt, content, img, href, isShow, type);
    }
}
