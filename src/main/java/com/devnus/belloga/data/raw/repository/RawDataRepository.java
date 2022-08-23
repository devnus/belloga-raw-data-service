package com.devnus.belloga.data.raw.repository;

import com.devnus.belloga.data.raw.domain.RawData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawDataRepository extends JpaRepository<RawData, Long> {
}

