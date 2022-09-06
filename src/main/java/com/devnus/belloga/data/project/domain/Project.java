package com.devnus.belloga.data.project.domain;

import com.devnus.belloga.data.raw.domain.DataType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "project")
@Getter
@NoArgsConstructor
public class Project {
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_agreed")
    private boolean isAgreed;

    @Column(name = "enterprise_id")
    private String enterpriseId;

    @Column(name = "zip_url")
    private String zipUrl;

    @Column(name = "data_type")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Builder
    public Project(String name, String enterpriseId, String zipUrl, DataType dataType) {
        this.name = name;
        this.enterpriseId = enterpriseId;
        this.zipUrl = zipUrl;
        this.dataType = dataType;
        this.isAgreed = false;
    }
}
