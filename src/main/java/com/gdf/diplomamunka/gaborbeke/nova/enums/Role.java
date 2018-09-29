package com.gdf.diplomamunka.gaborbeke.nova.enums;

public enum Role {

    USER("USER"){
        @Override
        public String getUserFriendlyRole() {
            return "Felhasználó";
        }
    },
    ADMIN("ADMIN"){
        @Override
        public String getUserFriendlyRole() {
            return "Rendszergazda";
        }
    },
    EMPLOYEE("EMPLOYEE"){
        @Override
        public String getUserFriendlyRole() {
            return "Alkalmazott";
        }
    };

    private String roleName;

    Role(String roleName){
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
    public abstract String getUserFriendlyRole();
}
