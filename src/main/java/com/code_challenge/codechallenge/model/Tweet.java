package com.code_challenge.codechallenge.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class Tweet {
    private User user; // nadawca
    private User toUser; // odbiorca
    private LocalDateTime time;

    @NotNull
    @Size(min = 1, max = 140)
    private String message;
}
