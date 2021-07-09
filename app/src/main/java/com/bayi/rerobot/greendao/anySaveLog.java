package com.bayi.rerobot.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class anySaveLog {
    @Id(autoincrement = true)
    private Long id;
    private String sn;
    private String question;
    private String answer;
    private String time;
    @Generated(hash = 901911400)
    public anySaveLog(Long id, String sn, String question, String answer,
            String time) {
        this.id = id;
        this.sn = sn;
        this.question = question;
        this.answer = answer;
        this.time = time;
    }

    @Generated(hash = 1121192907)
    public anySaveLog() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
