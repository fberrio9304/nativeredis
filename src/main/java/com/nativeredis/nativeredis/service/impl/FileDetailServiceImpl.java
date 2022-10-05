package com.nativeredis.nativeredis.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nativeredis.nativeredis.domain.FileDetailDto;
import com.nativeredis.nativeredis.infrastructure.repository.FileDetailRepository;
import com.nativeredis.nativeredis.service.FileDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.nativex.hint.JdkProxyHint;
import org.springframework.nativex.hint.SerializationHint;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@JdkProxyHint(types = {FileDetailService.class, org.springframework.aop.SpringProxy.class,
        org.springframework.aop.framework.Advised.class, org.springframework.core.DecoratingProxy.class})
@SerializationHint(types = {FileDetailRepository.class, ObjectMapper.class})
@CacheConfig(cacheNames = "FileDetailCache")
@Component
public class FileDetailServiceImpl implements FileDetailService {

    @Autowired
    private FileDetailRepository fileDetailRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Cacheable(cacheNames = {"FileDetailCache"})
    public List<FileDetailDto> getAllFileDetails() {
        List<FileDetailDto> result = fileDetailRepository.findAll().stream()
                .map(fileDetail -> objectMapper.convertValue(fileDetail, FileDetailDto.class))
                .collect(Collectors.toList());
        return result;
    }
}
