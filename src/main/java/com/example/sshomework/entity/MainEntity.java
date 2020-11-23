package com.example.sshomework.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

/**
 * @author Aleksey Romodin
 */
@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainEntity{

    @CreationTimestamp
    @Column(name = "date_time_created", updatable = false)
    private ZonedDateTime dateTimeCreated;

    @UpdateTimestamp
    @Column(name = "date_time_last_modified")
    private ZonedDateTime dateTimeLastModified;

    @Version
    @Column(name = "version")
    private Long version;
}

