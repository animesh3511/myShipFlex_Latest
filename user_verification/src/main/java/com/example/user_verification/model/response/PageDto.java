package com.example.user_verification.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageDto {

    private Object dataSet;
    private int totalPages;
    private Long totalElements;
    private int number;
}
