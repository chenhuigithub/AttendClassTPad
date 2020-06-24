package com.example.attendclasstpad.model;

import java.io.Serializable;
import java.util.List;

/**
 * 试卷
 */
public class TestPaper extends ICanGetKeyValue implements Serializable {
    private String ID; //试卷ID
    private String name;//试卷名称
    private String createTime;//创建时间
    private String testNum;//题目数量
    private String type;//试卷状态(0:未布置，1:正在做题，2:已做完待批阅)
    private List<Test> questionList;//试题列表

    private boolean isChoiced;//被选中

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTestNum() {
        return testNum;
    }

    public void setTestNum(String testNum) {
        this.testNum = testNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Test> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Test> questionList) {
        this.questionList = questionList;
    }

    public boolean isChoiced() {
        return isChoiced;
    }

    public void setChoiced(boolean choiced) {
        isChoiced = choiced;
    }
}
