package com.example.attendclasstpad.model;

import java.util.List;

/**
 * Created by Lqy0702 on 2018/6/21.
 */

public class OmicsBean {

	/**
	 * code : 0 msg : data :
	 * {"CategoryList":[{"DataID":4,"DataPID":0,"DataName":"课前预习"},
	 * {"DataID":5,"DataPID":4,"DataName":"知识梳理"}],
	 * "DataList":[{"DataID":"e36f2815-23f8-44bf-960f-e768eaeea212"
	 * ,"DataName":"形式�?,"Question":null,"Answer":null}
	 * ,{"DataID":"0343e30a-b625-46d4-97c6-056f1119a507"
	 * ,"DataName":"【知识与�?���?．举例说明生命活\u2026","Question":"
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span
	 * style=\"font-family:黑体\">【知识与�?���?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．举例说<\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\
	 * "font-family:宋体\">生命活动建立在细胞的基础上�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．举例说出生命系统的结构层次�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span
	 * style=\"font-family:黑体\">【过程与方法�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．使学生能将�?��得的文字、图表信息表达出来�?<\/span><span
	 * style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\
	 * "font-family:宋体\">．自主学习：运用互联网�?图书、杂志等资料，集和整理生物史学资料�?<\/span>\r\n\t\t\t<\/
	 * p > \ r \ n \ t \ t \ t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:黑体\">【情感�?态度、价值观目标�?\/span><span
	 * style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．激发对生物学学科的学习兴趣�?\/span><span
	 * style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．认同细胞是基本的生命系统�?<\/span><span
	 * style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\
	 * "font-family:宋体\">．体验合作学习的快乐�?\/span>\r\n\t\t\t<\/p>","Answer":null},{"DataID":"91a6ffee-523d-4458-a2a6-7671178caaec","DataName":"【知识与�?���?．理解并掌握生命
	 * \u2026 " , " Q u e s t i o n " : "
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:黑体\">【知识与�?���?\/span><span
	 * style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．理解并掌握生命系统的结构层�?\/span><span
	 * style=\"font-family:宋体\">�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．能判断某个例子处于生命系统结构的地�?\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span><span style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">．了解细胞在生命系统中的地位<\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">4<\/span><span
	 * style
	 * =\"font-family:宋体\">．举例说明种群�?群落的概念�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span
	 * style=\"font-family:黑体\">【过程与方法�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span
	 * style=\"font-family:宋体\">培养与人合作完成任务的能力及归纳总结知识、搜集资料解决问题的能力�?\/span><span
	 * style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:黑体\">【情感�?度与价�?观�?<\/span><span
	 * style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\
	 * "font-family:宋体\">培养科学的学习�?度与学生的协作精神�?<\/span>\r\n\t\t\t<\/p>","Answer":null},{"DataID":"ac56a8e9-f45d-4d88-b305-bd52c6c19b6b","DataName":"知识与技�?．理解细胞是生物体的\u2026","Q
	 * u e s t i o n " : " < d i v style=\"text-align:center\">\r\n\t\t\t\t
	 * <table cellspacing=\"0\" cellpadding=\"0\" style=\"margin-right:auto; margin-left:auto; border:0.75pt solid #000000; border-collapse:collapse\">
	 * \r\n\t\t\t\t\t
	 * <tr>
	 * \r\n\t\t\t\t\t\t
	 * <td style=\"width:106.25pt; border-right-style:solid; border-right-width:0.75pt; border-bottom-style:solid; border-bottom-width:0.75pt; padding-right:5.03pt; padding-left:5.03pt; vertical-align:middle\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\
	 * "font-family:黑体\">知识与技�?\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\
	 * r \ n \ t \ t \ t \ t \ t \ t
	 * <td style=\"width:267.4pt; border-left-style:solid; border-left-width:0.75pt; border-bottom-style:solid; border-bottom-width:0.75pt; padding-right:5.03pt; padding-left:5.03pt; vertical-align:top\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New
	 * Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．理解细胞是生物体的结构和功能的基本单位<\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New
	 * Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．举例说明生命活动建立在细胞的基�?���?\/span><span style=\
	 * "font-family:宋体\">�?\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New
	 * Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">．举例说明生命系统的结构层次<\/span><span style=\
	 * "font-family:宋体\">�?\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\r\n\t\t\t\t\t<\/tr
	 * > \ r \ n \ t \ t \ t \ t \ t
	 * <tr>
	 * \r\n\t\t\t\t\t\t
	 * <td style=\"width:106.25pt; border-top-style:solid; border-top-width:0.75pt; border-right-style:solid; border-right-width:0.75pt; border-bottom-style:solid; border-bottom-width:0.75pt; padding-right:5.03pt; padding-left:5.03pt; vertical-align:middle\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\
	 * "font-family:黑体\">过程与方�?\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\
	 * r \ n \ t \ t \ t \ t \ t \ t
	 * <td style=\"width:267.4pt; border-top-style:solid; border-top-width:0.75pt; border-left-style:solid; border-left-width:0.75pt; border-bottom-style:solid; border-bottom-width:0.75pt; padding-right:5.03pt; padding-left:5.03pt; vertical-align:top\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New
	 * Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．初步学会分析生物学知识素材<\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New
	 * Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．尝试将�?��得的文字、图表信息表达出�?\/span><span style=\
	 * "font-family:宋体\">�?\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\r\n\t\t\t\t\t<\/tr
	 * > \ r \ n \ t \ t \ t \ t \ t
	 * <tr>
	 * \r\n\t\t\t\t\t\t
	 * <td style=\"width:106.25pt; border-top-style:solid; border-top-width:0.75pt; border-right-style:solid; border-right-width:0.75pt; padding-right:5.03pt; padding-left:5.03pt; vertical-align:middle\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:黑体\">情感态度与价值观<\/span><span
	 * style=\
	 * "font-family:黑体\">目标<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\
	 * r \ n \ t \ t \ t \ t \ t \ t
	 * <td style=\"width:267.4pt; border-top-style:solid; border-top-width:0.75pt; border-left-style:solid; border-left-width:0.75pt; padding-right:5.03pt; padding-left:5.03pt; vertical-align:top\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New
	 * Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．激发学生对生物学学科的学习兴趣<\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">&#xa0;<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New
	 * Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．加强师生交流，让学生体验合作学习的快乐<\/span><span style=\
	 * "font-family:宋体\">�?\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\r\n\t\t\t\t\t<\/tr>\r\n\t\t\t\t<\/table>\r\n\t\t\t<\
	 * / d i v > \ r \ n \ t \ t \ t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; font-size:12pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman';
	 * -aw-import:ignore\
	 * ">&#xa0;<\/span>\r\n\t\t\t<\/p>","Answer":null},{"DataID":"4e86046c-92e6-41f8-ad8c-cd4d74d7991a","DataName":"【知识体系�
	 * ? " , " Q u e s t i o n " : "
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<img src=\
	 * "data:image/png;base64,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
	 * A E l F T k S u Q m C C \ " width=\"611\" height=\"359\" alt=\"\"
	 * style=\"-aw-left-pos:0pt; -aw-rel-hpos:character; -aw-rel-vpos:line;
	 * -aw-top-pos:0pt; -aw-wrap-type:inline\"
	 * />\r\n\t\t\t<\/p>","Answer":null},{"DataID":"89267bfc
	 * -c64e-45c4-8f44-589c6ab25fd6","DataName":"【知识体系�?","Question":"
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:52.5pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<img src=\
	 * "data:image/png;base64,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
	 * R U 5 E r k J g g g = = \ " width=\"498\" height=\"349\" alt=\"\"
	 * style=\"-aw-left-pos:0pt; -aw-rel-hpos:character; -aw-rel-vpos:line;
	 * -aw-top-pos:0pt; -aw-wrap-type:inline\" /><span
	 * style=\"font-family:'Times New Roman'; -aw-
	 * import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;
	 * <\/span>\r\n\t\t\t<\/p>","Answer":null},{"DataID":"636d
	 * 6ed3-d4e2-4a9e-83af-96866e3e4167","DataName":"【知识体系�?","Question":"
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<img src=\
	 * "data:image/png;base64,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
	 * R U 5 E r k J g g g = = \ " width=\"575\" height=\"341\" alt=\"\"
	 * style=\"-aw-left-pos:0pt; -aw-rel-hpos:character; -aw-rel-vpos:line;
	 * -aw-top-pos:0pt; -aw-wrap-type:inline\" /><span
	 * style=\"font-family:'Times New Roman';
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;
	 * <\/span>\r\n\t\t\t<\/p>","Answer":null},{"
	 * DataID":"2c6ea065-6038-4b34-8c21
	 * -381ebaeefde2","DataName":"�??生命活动离不�?���?．各类生\u2026","Question":"
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体;
	 * font-weight:bold\">�??生命活动离不�?���?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．各类生物的生命活动<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">）病�?\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0; <\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:宋体\">单细胞生�?\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0; <\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">）多细胞生物<\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0; <\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．生命活动的基础 <\/span>\r\n\t\t\t<\/p>\r\n\t\t\t<div
	 * style=\"text-align:center\">\r\n\t\t\t\t
	 * <table cellspacing=\"0\" cellpadding=\"0\" style=\"margin-right:auto; margin-left:auto; border:1pt solid #000000; border-collapse:collapse\">
	 * \r\n\t\t\t\t\t
	 * <tr style=\"height:23.4pt\">
	 * \r\n\t\t\t\t\t\t
	 * <td style=\"width:127.8pt; border-right-style:solid; border-right-width:1pt; border-bottom-style:solid; border-bottom-width:1pt; padding-top:0.25pt; padding-right:4.9pt; padding-left:4.9pt; vertical-align:bottom\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\
	 * "font-family:宋体\">生命活动<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\
	 * r \ n \ t \ t \ t \ t \ t \ t
	 * <td style=\"width:175.6pt; border-left-style:solid; border-left-width:1pt; border-bottom-style:solid; border-bottom-width:1pt; padding-top:0.25pt; padding-right:4.9pt; padding-left:4.9pt; vertical-align:bottom\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\
	 * "font-family:宋体\">基础<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\r\n\t\t\t\t\t<\/tr
	 * > \ r \ n \ t \ t \ t \ t \ t
	 * <tr style=\"height:45.2pt\">
	 * \r\n\t\t\t\t\t\t
	 * <td style=\"width:127.8pt; border-top-style:solid; border-top-width:1pt; border-right-style:solid; border-right-width:1pt; border-bottom-style:solid; border-bottom-width:1pt; padding-top:0.25pt; padding-right:4.9pt; padding-left:4.9pt; vertical-align:bottom\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\
	 * "font-family:宋体\">生物与环境之�?\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t\
	 * t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\
	 * "font-family:宋体\">物质和能量的交换<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\
	 * r \ n \ t \ t \ t \ t \ t \ t
	 * <td style=\"width:175.6pt; border-top-style:solid; border-top-width:1pt; border-left-style:solid; border-left-width:1pt; border-bottom-style:solid; border-bottom-width:1pt; padding-top:0.25pt; padding-right:4.9pt; padding-left:4.9pt; vertical-align:bottom\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New
	 * Roman'\">______<\/span><span style=\"font-family:'Times New Roman';
	 * text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;
	 * <\/span><span style=\"font-family:'Times New Roman
	 * '\">___<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\r\n\t\t\t\t\t<\/tr
	 * > \ r \ n \ t \ t \ t \ t \ t
	 * <tr style=\"height:23.4pt\">
	 * \r\n\t\t\t\t\t\t
	 * <td style=\"width:127.8pt; border-top-style:solid; border-top-width:1pt; border-right-style:solid; border-right-width:1pt; border-bottom-style:solid; border-bottom-width:1pt; padding-top:0.25pt; padding-right:4.9pt; padding-left:4.9pt; vertical-align:bottom\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\
	 * "font-family:宋体\">生长发育<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\
	 * r \ n \ t \ t \ t \ t \ t \ t
	 * <td style=\"width:175.6pt; border-top-style:solid; border-top-width:1pt; border-left-style:solid; border-left-width:1pt; border-bottom-style:solid; border-bottom-width:1pt; padding-top:0.25pt; padding-right:4.9pt; padding-left:4.9pt; vertical-align:bottom\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New Roman
	 * '\">_______________<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\r\n\t\t\t\t\t<\/tr
	 * > \ r \ n \ t \ t \ t \ t \ t
	 * <tr style=\"height:24.35pt\">
	 * \r\n\t\t\t\t\t\t
	 * <td style=\"width:127.8pt; border-top-style:solid; border-top-width:1pt; border-right-style:solid; border-right-width:1pt; padding-top:0.25pt; padding-right:4.9pt; padding-left:4.9pt; vertical-align:bottom\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\
	 * "font-family:宋体\">遗传与变�?\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\
	 * r \ n \ t \ t \ t \ t \ t \ t
	 * <td style=\"width:175.6pt; border-top-style:solid; border-top-width:1pt; border-left-style:solid; border-left-width:1pt; padding-top:0.25pt; padding-right:4.9pt; padding-left:4.9pt; vertical-align:bottom\">
	 * \r\n\t\t\t\t\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t\t\t\t\t<span style=\"font-family:'Times New Roman
	 * '\">_________________<\/span>\r\n\t\t\t\t\t\t\t<\/p>\r\n\t\t\t\t\t\t<\/td>\r\n\t\t\t\t\t<\/tr>\r\n\t\t\t\t<\/table>\r\n\t\t\t<\
	 * / d i v > \ r \ n \ t \ t \ t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体;
	 * font-weight:bold\">二�?生命系统的结构层�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．几个概�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">）系统：指彼此间 <\/span><span style=\"font-family:宋体;
	 * text-decoration:underline; -aw-import:spaces\
	 * ">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa
	 * 0 ; & # x a 0 ; & # x a 0 ; <\/span><span style=\
	 * "font-family:宋体\">、相互依赖的组分有规律地结合而形成的整体�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">）种群：在一定的区域内，同种生物的所�?\/span><span
	 * style=\"font-family:宋体; text-decoration:underline; -aw-import:spaces\
	 * ">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0
	 * ; & # x a 0 ; <\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0;<\/span><span
	 * style=\"font-family:宋体\">�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">）群落：在一定的区域内，�?���?\/span><span
	 * style=\"font-family:'Times New Roman'\">_______<\/span><span
	 * style=\"font-family:宋体\">组成�?��群落�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">4<\/span><span
	 * style=\"font-family:宋体\">）生态系统：生物群落和它�?\/span><span
	 * style=\"font-family:'Times New Roman'\">_____________<\/span><span
	 * style=\"font-family:宋体\">相互作用形成生�?系统�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-left:21pt; margin-bottom:0pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2.<\/span><span
	 * style=\"font-family:宋体\">结构层次：细�?\/span><span style=\"font-family:'Times
	 * New Roman'\">→______�?\/span><span
	 * style=\"font-family:宋体\">器官<\/span><span style=\"font-family:'Times New
	 * Roman'\">�?\/span><span style=\"font-family:宋体\">系统<\/span><span
	 * style=\"font-family:'Times New Roman'\">�?\/span><span
	 * style=\"font-family:宋体\">个体<\/span><span style=\"font-family:'Times New
	 * Roman'\">→________�?\/span><span style=\"font-family:宋体\">群落<\/span><span
	 * style=\"font-family:'Times New Roman'\">→____________�?\/span><span
	 * style=\"font-family:宋体\">生物圈�? <\/span>\r\n\t\t\t<\/p>","Answer":"
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span
	 * style=\"font-family:黑体\">【答案�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�??<\/span><span
	 * style=\"font-family:宋体\">生命活动离不�?���?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．（<\/span><span style=\"font-family:'Times New
	 * Roman'\">1<\/span><span style=\"font-family:宋体\">）细�?\/span><span
	 * style=\"font-family:宋体; -aw-import:spaces\">&#xa0; <\/span><span
	 * style=\"font-family:宋体\">活细胞（<\/span><span style=\"font-family:'Times New
	 * Roman'\">2<\/span><span style=\"font-family:宋体\">）单个细胞（<\/span><span
	 * style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">）分化的细胞<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．细胞代�?\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">细胞增殖、分�?\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">细胞内基�?\/span><span
	 * style=\"font-family:宋体\">传�?和变�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">二�?<\/span><span
	 * style=\"font-family:宋体\">生命<\/span><span
	 * style=\"font-family:宋体\">系统<\/span><span
	 * style=\"font-family:宋体\">的结构层�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．（<\/span><span style=\"font-family:'Times New
	 * Roman'\">1<\/span><span style=\"font-family:宋体\">）相互作�?\/span><span
	 * style=\"font-family:宋体; -aw-import:spaces\">&#xa0; <\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">2<\/span><span style=\"font-family:宋体\">）个体�?称（<\/span><span
	 * style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">）种�?\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0; <\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">4<\/span><span
	 * style=\"font-family:宋体\">）无机环�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．组�?\/span><span style=\"font-family:宋体\">
	 * <\/span><span style=\"font-family:宋体; -aw-import:spaces\">&#xa0;
	 * <\/span><span style=\"font-family:宋体\">种群 <\/span><span
	 * style=\"font-family:宋体; -aw-import:spaces\">&#xa0; <\/span><span style=\
	 * "font-family:宋体\">生�?系统<\/span>\r\n\t\t\t<\/p>"},{"DataID":"69c930d9-7203-40a8-bd9b-5978a9ec3ffe","DataName":"�??生命活动离不�?���?．细胞是
	 * \u2026 " , " Q u e s t i o n " : "
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体;
	 * font-weight:bold\">�??生命活动离不�?���?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．细胞是生物体的<\/span><span style=\"font-family:宋体;
	 * text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">单位�?\/span><span style=\"font-family:宋体;
	 * text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;
	 * <\/span><span
	 * style=\"font-family:宋体\">单位�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:宋体;
	 * text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">是没有细胞结构的生物，但必须依赖<\/span><span
	 * style=\"font-family:宋体; text-decoration:underline; -aw-import:spaces\
	 * ">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0
	 * ; <\/span><span
	 * style=\"font-family:宋体\">才能生活�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">．单细胞生物的生命活动以<\/span><span
	 * style=\"font-family:宋体; text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;
	 * <\/span><span
	 * style=\"font-family:宋体\">为基本单位�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">4<\/span><span
	 * style=\"font-family:宋体\">．多细胞生物依赖<\/span><span style=\"font-family:宋体;
	 * text-decoration:underline; -aw-import:spaces\
	 * ">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa
	 * 0 ; & # x a 0 ; & # x a 0 ; <\/span><span
	 * style=\"font-family:宋体\">密切合作，共同完成一系列的生命活动�?<\/span><span
	 * style=\"font-family:宋体; -aw-import:spaces\">&#xa0;&#xa0;&#xa0;
	 * <\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体;
	 * font-weight:bold\">二�?生命系统的结构层次的含义�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">1<\/span><span style=\
	 * "font-family:宋体\">）细胞：生物体结构和功能的基本单位�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">）组织：由许�?\/span><span style=\"font-family:宋体;
	 * text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\
	 * "font-family:宋体\">、结构和功能相同的细胞联合在�?��形成的细胞群�?\/span>\r\n\t\t\t<\/p>\r\n\t\
	 * t \ t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">）器官：指由不同�?\/span><span style=\"font-family:宋体;
	 * text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\
	 * "font-family:宋体\">按照�?��的次序联合起来，形成的具有一定生理功能的结构�?\/span>\r\n\t\t\t<\/p>\r\
	 * n \ t \ t \ t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">4<\/span><span
	 * style=\"font-family:宋体\">）系统：由能够共同完成一种或几种生理功能的多�?\/span><span
	 * style=\"font-family:宋体; text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">，按照一定的次序构成的�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">5<\/span><span style=\
	 * "font-family:宋体\">）生物个体：高等植物（如裸子植物和被子植物）由六种器官构成个体；高等动物（如脊椎动物）由各个系统构成个体�?<\/span>\r\n\t\t\t
	 * < \ / p > \ r \ n \ t \ t \ t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">6<\/span><span
	 * style=\"font-family:宋体\">）种群：指在�?��的空间和时间内的<\/span><span
	 * style=\"font-family:宋体; text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">个体的�?和�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">7<\/span><span
	 * style=\"font-family:宋体\">）生物群落：指在�?��自然区域内，相互之间有着直接或间接关系的<\/span><span
	 * style=\"font-family:宋体; text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">的�?和，�?��群落�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">8<\/span><span
	 * style=\"font-family:宋体\">）生态系统：�?\/span><span style=\"font-family:宋体;
	 * text-decoration:underline;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0;&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\
	 * "font-family:宋体\">与它的无机环境相互作用�?形成的统�?��体�?<\/span>\r\n\t\t\t<\/p>\r\n\t\t
	 * \ t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:'Times New Roman'\">9<\/span><span style=\
	 * "font-family:宋体\">）生物圈：指地球上的全部生物和它们的无机环境的�?和�?它包括海平面上�?下各�?\/span><span
	 * style=\"font-family:'Times New Roman'\">10 km<\/span><span style=\
	 * "font-family:宋体\">的空间圈层，包含了大气圈的底部�?水圈的全部和岩石圈的上部，是地球上最大的生�?系统�?\/span>\r\n\t\t\t<\
	 * / p > " , " A n s w e r " : "
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; widows:0; orphans:0; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:黑体\">【答案�?
	 * <\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">�??<\/span><span
	 * style=\"font-family:'Times New Roman'\">1<\/span><span
	 * style=\"font-family:宋体\">．结�?\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">功能<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">．病�?\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0;&#xa0;&#xa0; <\/span><span
	 * style=\"font-family:宋体\">活细�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">3<\/span><span
	 * style=\"font-family:宋体\">．细�?\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:'Times New Roman'\">4<\/span><span
	 * style=\"font-family:宋体\">．各种分化的细胞<\/span>\r\n\t\t\t<\/p>\r\n\t\t\t
	 * <p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:21pt; text-align:justify; line-height:150%; font-size:10.5pt\">
	 * \r\n\t\t\t\t<span style=\"font-family:宋体\">二�?�?\/span><span
	 * style=\"font-family:'Times New Roman'\">2<\/span><span
	 * style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:宋体\">形�?相似<\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0; <\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">3<\/span><span style=\"font-family:宋体\">）组�?\/span><span
	 * style=\"font-family:宋体; -aw-import:spaces\">&#xa0; <\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">4<\/span><span style=\"font-family:宋体\">）器�?�?\/span><span
	 * style=\"font-family:'Times New Roman'\">6<\/span><span
	 * style=\"font-family:宋体\">）同种生�?\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0; <\/span><span
	 * style=\"font-family:宋体\">�?\/span><span style=\"font-family:'Times New
	 * Roman'\">7<\/span><span style=\"font-family:宋体\">�?\/span><span
	 * style=\"font-family:宋体\">种群<\/span><span style=\"font-family:宋体\">
	 * �?\/span><span style=\"font-family:'Times New Roman'\">8<\/span><span
	 * style=\"font-family:宋体\">�?生物群落<\/span><span style=\"font-family:宋体;
	 * -aw-import:spaces\">&#xa0; <\/span>\r\n\t\t\t<\/p>"}]} count : 9
	 */

