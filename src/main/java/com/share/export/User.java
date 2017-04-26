package com.share.export;

import java.math.BigDecimal;
import java.util.Date;

public class User {

    @Excel(name = "姓名", width = 30)
    private String name;
 
    @Excel(name = "年龄", width = 60)
    private String age;
 
    @Excel(skip = true)
    private String password;
 
    @Excel(name = "xx")
    private Double xx;
 
    @Excel(name = "yy")
    private Date yy;
 
    @Excel(name = "锁定")
    private Boolean locked;
 
    @Excel(name = "金额")
    private BigDecimal db;
	
	public User() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}


	public double getXx() {
		return xx;
	}

	public void setXx(double xx) {
		this.xx = xx;
	}

	public Date getYy() {
		return yy;
	}

	public void setYy(Date yy) {
		this.yy = yy;
	}

	public BigDecimal getDb() {
		return db;
	}

	public void setDb(BigDecimal db) {
		this.db = db;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	
}
