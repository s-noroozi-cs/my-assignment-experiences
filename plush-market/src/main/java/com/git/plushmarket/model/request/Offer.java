package com.git.plushmarket.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Offer {
    private String plush;

    public Offer(String plush) {
        this.plush = plush;
    }
}
