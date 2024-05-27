package com.tritonkor.grouptester.desktop.net;

public final class ApiUrls {
    private ApiUrls() {}
    public static final String MAIN_URL = "http://localhost:8080/api";

    public static final String AUTH_URL = MAIN_URL + "/auth";
    public static final String REGISTER_USER_URL = AUTH_URL + "/register";
    public static final String AUTH_USER_URL = AUTH_URL + "/authenticate";

    public static final String TEST_URL = MAIN_URL + "/test";
    public static final String ALL_USER_TESTS = TEST_URL + "/user-tests";

    public static final String GROUP_URL = MAIN_URL + "/group";
    public static final String CREATE_GROUP_URL = GROUP_URL + "/new";
    public static final String DELETE_GROUP_URL = GROUP_URL + "/delete";
    public static final String JOIN_GROUP_URL = GROUP_URL + "/join";
    public static final String LEAVE_GROUP_URL = GROUP_URL + "/leave";
    public static final String GROUP_STATUS_URL = GROUP_URL + "/status";

    public static final String RESULT_URL = MAIN_URL + "/result";
    public static final String ALL_USER_RESULTS = RESULT_URL + "/user-results";

    public static final String USER_URL = MAIN_URL + "/user";
    public static final String ALL_USERS_URL = USER_URL + "/all";

}
