package com.fileshare_backend.fileshare;

import jakarta.persistence.*;

import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Table(name = "fileshare_db")


public class FileMetaData {

    public FileMetaData(){};

    public FileMetaData(String file_name ,Long file_size , String code){
        this.file_name = file_name;
        this.file_size = file_size;
        this.code = code;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String file_name;

    @Column (nullable = false)
    private Long file_size;

    @Column (nullable = false , unique = true)
    private String code;

    @Column (nullable = false)
    private LocalDateTime uploadedAt;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFile_name(){
        return file_name;
    }
    public void setFile_name(String fileName){
        this.file_name = fileName;
    }
    public Long getFile_size(){
        return file_size;
    }
    public void setFile_size(Long filesize){
        this.file_size = filesize;
    }
    public String getCode(){
        return code;
    }
    public void setCode(String code){
    this.code = code;
    }
    public LocalDateTime getUploadedAt() { return uploadedAt; }


    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}


