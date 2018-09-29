package com.gdf.diplomamunka.gaborbeke.nova.controller;

import com.gdf.diplomamunka.gaborbeke.nova.enums.Status;
import com.gdf.diplomamunka.gaborbeke.nova.model.Attachment;
import com.gdf.diplomamunka.gaborbeke.nova.model.Ticket;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.services.FileService;
import com.gdf.diplomamunka.gaborbeke.nova.services.TicketService;
import com.gdf.diplomamunka.gaborbeke.nova.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.editor.Editor;
import org.primefaces.component.fileupload.FileUpload;
import org.primefaces.component.texteditor.TextEditor;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Scope("session")
@Component(value = "userTicketsController")
@ELBeanName(value = "userTicketsController")
@Join(path = "/user/issue/mytickets", to = "/issue/mytickets/tickets.xhtml")
public class UserTicketsController {

    private final TicketService ticketService;
    private final FileService fileService;
    private final UserService userService;
    private Map<String, String> previewTypes;
    private Map<String, String> previewHeaderType;

    @Getter
    @Setter
    private TextEditor textEditor;

    @Getter
    @Setter
    private UploadedFile uploadedFile;

    @Getter
    @Setter
    private String dialogToExecute;


    @Getter
    @Setter
    private Boolean isPreviewDisabled;

    @Getter
    @Setter
    private String dialogHeaderTypeToModify;

    @Setter
    private StreamedContent pdfContent;

    @Setter
    private StreamedContent jpegContent;

    @Getter
    @Setter
    private List<Ticket> tickets;

    @Getter
    @Setter
    private Ticket selectedTicket;

    @Getter
    @Setter
    private DataTable dataTableTickets;

    @Autowired
    public UserTicketsController(TicketService ticketService, FileService fileService, UserService userService){
        this.ticketService = ticketService;
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostConstruct
    void initUserTicketsList(){
        String username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        Optional<User> loggedInUser = userService.getUserByUsername(username);
        tickets = ticketService.getUserTickets(loggedInUser.get());
        previewTypes = new HashMap();
        previewHeaderType = new HashMap();
        previewTypes.put("pdf", "PF('pdfAttachmentPreviewDialog').show()");
        previewTypes.put("jpg", "PF('jpegAttachmentPreviewDialog').show()");
        previewTypes.put("jpeg", "PF('jpegAttachmentPreviewDialog').show()");
        previewTypes.put("NA", "PF('noAttachmentDialog').show()");
        previewHeaderType.put("pdf", "pdfAttachmentPreviewDialogHeader");
        previewHeaderType.put("jpg", "jpegAttachmentPreviewDialogHeader");
        previewHeaderType.put("jpeg", "jpegAttachmentPreviewDialogHeader");
        previewHeaderType.put("NA", "Melléklet előnézet");
        selectedTicket = new Ticket();
    }

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        String username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        Optional<User> loggedInUser = userService.getUserByUsername(username);
        tickets = ticketService.getUserTickets(loggedInUser.get());
    }

    public void onRowSelect(SelectEvent event) {
        Ticket currentTicket = (Ticket) event.getObject();
        selectedTicket = currentTicket;
    }

    public void deleteTicket(ActionEvent ev) {
        FacesContext context = FacesContext.getCurrentInstance();
        String currentUserName = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        String message = null;
        User currentUser = userService.getUserByUsername(currentUserName).get();
        selectedTicket = (Ticket) dataTableTickets.getRowData();
        dataTableTickets.setSelection(selectedTicket);

        if(selectedTicket.getStatus().name().equals("CLOSED") || selectedTicket.getStatus().name().equals("IN_PROGRESS")) {
            message="'Folyamatban' vagy 'Lezárt' státuszban lévő jegyeket nem lehet törölni!";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hibajegy törlése", message));
            return;
        }
            ticketService.delete(selectedTicket);
            currentUser.removeTicket(selectedTicket);
            tickets.remove(selectedTicket);
            message = "A "+selectedTicket.getId()+" számú hibajegye törölve lett!";
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Hibajegy törlése", message));
    }

