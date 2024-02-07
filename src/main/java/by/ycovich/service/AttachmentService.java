package by.ycovich.service;

import by.ycovich.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    Attachment save(MultipartFile file) throws Exception;

    Attachment getAttachment(String id) throws Exception;
}
