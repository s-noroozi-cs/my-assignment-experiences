package com.backbase.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "tb_service_accesses")
public class ServiceAccess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "access_name")
    private String accessName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }
}
