package com.example.attendclasstpad.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程模型类
 *
 * @author zhaochenhui，2018.05.17
 */
public class Course {
    private String unitID;
    private String unitName;
    private List<Lesson> lessonList;

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public List<Lesson> getLessonList() {
        if (lessonList == null) {
            return new ArrayList<Lesson>();
        }

        return lessonList;
    }

    public void setLessonList(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

}
