// koupon/backend/src/main/java/com/koupon/backend/dto/LoginRequest.java
package com.koupon.backend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String userId;
}
