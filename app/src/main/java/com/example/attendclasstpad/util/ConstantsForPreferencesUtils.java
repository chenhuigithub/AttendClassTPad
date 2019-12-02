package com.example.attendclasstpad.util;

/**
 * SharedPerferences首选项表中的字段命名
 *
 * @author zhaochenhui_2017.12.05
 */
public class ConstantsForPreferencesUtils {
    /**
     * 登录名
     */
    public static final String LOGIN_NAME = "loginName";

    /**
     * 昵称（没有昵称时=登录名）
     */
    public static final String NICK_NAME = "nickName";
    /**
     * 登录密码
     */
    public static final String LOGIN_PASSWORD = "loginPassword";

    /**
     * 角色id
     */
    public static final String ROLE_ID = "roleId";

    /**
     * 头像路径
     */
    public static final String USER_HEAD_PIC_URL = "userHeadPicUrl";

    /**
     * 头像base64
     */
    public static final String USER_HEAD_PIC_BASE64 = "USER_HEAD_PIC_BASE64";

    /**
     * 当前页码(分页页卡，第几个页卡)
     */
    public static final String CURRENT_PAGE_NUM = "Current_Page_Num";
    /**
     * token
     */
    public static final String TOKEN = "token";
    /**
     * 章节id
     */
    public static final String CHAPTER_ID = "chapterId";
    /**
     * 章节名称
     */
    public static final String CHAPTER_NAME = "chapterName";

    /**
     * 班级数据(jsonArray格式)
     */
    public static final String CLASS_LIST_JSONARR = "CLASS_LIST_JSONARR";

    /**
     * 授课课程筛选条件标准名称（高中-语文-必修一-人教版）
     */
    public static final String ATTEND_COURSE_SCREENING_CRITERIA_NAME = "ATTEND_COURSE_SCREENING_CRITERIA_NAME";

    /**
     * 课程筛选条件标准名称（高中-语文-必修一-人教版）
     */
    public static final String COURSE_SCREENING_CRITERIA_NAME = "COURSE_SCREENING_CRITERIA_NAME";

    /**
     * 授课-课程筛选条件-学段ID
     */
    public static final String ATTEND_CLASS_CHOICE_PERIOD_ID = "ATTEND_CLASS_CHOICE_PERIOD_ID";

    /**
     * 授课-课程筛选条件-学科ID
     */
    public static final String ATTEND_CLASS_CHOICE_SUBJECT_ID = "ATTEND_CLASS_CHOICE_SUBJECT_ID";

    /**
     * 授课-课程筛选条件-版本ID
     */
    public static final String ATTEND_CLASS_CHOICE_EDITION_ID = "ATTEND_CLASS_CHOICE_EDITION_ID";

    /**
     * 授课-课程筛选条件-模块ID
     */
    public static final String ATTEND_CLASS_CHOICE_MODULE_ID = "ATTEND_CLASS_CHOICE_MODULE_ID";

    /**
     * 授课选择教材 最后一步筛选的条件ID
     */
    public static final String ATTEND_CLASS_CHOICE_LAST_ID = "ATTEND_CLASS_CHOICE_LAST_ID";
}
