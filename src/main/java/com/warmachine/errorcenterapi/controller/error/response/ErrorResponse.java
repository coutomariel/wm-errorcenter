package com.warmachine.errorcenterapi.controller.error.response;

import com.warmachine.errorcenterapi.util.Ambiente;
import com.warmachine.errorcenterapi.util.Level;
import com.warmachine.errorcenterapi.util.Status;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class ErrorResponse {
    private Ambiente ambient;
    private Level level;
    private String description;
    private String usernameFromUser;
    private Status status;
    private String ipOrigin;
}
