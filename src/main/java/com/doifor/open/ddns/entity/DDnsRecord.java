package com.doifor.open.ddns.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "t_ddns_record")
public class DDnsRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long accountId;
    private String domain;
    private String rrKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRrKey() {
        return rrKey;
    }

    public void setRrKey(String rrKey) {
        this.rrKey = rrKey;
    }
}
