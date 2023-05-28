package com.doifor.open.ddns.service;

public interface PlatformService {
    void updateDns(String rrKey, String domain, String ip, String key, String secret);
}
