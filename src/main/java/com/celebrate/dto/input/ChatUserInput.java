package com.celebrate.dto.input;

import lombok.Data;

@Data
public class ChatUserInput {
    private String id;
    private String name;
    public void set_id(String _id){
        this.id = _id;
    }
}