    public void selectTicketAndPreviewMode(Ticket selectedTicket){
        this.selectedTicket = selectedTicket;
        selectedTicket = (Ticket) dataTableTickets.getRowData();
        dataTableTickets.setSelection(selectedTicket);
        if (Objects.nonNull(selectedTicket.getAttachment())){
            this.isPreviewDisabled = false;
            String fileType = StringUtils.getFilenameExtension(selectedTicket.getAttachment().getFileName());
            dialogToExecute = previewTypes.get(fileType);
            dialogHeaderTypeToModify = previewHeaderType.get(fileType);
        }else{
            this.isPreviewDisabled = true;
        }
    }

    public void validateAndSaveTicketModifications() throws SQLException {
        String currentUserName = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

        if (isEditorContainsOnlyEmptyHtml()){
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "A szóköz nem elfogadott érték!"));
        }

     if (Objects.nonNull(uploadedFile)) {
         fileService.setUploadedFile(uploadedFile);
         if (uploadedFile.getSize() > 0 && (!uploadedFile.getFileName().equals("") && fileService.isValidFileType()) && fileService.isValidFileSize()) {
             Attachment attachment = selectedTicket.getAttachment();
             if (Objects.isNull(selectedTicket.getAttachment())){
                attachment = new Attachment();
            }

             attachment.setAttachment(fileService.getAttachmentAsBlob(uploadedFile));
             attachment.setFileName(uploadedFile.getFileName());
             attachment.setFileSize(uploadedFile.getSize());
             attachment.setUploadedOn(LocalDateTime.now());
             if (Objects.nonNull(selectedTicket.getAttachment())){
                 ticketService.removeAttachment(selectedTicket);
             }
             selectedTicket.setAttachment(attachment);

         }else if (!uploadedFile.getFileName().equals("") && fileService.isValidFileType()) {
             FacesContext context = FacesContext.getCurrentInstance();
             context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Érvénytelen hibajegy!", fileService.getErrorMessage()));
             return;
         }
     }

          selectedTicket.setStatus(Status.PENDING);
          selectedTicket.setModifyDate(LocalDateTime.now());
          User currentUser = userService.getUserByUsername(currentUserName).get();
          selectedTicket = ticketService.create(selectedTicket);
          currentUser.addTicket(selectedTicket);

      }

    public StreamedContent getPdfAttachmentForPreview() throws SQLException {
        Blob blob = selectedTicket.getAttachment().getAttachment();
        byte[] attachmentBytes = selectedTicket.getAttachment().getAttachment().getBytes(1, (int)blob.length());
        ByteArrayInputStream attachmentInputStream = new ByteArrayInputStream(attachmentBytes);
        attachmentInputStream.mark(0);
        pdfContent = new DefaultStreamedContent(attachmentInputStream,"application/pdf");
        return pdfContent;
    }
    public StreamedContent getPdfContent() throws IOException {
        if (pdfContent != null)
            pdfContent.getStream().reset(); //reset stream to the start position!
        return pdfContent;
    }

    public StreamedContent getJpegAttachmentForPreview() throws SQLException{
        Blob blob = selectedTicket.getAttachment().getAttachment();
        byte[] attachmentBytes = selectedTicket.getAttachment().getAttachment().getBytes(1, (int)blob.length());
        ByteArrayInputStream attachmentInputStream = new ByteArrayInputStream(attachmentBytes);
        attachmentInputStream.mark(0);
        jpegContent = new DefaultStreamedContent(attachmentInputStream,"application/pdf");
        return jpegContent;
    }

    public StreamedContent getJpegContent() throws IOException {
        if (jpegContent != null)
            jpegContent.getStream().reset(); //reset stream to the start position!
        return jpegContent;
    }

    private Boolean isEditorContainsOnlyEmptyHtml(){
        String content = selectedTicket.getContent();
        content = content.replaceAll("&nbsp;","").replaceAll("<p>|</p>|<br>|</br>|<div>|</div>|\t|\r|\n","");
        selectedTicket.setContent(content);
        return selectedTicket.getContent().trim().length() == 0;
    }
    }

