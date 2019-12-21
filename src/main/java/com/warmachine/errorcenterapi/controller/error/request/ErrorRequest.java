package com.warmachine.errorcenterapi.controller.error.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.warmachine.errorcenterapi.util.Ambiente;
import com.warmachine.errorcenterapi.util.Level;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorRequest {
    @NotNull(message = "Inform the ambient")
    private Ambiente ambient;
    @NotNull(message = "Inform the level")
    private Level level;
    @NotNull(message = "Inform the description")
    private String description;
}
