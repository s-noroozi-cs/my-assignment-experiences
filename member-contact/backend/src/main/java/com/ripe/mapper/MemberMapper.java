package com.ripe.mapper;

import com.ripe.controller.model.Contact;
import com.ripe.controller.model.Member;
import com.ripe.entity.ContactEntity;
import com.ripe.entity.MemberEntity;

import java.util.stream.Collectors;

public class MemberMapper {
    public static MemberEntity mapForSave(Member member) {
        MemberEntity entity = new MemberEntity();
        entity.setName(member.getName());
        entity.setContacts(member.getContacts()
                .stream()
                .map(MemberMapper::mapForSave)
                .collect(Collectors.toList()));
        return entity;
    }

    private static ContactEntity mapForSave(Contact contact) {
        ContactEntity entity = new ContactEntity();
        entity.setName(contact.getName());
        entity.setEmail(contact.getEmail());
        entity.setPhone(contact.getPhone());
        return entity;
    }


    public static Member mapForLoad(MemberEntity entity) {
        Member model = new Member();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setContacts(entity.getContacts()
                .stream()
                .map(MemberMapper::mapForLoad)
                .collect(Collectors.toList()));
        return model;
    }

    private static Contact mapForLoad(ContactEntity entity) {
        Contact model = new Contact();
        model.setName(entity.getName());
        model.setEmail(entity.getEmail());
        model.setPhone(entity.getPhone());
        return model;
    }

}
