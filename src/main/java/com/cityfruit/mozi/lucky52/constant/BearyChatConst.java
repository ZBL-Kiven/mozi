package com.cityfruit.mozi.lucky52.constant;

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

    /**
     * 无效开奖群
     */
    public static final String INVALID_GROUP = "您只能在 `#" + OPEN_TREASURE_BOX_GROUP + "` 群进行开奖\n" +
            "---\n" +
            "[传送门：我有建议提给「斯巴达幸运 52」](http://cityfruit-doc.i-mocca.com/web/#/27?page_id=751)";

    public static String cannotOpenBoxNotice(String bearyChatId) {
        return "@" + bearyChatId + "，您暂未获得今日开宝箱资格，加油！今天还未结束！\n" +
                "---\n" +
                "[传送门：我有建议提给「斯巴达幸运 52」](http://cityfruit-doc.i-mocca.com/web/#/27?page_id=751)";
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
                "- 输入 `@eva 开宝箱` 进行开奖\n" +
                "- 必须在 #" + OPEN_TREASURE_BOX_GROUP + " 群进行开奖，在其他群组或个人对话中进行开奖不算\n" +
                "---\n" +
                "[传送门：我有建议提给「斯巴达幸运 52」](http://cityfruit-doc.i-mocca.com/web/#/27?page_id=751)";
    }

    public static String noOpeningRights(String bearyChatId) {
        return "@" + bearyChatId + "，对不起，您暂时没有开宝箱资格\n" +
                "---\n" +
                "[传送门：我有建议提给「斯巴达幸运 52」](http://cityfruit-doc.i-mocca.com/web/#/27?page_id=751)";
    }

    public static String opened(String bearyChatId) {
        return "@" + bearyChatId + "，您今天已开过宝箱，一天只能开一次宝箱，请明天再来尝试！\n" +
                "---\n" +
                "[传送门：我有建议提给「斯巴达幸运 52」](http://cityfruit-doc.i-mocca.com/web/#/27?page_id=751)";
    }

    public static String openTreasureBoxResult(String bearyChatId, int qualityFragment) {
        return (qualityFragment == 0)
                ? "@" + bearyChatId + "，很遗憾没有抽中，明天再来～\n" +
                "---\n" +
                "[传送门：我有建议提给「斯巴达幸运 52」](http://cityfruit-doc.i-mocca.com/web/#/27?page_id=751)"
                : "@" + bearyChatId + "，恭喜您，您已获得 " + qualityFragment + " 个 `品质碎片`\n" +
                "---\n" +
                "[传送门：我有建议提给「斯巴达幸运 52」](http://cityfruit-doc.i-mocca.com/web/#/27?page_id=751)";
    }

}
