package com.gdf.diplomamunka.gaborbeke.nova.validator.credential;

import lombok.Data;

@Data
public class Credential {

    private String username;
    private String email;


    public Credential(String username, String email){
        this.username = username;
        this.email = email;
    }



}
