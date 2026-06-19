package com.celebrate.dto.input;

import lombok.Data;

import java.util.List;

@Data
public class CreateOptionInput {
    private String restaurant;
    private List<OptionInput> options;
}
