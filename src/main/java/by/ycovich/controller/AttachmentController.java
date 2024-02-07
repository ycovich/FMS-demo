package by.ycovich.controller;

import by.ycovich.dto.AttachmentDTO;
import by.ycovich.entity.Attachment;
import by.ycovich.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseEntity<AttachmentDTO> upload(@RequestParam("file")
                                                MultipartFile file) throws Exception {
        Attachment attachment = attachmentService.save(file);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/download/")
                .path(attachment.getId())
                .toUriString();
        AttachmentDTO response = AttachmentDTO.builder()
                .fileName(attachment.getFileName())
                .url(url)
                .fileType(attachment.getFileType())
                .fileSize(file.getSize())
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id")
                                             String id) throws Exception {
        Attachment attachment = attachmentService.getAttachment(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + attachment.getFileName() + "\"")
                .body(new ByteArrayResource(attachment.getData()));
    }
}
