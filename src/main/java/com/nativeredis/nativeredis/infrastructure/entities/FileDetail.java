package com.nativeredis.nativeredis.infrastructure.entities;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class FileDetail implements Serializable {

    @Id
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
