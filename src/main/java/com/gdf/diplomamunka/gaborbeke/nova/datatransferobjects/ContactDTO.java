package com.gdf.diplomamunka.gaborbeke.nova.datatransferobjects;

import lombok.Data;

@Data
public class ContactDTO {

    private String username;
    private String firstname;
    private String lastName;
    private String email;
    private String subject;
    private Long issueId;

    public ContactDTO(String username, String firstName, String lastname, String email, String subject, Long issueId){
        this.username = username;
        this.firstname = firstName;
        this.lastName = lastname;
        this.email = email;
        this.subject = subject;
        this.issueId = issueId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactDTO that = (ContactDTO) o;

        return email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}
