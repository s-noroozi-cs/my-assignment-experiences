package com.ripe.repository;

import com.ripe.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
