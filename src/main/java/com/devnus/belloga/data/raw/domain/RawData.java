package com.devnus.belloga.data.raw.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "raw_data")
@Getter
@NoArgsConstructor
public class RawData {

    @Id
    @Column(name = "raw_data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "enterprise_id")
    private String enterpriseId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_agreed")
    private boolean isAgreed;

    @Column(name = "data_type")
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Builder
    public RawData(String enterpriseId, String imageUrl, DataType dataType) {
        this.enterpriseId = enterpriseId;
        this.imageUrl = imageUrl;
        this.isAgreed = false;
        this.dataType = dataType;
    }

}
