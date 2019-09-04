package com.example.attendclasstpad.model;

/**
 * 题目模型类
 *
 * @author chenhui 2019.04.04
 */
public class Test {
    private String id;// 唯一ID
    private String title;// 题目标题
    private String content;// 题目内容
    private String answer;// 答案
    private String analysis;// 解析
    private boolean isChoiced;// 是否已被选中

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public boolean isChoiced() {
        return isChoiced;
    }

    public void setChoiced(boolean isChoiced) {
        this.isChoiced = isChoiced;
    }

}
