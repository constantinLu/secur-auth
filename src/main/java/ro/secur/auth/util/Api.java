package ro.secur.auth.util;

public class Api {

    private static final String PREFIX_URL = "/api/v1";

    public static final String LOGIN_URL = PREFIX_URL + "/login";

    public static final String USERS_URL = PREFIX_URL + "/users";

    public static final String FORGOT_PASSWORD_URL = PREFIX_URL + "/forgotPassword";

    public static final String RESET_PASSWORD_URL = PREFIX_URL + "/{token}/resetPassword";

}
