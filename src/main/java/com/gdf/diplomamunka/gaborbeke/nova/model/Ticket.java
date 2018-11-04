package com.gdf.diplomamunka.gaborbeke.nova.model;

import com.gdf.diplomamunka.gaborbeke.nova.enums.Role;
import com.gdf.diplomamunka.gaborbeke.nova.enums.Status;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.AUTO;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy=AUTO)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = true)
    @Getter
    @Setter
    private Long employeeId;

    @Column(nullable = true)
    @Getter
    @Setter
    private LocalDateTime createDate;

    @Column(nullable = true)
    @Getter
    @Setter
    private LocalDateTime modifyDate;

    @Column(nullable = true)
    @Getter
    @Setter
    private LocalDateTime resolveDate;

    @Column(nullable = true)
    @Getter
    @Setter
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter
    @Setter
    private User user;

    @Column(nullable = true)
    @Type(type="text")
    @Getter
    @Setter
    private String content;

    @Getter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "ticket", optional = true, orphanRemoval = true)
    private Attachment attachment;

    @Column(nullable = true)
    @Enumerated(STRING)
    @Getter
    @Setter
    private Status status;

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", createDate=" + createDate +
                ", modifyDate=" + modifyDate +
                ", resolveDate=" + resolveDate +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", attachment=" + attachment +
                ", status=" + status +
                '}';
    }

    public void setAttachment(Attachment attachment){
        if (Objects.isNull(attachment)){
          this.attachment.setAttachment(null);
        }else{
            attachment.setTicket(this);
            attachment.setId(this.getId());
        }
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        return id != null ? id.equals(ticket.id) : ticket.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
