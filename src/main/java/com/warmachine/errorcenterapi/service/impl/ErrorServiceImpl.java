package com.warmachine.errorcenterapi.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.warmachine.errorcenterapi.Messages;
import com.warmachine.errorcenterapi.controller.error.converter.ErrorResponseConverter;
import com.warmachine.errorcenterapi.controller.error.request.ErrorRequest;
import com.warmachine.errorcenterapi.controller.error.response.ErrorMessageResponse;
import com.warmachine.errorcenterapi.controller.error.response.ErrorResponse;
import com.warmachine.errorcenterapi.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.warmachine.errorcenterapi.controller.error.converter.ErrorRequestConverter;
import com.warmachine.errorcenterapi.entity.Error;
import com.warmachine.errorcenterapi.entity.User;
import com.warmachine.errorcenterapi.repository.ErrorsRepository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ErrorServiceImpl {

    private ErrorsRepository errorsRepository;
    private ErrorRequestConverter converter;
    private UserServiceImpl userService;

    @Autowired
    public ErrorServiceImpl(ErrorsRepository errorsRepository, ErrorRequestConverter converter, UserServiceImpl userService) {
        this.errorsRepository = errorsRepository;
        this.converter = converter;
        this.userService = userService;
    }


    public ErrorMessageResponse createError(ErrorRequest createErrorRequest) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> userOpt = userService.findByEmail(loggedInUser.getName());

        if(userOpt.isPresent()){
            User user = userOpt.get();
            String ip = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getRemoteAddr();
            Error error = converter.errorFromRequest(createErrorRequest, user, ip);
            errorsRepository.save(error);
            return new ErrorMessageResponse(Messages.ERROR_CREATED);
        }

       throw new IllegalArgumentException(Messages.UNABLE_TO_FIND_USER);

    }

    public ErrorMessageResponse delete(Long id) {
        Optional<Error> errorOpt = errorsRepository.findById(id);

        if(errorOpt.isPresent()) {
            Error error = errorOpt.get() ;
            errorsRepository.delete(error);
            return new ErrorMessageResponse(Messages.ERROR_DELETED);
        }

        throw new IllegalArgumentException(Messages.UNABLE_TO_FIND_ERROR);
    }

    public ErrorMessageResponse archive(Long id){
        Optional<Error> errorOpt = errorsRepository.findById(id);

        if(errorOpt.isPresent()){
            Error error = errorOpt.get();
            error.setStatus(Status.ARCHIVED);
            errorsRepository.save(error);
            return new ErrorMessageResponse(Messages.ERROR_ARCHIVED);
        }

        throw new IllegalArgumentException(Messages.UNABLE_TO_FIND_ERROR);
    }

    public List<ErrorResponse> detailAllErrors() {
        List<Error> errors = errorsRepository.findAll();
        return errors.stream()
                .map(ErrorResponseConverter::errorResponseFromEntity)
                .collect(Collectors.toList());

    }

}
