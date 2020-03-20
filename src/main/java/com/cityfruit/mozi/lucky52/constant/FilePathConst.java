package com.cityfruit.mozi.lucky52.constant;

/**
 * @author tianyuheng
 * @date 2020/02/10
 */
public final class FilePathConst {

    /**
     * json 文件绝对路径
     */
    private static final String JSON_FILE_PATH = System.getenv("MZ_JSON_FILE_PATH");

    public static final String RECORDS_JSON_FILE = JSON_FILE_PATH + "/records.json";

    public static final String DAY_SCORE_JSON_FILE = JSON_FILE_PATH + "/score_%s.json";

    public static final String MEMBERS_JSON_FILE = JSON_FILE_PATH + "/members.json";

    public static final String ZOMBIE_JSON_FILE = JSON_FILE_PATH + "/zombie_%s.json";

    public static final String OPERATIONS_JSON_FILE = JSON_FILE_PATH + "/operations.json";

}
