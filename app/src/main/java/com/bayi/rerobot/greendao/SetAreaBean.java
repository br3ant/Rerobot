package com.bayi.rerobot.greendao;

import com.tobot.slam.data.LocationBean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

//设置区域名称
@Entity
public class SetAreaBean {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String areaname;
    private boolean isSelected;
    private boolean isDaolan;
    @Convert(columnType = String.class, converter = LocationBeanConverter.class)
    private List<LocationBean> data;
    @Generated(hash = 243223899)
    public SetAreaBean(Long id, String areaname, boolean isSelected,
            boolean isDaolan, List<LocationBean> data) {
        this.id = id;
        this.areaname = areaname;
        this.isSelected = isSelected;
        this.isDaolan = isDaolan;
        this.data = data;
    }
    @Generated(hash = 721574089)
    public SetAreaBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAreaname() {
        return this.areaname;
    }
    public void setAreaname(String areaname) {
        this.areaname = areaname;
    }
    public boolean getIsSelected() {
        return this.isSelected;
    }
    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public List<LocationBean> getData() {
        return this.data;
    }
    public void setData(List<LocationBean> data) {
        this.data = data;
    }
    public boolean getIsDaolan() {
        return this.isDaolan;
    }
    public void setIsDaolan(boolean isDaolan) {
        this.isDaolan = isDaolan;
    }

}
