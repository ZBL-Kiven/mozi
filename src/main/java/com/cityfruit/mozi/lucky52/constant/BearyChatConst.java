package com.cityfruit.mozi.lucky52.constant;

import com.cityfruit.mozi.lucky52.entity.UserInfo;
import com.cityfruit.mozi.lucky52.entity.Member;
import com.cityfruit.mozi.lucky52.entity.TaskStatus;
import com.cityfruit.mozi.lucky52.util.UserUtil;


/**
 * 返回信息常量
 *
 * @author tianyuheng
 * @date 2020/02/13
 */
public class BearyChatConst {

    /**
     * 初始化返回内容
     */
    public static final String SUCCESS_MESSAGE = "Success";

    /**
     * 开宝箱群组
     */
    public static final String OPEN_TREASURE_BOX_GROUP = "推送测试";

    private static final String PORTAL = "\n---\n [ 传送门：我有建议提给「斯巴达幸运 52」](http://cityfruit-doc.i-mocca.com/web/#/27?page_id=751)";

    //统计 Bug 数量的 formart 内容
    private static final String Bug_Formart_Mark = "> 有效创建 S1 级 bug %d 个 | S2 级 %d 个 | S3 级 %d 个 | S4 级 %d 个\n" +
            "> 有效关闭 S1 级 bug %d 个 | S2 级 %d 个 | S3 级 %d 个 | S4 级 %d 个" + PORTAL;
    // 当自己的 QP 总分是发送请求时所有用户第一名时
    private static final String QP_SCORE_FIRST = "@%s , 您当前的 QP 总得分为 %.1f , 已击败 %s 的幸运 52 用户 , 是今日第一名 ! \n" + Bug_Formart_Mark;
    // 当自己的 QP 总分不是发送请求时所有用户第一名时
    private static final String QP_SCORE_NOT_FIRST = "@%s , 您当前的 QP 总得分为 %.1f , 距第一名还差 %.1f 分 ！ \n" + Bug_Formart_Mark;
    // 当所有用户的 QP 得分都是 0；或所有用户的 QP 得分不是 0，且分数相等
    private static final String QP_SCROE_EQUEALS = "@%s , 您当前的 QP 总得分为 %.1f , 和其他用户在同一起跑线 ! \n" + Bug_Formart_Mark;


    public static int TYPE_QP_SCORE_FIRST = 11;
    public static int TYPE_QP_SCORE_NOT_FIRST = 12;
    public static int TYPE_QP_SCROE_EQUEALS = 13;

    /**
     * 无效开奖群
     */
    public static final String INVALID_GROUP = "您只能在 `#" + OPEN_TREASURE_BOX_GROUP + "` 群进行开奖" + PORTAL;

    public static String scheduleQualityPoint(Member member) {
        int addOpen = 0;
        for (String severity : member.getOpen().keySet()
        ) {
            addOpen += member.getOpen().getOrDefault(severity, 0);
        }

        int addClose = 0;
        for (String severity : member.getClose().keySet()
        ) {
            addClose += member.getClose().getOrDefault(severity, 0);
        }
        return member.getName() + "的 QP 得分为 " + member.getQualityPoint() + "（有效创建 " + addOpen + " 个 bug，有效关闭 " + addClose + " 个 bug）";
    }

    public static String cannotOpenBoxNotice(String bearyChatId) {
        return "@" + bearyChatId + "，您暂未获得今日开宝箱资格，加油！今天还未结束！" + PORTAL;
    }

    public static String openBoxNotice(String bearyChatId, String probabilityName) {
        // 构造推送消息体
        return "@" + bearyChatId + "，您已获得今日开宝箱资格，" +
                probabilityName +
                // 推送消息体后缀
                "掉落`品质碎片`，每 20 个品质碎片可以兑换 1 个品质星\n" +
                "您可以选择现在开奖，或继续工作一会再开，但请注意一天之内只能开一次\n" +
                "---\n" +
                "开奖方式及规则：\n" +
                "- 输入 `@eva 我要中奖` 进行开奖\n" +
                "- 必须在 #" + OPEN_TREASURE_BOX_GROUP + " 群进行开奖，在其他群组或个人对话中进行开奖不算" + PORTAL;
    }

    public static String noOpeningRights(String bearyChatId) {
        return "@" + bearyChatId + "，对不起，您暂时没有开宝箱资格" + PORTAL;
    }

