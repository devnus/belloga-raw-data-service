package com.devnus.belloga.data.raw.service;

import com.devnus.belloga.data.common.util.S3Uploader;
import com.devnus.belloga.data.raw.domain.DataType;
import com.devnus.belloga.data.raw.domain.RawData;
import com.devnus.belloga.data.raw.dto.EventRawData;
import com.devnus.belloga.data.raw.event.RawDataProducer;
import com.devnus.belloga.data.raw.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RawDataServiceImpl implements RawDataService{

    private final RawDataRepository rawDataRepository;
    private final S3Uploader s3Uploader;
    private final RawDataProducer rawDataProducer;

    /**
     * S3에 Raw 데이터를 업로드하고, DB에 해당 Raw데이터의 정보를 저장 및 데이터 전처리 마이크로서비스로 비동기 전달
     */
    @Override
    @Transactional
    public boolean saveRawData(String enterpriseId, DataType dataType, MultipartFile multipartFile) {

        String imageUrl = s3Uploader.upload(multipartFile, "dev-data");

        RawData rawData = rawDataRepository.save(RawData.builder()
                        .dataType(DataType.OCR)
                        .enterpriseId(enterpriseId)
                        .imageUrl(imageUrl).build());

        rawDataProducer.uploadRawData(EventRawData.uploadRawData.builder()
                        .rawDataId(rawData.getId())
                        .dataType(DataType.OCR)
                        .enterpriseId(enterpriseId)
                        .imageUrl(imageUrl).build());

        return true;
    }
}