package com.yo.yoshare.memberservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UmsMemberExistResult {

    Boolean exist;
    Boolean username;
    Boolean phone;
    Boolean email;

    public UmsMemberExistResult() {
        username = false;
        phone = false;
        email = false;
        exist = false;
    }


}
