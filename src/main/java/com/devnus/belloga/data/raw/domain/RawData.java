package com.devnus.belloga.data.raw.domain;

import com.devnus.belloga.data.common.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "raw_data")
@Getter
@NoArgsConstructor
public class RawData extends BaseTimeEntity {

    @Id
    @Column(name = "raw_data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enterprise_id")
    private String enterpriseId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "data_type")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Builder
    public RawData(String enterpriseId, String fileUrl, Long projectId, String fileName, DataType dataType) {
        this.enterpriseId = enterpriseId;
        this.projectId = projectId;
        this.fileUrl = fileUrl;
        this.dataType = dataType;
        this.fileName = fileName;
    }

}
