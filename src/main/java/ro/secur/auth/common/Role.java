package ro.secur.auth.common;

public enum Role {

    ADMIN,
    USER;

    public String role() {
        return "ROLE_" + this.toString();
    }
}

