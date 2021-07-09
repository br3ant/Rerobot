package com.bayi.rerobot.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TargetBean {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String cnname;
    @Generated(hash = 1981912559)
    public TargetBean(Long id, String name, String cnname) {
        this.id = id;
        this.name = name;
        this.cnname = cnname;
    }
    @Generated(hash = 196922752)
    public TargetBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCnname() {
        return this.cnname;
    }
    public void setCnname(String cnname) {
        this.cnname = cnname;
    }
}
