package com.gdf.diplomamunka.gaborbeke.nova.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AttachmentTest {

    @Test
    public void testCreate() {
        Attachment attachment = new Attachment();
        attachment.setFileName("alma");

        assertThat(attachment.getFileName(), equalTo("alma"));
    }
}
