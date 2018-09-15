package com.gdf.diplomamunka.gaborbeke.nova.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.sql.Blob;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
public class Attachment {

    public Attachment(){}

    @Id
    private Long id;

    @Column(nullable = true)
    @Lob
    private Blob attachment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticketId", nullable = false)
    @MapsId
    private Ticket ticket;

    @Column(nullable = false, name = "uploaded_on")
    private LocalDateTime uploadedOn;

    @Column(nullable = false, name = "file_name")
    private String fileName;

    @Column(nullable = false, name = "file_size")
    private long fileSize;
}
