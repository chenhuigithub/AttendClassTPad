package com.example.attendclasstpad.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 用户信息模型类
 *
 * @author chenhui 2018.06.27
 */
public class User implements Serializable {
    private String id;// 唯一ID
    private String name;// 真实姓名
    private String headPic;// 头像
    private String mobileNum;// 手机号码
    private String email;// 邮箱

    private String loginName;// 登录名
    private String loginPassword;// 登录密码

    private String type;//用户登录类型（APP类型:备课:1，授课:2，授课学生端:3）

    private AdditionalInfo additionalInfo;// 附加信息

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JSONField(name = "NickName")
    public String getName() {
        return name;
    }

    @JSONField(name = "NickName")
    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "Avatar")
    public String getHeadPic() {
        return headPic;
    }

    @JSONField(name = "Avatar")
    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    @JSONField(name = "Mobile")
    public String getMobileNum() {
        return mobileNum;
    }

    @JSONField(name = "Mobile")
    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JSONField(name = "txtname")
    public String getLoginName() {
        return loginName;
    }

    @JSONField(name = "txtname")
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @JSONField(name = "txtpwd")
    public String getLoginPassword() {
        return loginPassword;
    }

    @JSONField(name = "txtpwd")
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    @JSONField(name = "model")
    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    @JSONField(name = "model")
    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @JSONField(name = "type")
    public String getType() {
        return type;
    }

    @JSONField(name = "type")
    public void setType(String type) {
        this.type = type;
    }
}
