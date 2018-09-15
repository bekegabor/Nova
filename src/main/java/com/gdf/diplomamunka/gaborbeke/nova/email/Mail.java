package com.gdf.diplomamunka.gaborbeke.nova.email;

import lombok.Data;

@Data
public class Mail {

    private String subject;
    private String username;
    private String message;
    private String sendTo;

    public Mail(){}

}
