package com.git.plushmarket.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Trade {
    private String take;
    private String give;

    public boolean isTakeAndGiveDifferent() {
        return !Objects.equals(take, give);
    }
}
