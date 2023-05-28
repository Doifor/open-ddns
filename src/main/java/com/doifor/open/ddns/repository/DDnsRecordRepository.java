package com.doifor.open.ddns.repository;

import com.doifor.open.ddns.entity.DDnsRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DDnsRecordRepository extends JpaRepository<DDnsRecord, Long> {
}
