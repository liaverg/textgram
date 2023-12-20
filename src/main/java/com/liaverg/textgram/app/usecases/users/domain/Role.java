package com.liaverg.textgram.app.usecases.users.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    FREE(1000, 5),
    PREMIUM(3000, Integer.MAX_VALUE);

    private final int MAX_POST_SIZE;
    private final int MAX_COMMENT_COUNT_PER_POST;
}
