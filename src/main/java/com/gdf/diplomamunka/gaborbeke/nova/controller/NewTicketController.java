package com.gdf.diplomamunka.gaborbeke.nova.controller;

import com.gdf.diplomamunka.gaborbeke.nova.enums.Status;
import com.gdf.diplomamunka.gaborbeke.nova.model.Attachment;
import com.gdf.diplomamunka.gaborbeke.nova.model.Ticket;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.services.FileService;
import com.gdf.diplomamunka.gaborbeke.nova.services.TicketService;
import com.gdf.diplomamunka.gaborbeke.nova.services.UserService;
import lombok.Data;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Scope(value = "request")
@Component(value = "newTicketController")
@ELBeanName(value = "newTicketController")
@Join(path = "/user/issue/ticket/new/", to = "/issue/ticket/new/new_ticket_form.xhtml")
@Data
public class NewTicketController {

    private Ticket ticket = new Ticket();
    private UploadedFile uploadedFile;
    private final FileService fileService;
    private final UserService userService;
    private final TicketService ticketService;

    @Autowired
    public NewTicketController(FileService fileService, UserService userService, TicketService ticketService){
        this.fileService = fileService;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    public void save() throws SQLException, IOException, InterruptedException {
        String currentUserName = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        FacesContext context = FacesContext.getCurrentInstance();
        if(context.getMessageList().size()!=0){
            return;
        }

        if (isEditorContainsOnlyEmptyHtml()){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "A szóköz nem elfogadott érték!"));
            return;
        }

        if (Objects.nonNull(uploadedFile)) {
            fileService.setUploadedFile(uploadedFile);

            if (uploadedFile.getSize() > 0 && (!uploadedFile.getFileName().equals("") && fileService.isValidFileType()) && fileService.isValidFileSize()) {
                Attachment attachment = new Attachment();
                attachment.setAttachment(fileService.getAttachmentAsBlob(uploadedFile));
                attachment.setFileName(uploadedFile.getFileName());
                attachment.setFileSize(uploadedFile.getSize());
                attachment.setUploadedOn(LocalDateTime.now());
                ticket.setAttachment(attachment);
            }else{//set dummy attachment
                Attachment attachment = new Attachment();
                attachment.setFileName("");
                attachment.setFileSize(1);
                attachment.setUploadedOn(LocalDateTime.now());
                ticket.setAttachment(attachment);
            }

            ticket.setStatus(Status.PENDING);
            ticket.setCreateDate(LocalDateTime.now());
            User currentUser = userService.getUserByUsername(currentUserName).get();
            if (uploadedFile.getFileName().equals("") || fileService.isValidFileType()){
                currentUser.addTicket(ticket);
                ticketService.create(ticket);
                ExternalContext externalContext = context.getExternalContext();
                externalContext.redirect(externalContext.getRequestContextPath()+"/user/issue/mytickets");
            }else if (!uploadedFile.getFileName().equals("") && fileService.isValidFileType()){
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Érvénytelen hibajegy!", fileService.getErrorMessage()));
            }
        }else{
            ticket.setStatus(Status.PENDING);
            ticket.setCreateDate(LocalDateTime.now());
            User currentUser = userService.getUserByUsername(currentUserName).get();
            currentUser.addTicket(ticket);
            ticketService.create(ticket);
            ExternalContext externalContext = context.getExternalContext();
            externalContext.redirect(externalContext.getRequestContextPath()+"/user/issue/mytickets");
        }

    }

    private Boolean isEditorContainsOnlyEmptyHtml(){
        String content = ticket.getContent();
        content = content.replaceAll("&nbsp;","").replaceAll("<p>|</p>|<br>|</br>|<div>|</div>|\t|\r|\n","");
        ticket.setContent(content);
        return ticket.getContent().trim().length() == 0;
    }
}
