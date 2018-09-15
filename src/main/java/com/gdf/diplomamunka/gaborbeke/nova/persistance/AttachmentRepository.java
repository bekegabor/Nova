package com.gdf.diplomamunka.gaborbeke.nova.persistance;

import com.gdf.diplomamunka.gaborbeke.nova.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
