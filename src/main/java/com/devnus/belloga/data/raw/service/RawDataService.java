package com.devnus.belloga.data.raw.service;

import com.devnus.belloga.data.raw.domain.DataType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RawDataService {
    boolean saveRawData(String enterpriseId, DataType dataType, MultipartFile multipartFile);
}
