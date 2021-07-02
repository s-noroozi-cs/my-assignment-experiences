package com.ripe.controller.rest;

import com.ripe.controller.model.Member;
import com.ripe.entity.MemberEntity;
import com.ripe.exception.BadRequestException;
import com.ripe.exception.InternalServerError;
import com.ripe.exception.NotFoundException;
import com.ripe.mapper.MemberMapper;
import com.ripe.repository.MemberRepository;
import com.ripe.util.JsonUtil;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MembersRestController {
    private JsonUtil jsonUtil;
    private MemberRepository repository;

    @Autowired
    public void setJsonUtil(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }

    @Autowired
    public void setRepository(MemberRepository repository) {
        this.repository = repository;
    }

    @PostMapping(value = "/members", consumes = "application/json", produces = "application/json")
    public ResponseEntity addMember(@RequestBody String requestBody) {
        Either<Throwable, Member> eitherMember = jsonUtil.deserialize(requestBody, Member.class);

        if (eitherMember.isLeft()) {
            throw new BadRequestException(eitherMember.getLeft());
        }

        Try<MemberEntity> entitySaveTry = Try.of(() -> repository.save(MemberMapper.mapForSave(eitherMember.get())));

        if (entitySaveTry.isFailure()) {
            throw new InternalServerError(entitySaveTry.getCause());
        } else {
            return ResponseEntity.ok(MemberMapper.mapForLoad(entitySaveTry.get()));
        }
    }

    @GetMapping(value = "/members", produces = "application/json")
    public ResponseEntity listOfAllMember() {
        List<Member> members = repository.findAll().stream()
                .map(MemberMapper::mapForLoad).collect(Collectors.toList());
        return ResponseEntity.ok(members);
    }

    @GetMapping(value = "/members/{id}", produces = "application/json")
    public ResponseEntity memberDetail(@PathVariable("id") long memberId) {
        Member member = repository.findById(memberId)
                .map(MemberMapper::mapForLoad)
                .orElseThrow(() -> new NotFoundException("There is not exist any member with id:" + memberId));
        return ResponseEntity.ok(member);
    }
}
