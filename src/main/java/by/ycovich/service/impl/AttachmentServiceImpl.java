package by.ycovich.service.impl;

import by.ycovich.entity.Attachment;
import by.ycovich.repository.AttachmentRepository;
import by.ycovich.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AttachmentServiceImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;

    @Override
    public Attachment save(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("[file name contains invalid path sequence] " + fileName);
            }
            Attachment attachment = Attachment.builder()
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .data(file.getBytes())
                    .build();
            return attachmentRepository.save(attachment);
        } catch (Exception e) {
            throw new Exception("[could not save file] " + fileName);
        }
    }

    @Override
    public Attachment getAttachment(String id) throws Exception {
        return attachmentRepository
                .findById(id)
                .orElseThrow(() -> new Exception("[file not found] " + id));
    }
}
