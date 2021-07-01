package com.backbase.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_service_keys")
public class ServiceKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "KEY",unique = true)
    private String key;

    @Column(name = "EXPIRATION_TIME")
    private LocalDateTime expirationTime;

    @Column(name = "OWNER")
    private String owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_service_key_access",
            joinColumns = @JoinColumn(name = "service_key_id"),
            inverseJoinColumns = @JoinColumn(name = "service_access_id"))
    private List<ServiceAccess> serviceAccesses;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<ServiceAccess> getServiceAccesses() {
        return serviceAccesses;
    }

    public void setServiceAccesses(List<ServiceAccess> serviceAccesses) {
        this.serviceAccesses = serviceAccesses;
    }
}
