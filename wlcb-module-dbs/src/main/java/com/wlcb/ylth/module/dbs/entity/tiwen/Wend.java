package com.wlcb.ylth.module.dbs.entity.tiwen;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
public class Wend implements Serializable {

    private static final long serialVersionUID = -289991068376786232L;
    private String uuid;
    private String img;
    private String wendu;
    private String detail;
    private Date insertAt;
    private String openid;

    private String jiedao;
    private String shequ;
    private String xiaoqu;
    private String loudong;
    private String menpai;
    private Integer isNormal;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wend wend = (Wend) o;
        return Objects.equals(uuid, wend.uuid) &&
                Objects.equals(img, wend.img) &&
                Objects.equals(wendu, wend.wendu) &&
                Objects.equals(detail, wend.detail) &&
                Objects.equals(insertAt, wend.insertAt) &&
                Objects.equals(openid, wend.openid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, img, wendu, detail, insertAt, openid);
    }
}
