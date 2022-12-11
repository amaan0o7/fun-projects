package com.producer.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class Books {
    private String bookName;
    private String author;
    private List<Books> bookList;
    private int size;
}