    public static String opened(String bearyChatId) {
        return "@" + bearyChatId + "，您今天已开过宝箱，一天只能开一次宝箱，请明天再来尝试！" + PORTAL;
    }

    public static String openTreasureBoxResult(String bearyChatId, int qualityFragment) {
        return (qualityFragment == 0)
                ?
                ("@" + bearyChatId + "，很遗憾没有抽中，明天再来～" + PORTAL)
                : ("@" + bearyChatId + "，恭喜您，您已获得 " + qualityFragment + " 个 `品质星碎片`" + PORTAL);
    }

    public static String noRights(String bearyChatId) {
        return "@" + bearyChatId + "，只有 PO、QA 用户可以使用此功能" + PORTAL;
    }

    /**
     * 需要特殊任务 总分数
     *
     * @param member             当前用户
     * @param firstScoreMember   所有用户中得分第一的用户
     * @param memberQualityPoint 当前用户的得分
     * @param contentType        当前用户需要匹配的提示信息类型
     * @param perOfWins          当前用户如果是第一名所击败用户的百分比
     * @param scoreDiff          当前用户不是第一名，和第一名之间相差的分数
     * @return QP 得分推送内容
     */
    public static String getQualityPoint(Member member, Member firstScoreMember, float memberQualityPoint, int contentType, String perOfWins, float scoreDiff) {
        int addOpen = 0;
        for (String severity : member.getOpen().keySet()
        ) {
            addOpen += member.getOpen().getOrDefault(severity, 0);
        }
        int addClose = 0;
        for (String severity : member.getClose().keySet()
        ) {
            addClose += member.getClose().getOrDefault(severity, 0);
        }

        String bearyChatId = member.getBearyChatId();

        int openS1Count = member.getOpen().getOrDefault("1", 0);
        int openS2Count = member.getOpen().getOrDefault("2", 0);
        int openS3Count = member.getOpen().getOrDefault("3", 0);
        int openS4Count = member.getOpen().getOrDefault("4", 0);

        int closeS1Count = member.getClose().getOrDefault("1", 0);
        int closeS2Count = member.getClose().getOrDefault("2", 0);
        int closeS3Count = member.getClose().getOrDefault("3", 0);
        int closeS4Count = member.getClose().getOrDefault("4", 0);

        String originContent = "@%s，您当前的 QP 总得分为 %.1f（有效创建 %d 个 bug，有效关闭 %d 个 bug）\n" +
                "> 有效创建 S1 级 bug %d 个 | S2 级 %d 个 | S3 级 %d 个 | S4 级 %d 个\n" +
                "> 有效关闭 S1 级 bug %d 个 | S2 级 %d 个 | S3 级 %d 个 | S4 级 %d 个" + PORTAL;
        String content;

        if (contentType == TYPE_QP_SCORE_FIRST) {  //第一名
            content = QP_SCORE_FIRST;
            return String.format(content, bearyChatId, memberQualityPoint, perOfWins,
                    openS1Count, openS2Count, openS3Count, openS4Count,
                    closeS1Count, closeS2Count, closeS3Count, closeS4Count);
        } else if (contentType == TYPE_QP_SCORE_NOT_FIRST) { //非第一名
            content = QP_SCORE_NOT_FIRST;
            /*int firstOpenS1Count = firstScoreMember.getOpen().getOrDefault("1", 0);
            int firstOpenS2Count = firstScoreMember.getOpen().getOrDefault("2", 0);
            int firstOpenS3Count = firstScoreMember.getOpen().getOrDefault("3", 0);
            int firstOpenS4Count = firstScoreMember.getOpen().getOrDefault("4", 0);

            int firstCloseS1Count = firstScoreMember.getClose().getOrDefault("1", 0);
            int firstCloseS2Count = firstScoreMember.getClose().getOrDefault("2", 0);
            int firstCloseS3Count = firstScoreMember.getClose().getOrDefault("3", 0);
            int firstCloseS4Count = firstScoreMember.getClose().getOrDefault("4", 0);
            int diffOpenCount = (firstOpenS1Count + firstOpenS2Count + firstOpenS3Count + firstOpenS4Count) - (openS1Count + openS2Count + openS3Count + openS4Count);
            int diffCloseCount = (firstCloseS1Count + firstCloseS2Count + firstCloseS3Count + firstCloseS4Count) - (closeS1Count + closeS2Count + closeS3Count + closeS4Count);
            if (diffOpenCount < 0) {
                diffOpenCount = 0;
            }
            if (diffCloseCount < 0) {
                diffCloseCount = 0;
            }*/
            return String.format(content, bearyChatId, memberQualityPoint, scoreDiff, openS1Count, openS2Count, openS3Count, openS4Count,
                    closeS1Count, closeS2Count, closeS3Count, closeS4Count);
        } else if (contentType == TYPE_QP_SCROE_EQUEALS) {  //同一起跑线
            content = QP_SCROE_EQUEALS;
            return String.format(content, bearyChatId, memberQualityPoint, openS1Count, openS2Count, openS3Count, openS4Count,
                    closeS1Count, closeS2Count, closeS3Count, closeS4Count);
        } else {
            content = originContent;
            return String.format(content,
                    bearyChatId,
                    memberQualityPoint,
                    addOpen, addClose,
                    openS1Count, openS2Count, openS3Count, openS4Count,
                    closeS1Count, closeS2Count, closeS3Count, closeS4Count
            );
        }

    }

