package com.nativeredis.nativeredis.domain;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@Builder
public class FileDetailDto implements Serializable {

    private Long id;
    private Long idLoadingHeader;
    private Long idStateFileDetail;
    private String name;
    private String type;
    private String urlStorage;
    private String creationUser;
    private Long size;
    private String typeFile;
    private Integer orderType;
    private String urlProcessFile;
    private String stateValidation;

}
