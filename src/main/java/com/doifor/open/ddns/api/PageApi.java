package com.doifor.open.ddns.api;

import com.doifor.open.ddns.entity.Account;
import com.doifor.open.ddns.entity.DDnsRecord;
import com.doifor.open.ddns.repository.AccountRepository;
import com.doifor.open.ddns.repository.DDnsRecordRepository;
import com.doifor.open.ddns.service.PlatformService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class PageApi {
    @Autowired
    private Map<String, PlatformService> platformServiceMap;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DDnsRecordRepository ddnsRecordRepository;

    record AccountVO(Long id, String name, String platform, String accessKey, String secret){}

    @GetMapping("/account/all")
    public List<AccountVO> listAccount(){
        return accountRepository.findAll().stream()
                .map(a -> new AccountVO(a.getId(), a.getName(),a.getPlatform(), a.getAccessKey(), a.getSecret()))
                .toList();
    }

    @GetMapping("/platforms")
    public Set<String> platforms() {
        return platformServiceMap.keySet();
    }

    @PostMapping("/account")
    public void addAccount(@RequestBody AccountVO accountVO) {
        Account account = new Account();
        BeanUtils.copyProperties(accountVO, account);
        accountRepository.save(account);
    }

    @DeleteMapping("/account/{id}")
    @Transactional
    public void deleteAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        // fixme delete ddns record
    }

    @GetMapping("/domains")
    public List<?> domains() {
        return null;
    }

    @GetMapping("/domain/records")
    public List<?> records(String domain, Long accountId) {
        return null;
    }

    record DDNSRecord(String rrKey, String domain, Long accountId, String currentIp){}

    @GetMapping("/record")
    public List<?> ddnsRecords(){
        List<DDnsRecord> dDnsRecords = ddnsRecordRepository.findAll();
        return null;
    }

    @PostMapping("/record")
    public void addRecord() {

    }
}
