package com.doifor.open.ddns;

import com.doifor.open.ddns.entity.Account;
import com.doifor.open.ddns.entity.DDnsRecord;
import com.doifor.open.ddns.repository.AccountRepository;
import com.doifor.open.ddns.repository.DDnsRecordRepository;
import com.doifor.open.ddns.service.PlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class DDNSRefreshTask {

    private final Logger logger = LoggerFactory.getLogger(DDNSRefreshTask.class);
    private final AtomicReference<String> lastIp = new AtomicReference<>();

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DDnsRecordRepository ddnsRecordRepository;
    @Autowired
    private Map<String, PlatformService> refreshServiceMap;

    @Scheduled(cron = "0/10 * * * * ?")
    public void refresh() {
        final String ip = restTemplate.getForObject("http://myip.ipip.net/s", String.class);
        logger.info("check local public ip: {}", ip);
        String last = lastIp.get();
        if (!Objects.equals(last, ip)) {
            Map<Long, List<DDnsRecord>> recordMap = ddnsRecordRepository.findAll().stream().collect(Collectors.groupingBy(DDnsRecord::getAccountId));
            if (!recordMap.isEmpty()) {
                accountRepository.findAllById(recordMap.keySet())
                        .stream().collect(Collectors.groupingBy(Account::getPlatform))
                        .forEach((p, as) -> {
                            final PlatformService service = refreshServiceMap.get(p);
                            as.forEach(a -> recordMap.get(a.getId()).forEach(r -> {
                                logger.info("refresh {} ddns: {} <--> {}.{}", p, ip, r.getRrKey(), r.getDomain());
                                service.updateDns(r.getRrKey(), r.getDomain(), ip, a.getAccessKey(), a.getSecret());
                            }));
                        });
            }
            lastIp.set(ip);
        }
    }
}
