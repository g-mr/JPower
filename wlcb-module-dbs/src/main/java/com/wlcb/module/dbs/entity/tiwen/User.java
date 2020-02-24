package com.wlcb.module.dbs.entity.tiwen;

import lombok.Data;

import java.util.Objects;

@Data
public class User {
    private String openid;
    private String name;
    private String phone;
    private String idcard;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(openid, user.openid) &&
                Objects.equals(name, user.name) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(idcard, user.idcard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openid, name, phone, idcard);
    }
}
