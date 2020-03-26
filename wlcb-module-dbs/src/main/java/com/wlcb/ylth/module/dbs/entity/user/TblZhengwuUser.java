package com.wlcb.ylth.module.dbs.entity.user;

import com.wlcb.ylth.module.dbs.entity.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName tblZhengwuUser
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-03-25 22:04
 * @Version 1.0
 */
@Data
public class TblZhengwuUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 2270601188942777314L;

    private String name;
    private String phone;
    private String openid;
    private String idcard;
    private Date insertAt;
    private String data;
    private String bankCardNumber;
    private String bankCardAddress;
    // 区分普通用户和政府用户，普通为0，政府为1
    private Integer rule;
    // 注册码
    private String code;
    private Integer bind; // 政府用户绑定状态，0是未绑定，1是已绑定
    private String position;
    private Integer officeId;
    private Integer cldw;
    private Date updateAt;
    private Integer gaosu;
    private Integer jiedao; // 还是officeId
    private String address;
    private Integer shequ;
    private Integer xiaoqu;
    private String uid; //uid
    private String loudong;
    private String menpai;
    private Integer tjdw;
    private Integer wuhan;
    private Date geliAt;
    private String shequStr;
    private String xiaoquStr;
    private String jiedaoStr;
    private String quxian;
    private String miyao;
    private Integer healthStatus;
    private String userinfo;
    private String carNumber;
    private Integer idtype;
    private String gzdw;

    private String qrcodeText;


    private Integer kcount;

    public String getBindStr() {
        if (bind == null) {
            return "非注册用户";
        } else if (bind == 0) {
            return "未绑定";
        } else if (bind == 1) {
            return "已绑定";
        } else {
            return "绑定信息异常";
        }
    }
}
