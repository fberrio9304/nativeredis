package com.nativeredis.nativeredis.infrastructure.repository;

import com.nativeredis.nativeredis.infrastructure.entities.FileDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDetailRepository extends CrudRepository<FileDetail, Long> {

    List<FileDetail> findAll();

    @Query("SELECT COUNT(fd) FROM FileDetail fd")
    Long countFileDetails();

}
