
package com.nativeredis.nativeredis.infrastructure.controller;

import com.nativeredis.nativeredis.domain.FileDetailDto;
import com.nativeredis.nativeredis.service.FileDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/file-detail")
public class FileDetailController {

    private final FileDetailService fileDetailServiceImpl;

    public FileDetailController(FileDetailService fileDetailServiceImpl) {
        this.fileDetailServiceImpl = fileDetailServiceImpl;
    }

    @GetMapping(path = "/find-all")
    public ResponseEntity<List<FileDetailDto>> findAll() {
        log.info("Se inicia Prueba");
        List<FileDetailDto> response = fileDetailServiceImpl.getAllFileDetails();
        return ResponseEntity.ok(response);
    }
}