    /**
     * 计算总 QP
     *
     * @param member 用户
     * @return 总 QP
     */
    public static float calculateQualityPoint(Member member) {

        //QP = (新建 S1 级 Bug 数)x16 +
        // (新建 S2 级 Bug 数)x8 +
        // (新建 S3 级 Bug 数)x1 +
        // (新建 S4 级 Bug 数)x0.5 +
        // (关闭 S1 级 Bug 数)x20 +
        // (关闭 S2 级 Bug 数)x8 +
        // (关闭 S3 级 Bug 数)x1.5 +
        // (关闭 S4 级 Bug 数)x2

        float qp = (float) (
                member.getOpen().getOrDefault("1", 0) * 16 +
                        member.getOpen().getOrDefault("2", 0) * 8 +
                        member.getOpen().getOrDefault("3", 0) +
                        member.getOpen().getOrDefault("4", 0) * 0.5 +
                        member.getClose().getOrDefault("1", 0) * 20 +
                        member.getClose().getOrDefault("2", 0) * 8 +
                        member.getClose().getOrDefault("3", 0) * 1.5 +
                        member.getClose().getOrDefault("4", 0) * 2);


        return calculateSpecialQualityPoint(member.getStatus()) + qp;
    }

    /**
     * 计算 特殊任务 QP
     *
     * @param tasks 任务
     * @return 特殊任务总分
     */
    private static int calculateSpecialQualityPoint(TaskStatus tasks) {
        int sum = 0;
        sum += tasks.isTaskSuccess1() ? 1 : 0;
        sum += tasks.isTaskSuccess2() ? TaskConst.TASK_NAME_2_EXTRA_SCORE : 0;
        sum += tasks.isTaskSuccess3() ? TaskConst.TASK_NAME_3_EXTRA_SCORE : 0;
        sum += tasks.isTaskSuccess4() ? TaskConst.TASK_NAME_4_EXTRA_SCORE : 0;
        sum += tasks.isTaskSuccess5() ? TaskConst.TASK_NAME_5_EXTRA_SCORE : 0;
        sum += tasks.isTaskSuccess6() ? 2 : 0;
        sum += tasks.isTaskSuccess7() ? 2 : 0;
        sum += tasks.isTaskSuccess8() ? 5 : 0;
        sum += tasks.isTaskSuccess9() ? 5 : 0;
        sum += tasks.isTaskSuccess10() ? 2 : 0;
        sum += tasks.isTaskSuccess11() ? TaskConst.TASK_NAME_11_EXTRA_SCORE : 0;
        return sum;
    }

    public static String getQualityFragment(Member member) {
        return UserUtil.listUer(false, userInfos -> {
            String userName = member.getName();
            for (UserInfo userInfo : userInfos) {
                if (userName.equals(userInfo.getName())) {
                    return "@" + member.getBearyChatId() + "，您的碎片数还有 " + userInfo.getQualityFragment() + " 个（每 20 个碎片可兑换 1 颗品质星）" + PORTAL;
                }
            }
            return "";
        });
    }

    public static String getPushBcByFinishedTask(String bcId, String taskName, int qp, double sumQp) {
        String content = "@%s，恭喜您完成 `%s`，获得 QP +%d（当前总 QP 总分为 %.1f）" + PORTAL;
        return String.format(content, bcId, taskName, qp, sumQp);
    }
}
