package com.bootdo.data.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * 供应商信息表
 * @Author: yogiCai
 * @date 2017-11-24 23:12:54
 */
public class VendorDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
    //编号
    private Integer no;
	//供应商名称
	private String name;
	//供应商类别
	private String type;
	//联系人
	private String contacts;
	//联系人电话
	private String phone;
    //供应商地址
    private String address;
	//状态
	private Integer status = 0;
	//创建时间
	private Date createTime;
	//修改时间
	private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
