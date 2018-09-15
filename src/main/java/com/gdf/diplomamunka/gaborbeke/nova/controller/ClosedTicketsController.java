package com.gdf.diplomamunka.gaborbeke.nova.controller;


import com.gdf.diplomamunka.gaborbeke.nova.model.Ticket;
import com.gdf.diplomamunka.gaborbeke.nova.model.User;
import com.gdf.diplomamunka.gaborbeke.nova.services.FileService;
import com.gdf.diplomamunka.gaborbeke.nova.services.TicketService;
import com.gdf.diplomamunka.gaborbeke.nova.services.UserService;
import lombok.Getter;
import lombok.Setter;
import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.texteditor.TextEditor;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;


@Scope(value = "session")
@Component(value = "closedTicketsController")
@ELBeanName(value = "closedTicketsController")
@Join(path = "/employee/issues/closed", to = "/employee/issues/closed_tickets.xhtml")
public class ClosedTicketsController {

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
    public ClosedTicketsController(TicketService ticketService, FileService fileService, UserService userService){
        this.ticketService = ticketService;
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostConstruct
    void initOpenTicketsList(){
        String username = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        Optional<User> loggedInEmployee = userService.getUserByUsername(username);
        tickets = ticketService.getClosedTicketsByEmployee(loggedInEmployee.get());
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
        Optional<User> loggedInEmployee = userService.getUserByUsername(username);
        tickets = ticketService.getClosedTicketsByEmployee(loggedInEmployee.get());
    }

    public void onRowSelect(SelectEvent event) {
        Ticket currentTicket = (Ticket) event.getObject();
        selectedTicket = currentTicket;
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

}
