package com.gdf.diplomamunka.gaborbeke.nova.enums;

public enum Role {

    USER("USER"), ADMIN("ADMIN"), EMPLOYEE("EMPLOYEE");

    private String roleName;

    Role(String roleName){
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
