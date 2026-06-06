package com.fileshare_backend.fileshare;

import com.fileshare_backend.fileshare.FileMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetaData , Long > {

    Optional<FileMetaData> findByCode(String code);
}