	private int code;
	private String msg;
	private DataBean data;
	private int count;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public static class DataBean {
		private List<CategoryListBean> CategoryList;
		private List<DataListBean> DataList;

		public List<CategoryListBean> getCategoryList() {
			return CategoryList;
		}

		public void setCategoryList(List<CategoryListBean> CategoryList) {
			this.CategoryList = CategoryList;
		}

		public List<DataListBean> getDataList() {
			return DataList;
		}

		public void setDataList(List<DataListBean> DataList) {
			this.DataList = DataList;
		}

		/**
		 * 目录
		 * 
		 * @author chenhui
		 * 
		 */
		public static class CategoryListBean {
			/**
			 * DataID : 4 DataPID : 0 DataName : 课前预习
			 */

			private String DataID;
			private String DataPID;
			private String DataName;

			public String getDataID() {
				return DataID;
			}

			public void setDataID(String DataID) {
				this.DataID = DataID;
			}

			public String getDataPID() {
				return DataPID;
			}

			public void setDataPID(String DataPID) {
				this.DataPID = DataPID;
			}

			public String getDataName() {
				return DataName;
			}

			public void setDataName(String DataName) {
				this.DataName = DataName;
			}
		}

		public static class DataListBean {
			/**
			 * DataID : e36f2815-23f8-44bf-960f-e768eaeea212 DataName : 形式�?
			 * Question : null Answer : null
			 */

			private String DataID;
			private String DataName;
			private String Question;
			private String Answer;

			private boolean isShowAA = false;// 是否显示答案与解析，默认为不显示
			private boolean hasJoinInPreview = false;// 是否加入预览，默认为不加入

			public String getDataID() {
				return DataID;
			}

			public void setDataID(String DataID) {
				if (DataID != null) {
					this.DataID = DataID;
				} else {
					this.DataID = "";
				}
			}

			public String getDataName() {
				return DataName;
			}

			public void setDataName(String DataName) {
				if (DataName != null) {
					this.DataName = DataName;
				} else {
					this.DataName = "";
				}

			}

			public String getQuestion() {
				return Question;
			}

			public void setQuestion(String Question) {
				if (Question != null) {
					this.Question = Question;
				} else {
					this.Question = "";
				}

			}

			public String getAnswer() {
				return Answer;
			}

			public void setAnswer(String Answer) {
				if (Answer != null) {
					this.Answer = Answer;
				} else {
					this.Answer = "";
				}
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

		}
	}
}
