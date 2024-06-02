package com.tritonkor.grouptester.desktop.net;

public final class ApiUrls {
    private ApiUrls() {}
    public static final String MAIN_URL = "http://localhost:8080/api";

    public static final String AUTH_URL = MAIN_URL + "/auth";
    public static final String REGISTER_USER_URL = AUTH_URL + "/register";
    public static final String AUTH_USER_URL = AUTH_URL + "/authenticate";
    public static final String UNAUTH_USER_URL = AUTH_URL + "/unauthorize";

    public static final String TEST_URL = MAIN_URL + "/test";
    public static final String ALL_USER_TESTS_URL = TEST_URL + "/user-tests";
    public static final String SAVE_QUESTION_TESTS_URL = TEST_URL + "/question/save";
    public static final String CREATE_TEST_URL = TEST_URL + "/create";
    public static final String DELETE_TEST_URL = TEST_URL + "/delete";
    public static final String DELETE_QUESTION_TEST_URL = TEST_URL + "/question/delete";

    public static final String GROUP_URL = MAIN_URL + "/group";
    public static final String CREATE_GROUP_URL = GROUP_URL + "/new";
    public static final String DELETE_GROUP_URL = GROUP_URL + "/delete";
    public static final String JOIN_GROUP_URL = GROUP_URL + "/join";
    public static final String LEAVE_GROUP_URL = GROUP_URL + "/leave";
    public static final String GROUP_STATUS_URL = GROUP_URL + "/status";
    public static final String CHANGE_USER_STATUS_URL = GROUP_URL + "/user/change-status";
    public static final String CHOOSE_TEST_URL = GROUP_URL + "/test";
    public static final String RUN_TEST_URL = GROUP_URL + "/run-test";

    public static final String RESULT_URL = MAIN_URL + "/result";
    public static final String SAVE_RESULT_URL = RESULT_URL + "/save";
    public static final String ALL_STUDENT_RESULTS_URL = RESULT_URL + "/student-results";
    public static final String ALL_TEACHER_RESULTS_URL = RESULT_URL + "/teacher-results";

    public static final String USER_URL = MAIN_URL + "/user";
    public static final String ALL_USERS_URL = USER_URL + "/all";

    public static final String TAG_URL = MAIN_URL + "/tag";
    public static final String ALL_TAGS_URL = TAG_URL + "/all";

}
