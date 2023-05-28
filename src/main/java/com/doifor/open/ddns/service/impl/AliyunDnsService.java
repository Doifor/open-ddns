package com.doifor.open.ddns.service.impl;

import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.AddDomainRecordRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponse;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.doifor.open.ddns.service.PlatformService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("aliyun")
public class AliyunDnsService implements PlatformService {
    private final Logger logger = LoggerFactory.getLogger(AliyunDnsService.class);
    @Override
    public void updateDns(String rrKey, String domain, String ip, String key, String secret) {
        Config config = new Config().setAccessKeyId(key)
                .setAccessKeySecret(secret)
                .setEndpoint("alidns.aliyuncs.com");
        try {
            Client client = new Client(config);

            DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest()
                    .setDomainName(domain).setRRKeyWord(rrKey);
            RuntimeOptions runtime = new RuntimeOptions();
            DescribeDomainRecordsResponse response = client.describeDomainRecordsWithOptions(request, runtime);
            response.body.domainRecords.record.stream().findFirst()
                    .ifPresentOrElse(r -> {
                        if (!Objects.equals(r.value, ip)) {
                            UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest()
                                    .setRecordId(r.recordId)
                                    .setRR(rrKey)
                                    .setType("A")
                                    .setValue(ip);
                            try {
                                client.updateDomainRecordWithOptions(updateDomainRecordRequest, runtime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, () -> {
                        AddDomainRecordRequest addDomainRecordRequest = new AddDomainRecordRequest()
                                .setDomainName(domain)
                                .setRR(rrKey)
                                .setType("A")
                                .setValue(ip);
                        try {
                            client.addDomainRecordWithOptions(addDomainRecordRequest, runtime);
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }

                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
