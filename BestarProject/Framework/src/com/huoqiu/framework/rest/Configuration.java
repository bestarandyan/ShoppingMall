package com.huoqiu.framework.rest;

public enum Configuration {
    /***************FYB Configuration**********************/
    // TEST("http", "192.168.1.244", 8889, "/rest"),
    // RELEASE("http", "agentapp.iwjw.com", 80, "/IWAgentSOA/agentapp"),
    // TEST("http", "userappbeta.iwjw.com", 8101, "/ihouse"),
    // RELEASE("http", "fyb365.com", 80, "/rest"),

    TEST("http", "192.168.1.14", 8123, "/contractAppserver"),
//     TEST("http", "121.40.129.114", 8125, "/contractAppserver"),
//    TEST("http", "121.40.129.114", 8103, "/rest"),
//    RELEASE("http", "contractappsrv.iwjwagent.com", 80, ""),
    RELEASE("http", "contractappsrv.iwjwagent.com", 80, ""),

//    TEST("http", "192.168.1.14", 8103, "/rest"),RELEASE("http", "fyb365.com", 80, "/rest"),



    /***************IW Configuration**********************/
    IWJW_RELEASE("http", "userapp.iwjw.com", 80, "/ihouse"),
    IWJW_BETA("http", "userappbeta.iwjw.com", 8101, "/ihouse"),
    IWJW_TEST("http", "192.168.1.15", 8101, "/ihouse"),
    IWJW_TEST2("http", "192.168.1.213", 8080, "/ihouse"),
    IWJW_TEST3("http", "192.168.1.183", 8080, "/ihouse");



    private Configuration(String protocol, String hostname, int port, String path) {
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
    }

    public static Configuration DEFAULT = TEST;

    public String appKey = "fybao.superjia.com";
    // 报文头需要的字段
    public String appKeyLabel = "App-Key"; // key
    public String appSecretLabel = "App-Secret"; // secret
    public String appTime = "App_Time";// 当前时间
    public String appVersion = "ver"; // 手机端版本号
    public String appOS = "os"; // 手机来源 (android/ios)
    public String appIMEI = "imei"; // 手机唯一标志
    public String appModel = "model"; // 手机型号

    // Configuration
    public String protocol = "http";
    public String hostname = "192.168.1.123";// 生产环境
    public int port = 80;
    public String path = "/rest";

    public String getRootUrl() {
        return protocol + "://" + hostname + ":" + port + path;
    }

    public String getDomain() {
        return protocol + "://" + hostname + ":" + port;
    }

}