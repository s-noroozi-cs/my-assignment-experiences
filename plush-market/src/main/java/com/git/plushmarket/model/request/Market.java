package com.git.plushmarket.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Market {
    private List<Plush> plushes;
    private List<Trade> trades;
}
