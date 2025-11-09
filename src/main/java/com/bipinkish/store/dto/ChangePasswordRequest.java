package com.bipinkish.store.dto;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
