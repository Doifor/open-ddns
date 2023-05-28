
package com.doifor.open.ddns.repository;

import com.doifor.open.ddns.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
