package com.devnus.belloga.data.project.domain;

import com.devnus.belloga.data.common.domain.BaseTimeEntity;
import com.devnus.belloga.data.raw.domain.DataType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "project")
@Getter
@NoArgsConstructor
public class Project extends BaseTimeEntity {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_agreed")
    private Boolean isAgreed;

    @Column(name = "enterprise_id")
    private String enterpriseId;

    @Column(name = "zip_uuid")
    private String zipUUID;

    @Column(name = "zip_url")
    private String zipUrl;

    @Column(name = "data_type")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Column(name = "description")
    private String description;

    public void agree(){
        this.isAgreed = true;
    }

    @Builder
    public Project(String name, String enterpriseId, String zipUUID, DataType dataType, String description, String zipUrl) {
        this.name = name;
        this.enterpriseId = enterpriseId;
        this.dataType = dataType;
        this.isAgreed = false;
        this.zipUUID = zipUUID;
        this.description = description;
        this.zipUrl = zipUrl;
    }
}
