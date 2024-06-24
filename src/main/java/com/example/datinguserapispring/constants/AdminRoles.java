package com.example.datinguserapispring.constants;

public class AdminRoles {
    public static final String ADMIN_ROLE_OWNER_STR = "OWNER";
    public static final String ADMIN_ROLE_ADMIN_STR = "ADMIN";
    public static final String ADMIN_ROLE_MODERATOR_STR = "MODERATOR";
    public static final int ADMIN_ROLE_OWNER = 1024;
    public static final int ADMIN_ROLE_ADMIN = 512;
    public static final int ADMIN_ROLE_MODERATOR = 256;

    public static String getAdminRole(int authority) {
        return switch (authority) {
            case ADMIN_ROLE_ADMIN -> ADMIN_ROLE_ADMIN_STR;
            case ADMIN_ROLE_MODERATOR -> ADMIN_ROLE_MODERATOR_STR;
            case ADMIN_ROLE_OWNER -> ADMIN_ROLE_OWNER_STR;
            default -> "";
        };
    }

    private AdminRoles() {}
}
