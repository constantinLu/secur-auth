package ro.secur.auth.common;

public enum Role {

    ADMIN,
    USER;

    public static String getRole(Role role) {
        return "ROLE_" + role.toString();
    }
}

