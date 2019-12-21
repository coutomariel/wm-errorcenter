package com.warmachine.errorcenterapi.controller.error.converter;

import com.warmachine.errorcenterapi.controller.error.response.ErrorResponse;
import com.warmachine.errorcenterapi.entity.Error;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseConverter {

    public static ErrorResponse errorResponseFromEntity(Error error){
        return ErrorResponse.builder().description(error.getDescription()).usernameFromUser(error.getUser().getEmail())
                               .ambient(error.getAmbient()).level(error.getLevel()).ipOrigin(error.getIpOrigin())
                               .status(error.getStatus()).build();
    }
}
