package com.example.attendclasstpad.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 题目模型类
 *
 * @author chenhui 2019.04.04
 */
public class Test implements Serializable {
    private String id;// 唯一ID
    private String title;// 题目标题
    private String questionHtml;// 题文Html
    private String answerAnalysisHtml;// 答案及解析Html
    private String knowledge;// 所属知识点
    private String typeID;// 所属题型ID
    private String typeName;// 所属题型名称
    private String level;// 所属难度名称

    private boolean isShowAA = false;// 是否显示答案与解析，默认为不显示
    private boolean hasJoinInPreview = false;// 是否加入预览，默认为不加入
    private boolean isChoiced;//是否被选中

    @JSONField(name = "DataID")
    public String getId() {
        return id;
    }

    @JSONField(name = "DataID")
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JSONField(name = "Question")
    public String getQuestionHtml() {
        return questionHtml;
    }

    @JSONField(name = "Question")
    public void setQuestionHtml(String questionHtml) {
        this.questionHtml = questionHtml;
    }

    @JSONField(name = "Answer")
    public String getAnswerAnalysisHtml() {
        return answerAnalysisHtml;
    }

    @JSONField(name = "Answer")
    public void setAnswerAnalysisHtml(String answerAnalysisHtml) {
        this.answerAnalysisHtml = answerAnalysisHtml;
    }

    @JSONField(name = "Knowledge")
    public String getKnowledge() {
        return knowledge;
    }

    @JSONField(name = "Knowledge")
    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    @JSONField(name = "DataType")
    public String getTypeName() {
        return typeName;
    }

    @JSONField(name = "DataType")
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @JSONField(name = "DataLevel")
    public String getLevel() {
        return level;
    }

    @JSONField(name = "DataLevel")
    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isShowAA() {
        return isShowAA;
    }

    public void setShowAA(boolean isShowAA) {
        this.isShowAA = isShowAA;
    }

    public boolean isHasJoinInPreview() {
        return hasJoinInPreview;
    }

    public void setHasJoinInPreview(boolean hasJoinInPreview) {
        this.hasJoinInPreview = hasJoinInPreview;
    }
    public boolean isChoiced() {
        return isChoiced;
    }

    public void setChoiced(boolean choiced) {
        isChoiced = choiced;
    }
}
