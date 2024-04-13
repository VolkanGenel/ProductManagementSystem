package com.volkan.service;

import com.volkan.dto.request.LoginRequestDto;
import com.volkan.dto.request.RegisterRequestDto;
import com.volkan.dto.request.ResetPasswordRequestDto;
import com.volkan.dto.request.UpdateRoleRequestDto;
import com.volkan.dto.response.FindAllResponseDto;
import com.volkan.exception.AuthServiceException;
import com.volkan.exception.EErrorType;
import com.volkan.mapper.IAuthMapper;
import com.volkan.rabbitmq.model.ResetPasswordModel;
import com.volkan.rabbitmq.producer.ResetPasswordProducer;
import com.volkan.repository.IAuthRepository;
import com.volkan.repository.entity.Auth;
import com.volkan.repository.enums.EStatus;
import com.volkan.utility.CodeGenerator;
import com.volkan.utility.JwtTokenManager;
import com.volkan.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth,Long> {
    private final IAuthRepository repository;
    private final JwtTokenManager tokenManager;
    private final ResetPasswordProducer resetPasswordProducer;
    //private final PasswordEncoder passwordEncoder;

    public AuthService(IAuthRepository repository,
                       JwtTokenManager tokenManager,
                       ResetPasswordProducer resetPasswordProducer) {
        super(repository);
        this.repository = repository;
        this.tokenManager = tokenManager;
        this.resetPasswordProducer = resetPasswordProducer;
    }

    public Auth register(RegisterRequestDto dto) {
        if(repository.isEmail(dto.getEmail()))
            throw new AuthServiceException(EErrorType.REGISTER_ERROR_EMAIL);
            String hashedPassword = CodeGenerator.encode(dto.getPassword());
        dto.setPassword(hashedPassword);
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        save(auth);
        return auth;
    }

    public String forgotMyPassword(String email) {
        Optional<Auth> auth = repository.findByEmail(email);
        if (auth.isEmpty())
            throw new AuthServiceException(EErrorType.EMAIL_NOT_FOUND);
        if(auth.get().getStatus().equals(EStatus.DELETED))
            throw new AuthServiceException(EErrorType.AUTH_DELETED);
        String generatedCode = CodeGenerator.generateCode();
        auth.get().setPassword(CodeGenerator.encode(generatedCode));
        auth.get().setStatus(EStatus.PASSIVE);
        update(auth.get());
        resetPasswordProducer.sendNewPassword(ResetPasswordModel.builder().email(auth.get().getEmail()).password(generatedCode).build());
        return "Password has been sent by e-mail, password needs to be updated for activating the profile!";
    }

    public Boolean resetPassword(ResetPasswordRequestDto dto) {
        Optional<Auth> authOptional = repository.findOptionalByEmailIgnoreCaseAndPassword(dto.getEmail(), CodeGenerator.encode(dto.getPassword()));
        if (authOptional.isEmpty()) {
            throw new AuthServiceException(EErrorType.LOGIN_ERROR);
        }
        if(authOptional.get().getStatus().equals(EStatus.DELETED))
            throw new AuthServiceException(EErrorType.AUTH_DELETED);
        if (dto.getNewPassword().equals(dto.getReNewPassword())) {
            authOptional.get().setPassword(CodeGenerator.encode(dto.getNewPassword()));
            authOptional.get().setStatus(EStatus.ACTIVE);
            save(authOptional.get());
        } else {
            throw new AuthServiceException(EErrorType.PASSWORD_UNMATCHED);
        }
        return true;
    }

    public String login(LoginRequestDto dto) {
        String hashedPassword = CodeGenerator.encode(dto.getPassword());
        dto.setPassword(hashedPassword);
        Optional<Auth> auth = repository.findOptionalByEmailIgnoreCaseAndPassword(dto.getEmail(), dto.getPassword());
        if (auth.isEmpty())
            throw new AuthServiceException(EErrorType.LOGIN_ERROR_USERNAME_PASSWORD);
        if (auth.get().getStatus().equals(EStatus.PASSIVE))
            throw new AuthServiceException(EErrorType.STATUS_NOT_ACTIVE);
        if(auth.get().getStatus().equals(EStatus.DELETED))
            throw new AuthServiceException(EErrorType.AUTH_DELETED);
        Optional<String> token = Optional.of(tokenManager.createToken(auth.get().getId(),auth.get().getRole(), auth.get().getStatus()).get());
        if(token.isEmpty())
            throw new AuthServiceException(EErrorType.TOKEN_NOT_CREATED);
        return token.get();
    }

    public Boolean deleteAuth (Long id) {
        Optional<Auth> auth = findById(id);
        if (auth.isEmpty())
            throw new AuthServiceException(EErrorType.AUTH_NOT_FOUND);
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        return true;
    }

    public Boolean updateAuth(UpdateRoleRequestDto dto) {
        Optional <Long> id= tokenManager.getIdFromToken(dto.getToken());
        if(id.isEmpty())
            throw new AuthServiceException(EErrorType.INVALID_TOKEN);
        Optional<Auth> tokenOwner = findById(id.get());
        if(tokenOwner.isEmpty())
            throw new AuthServiceException(EErrorType.AUTH_NOT_FOUND);
        if(!tokenOwner.get().getEmail().equals("volkangenel@hotmail.com")) {
            throw new AuthServiceException(EErrorType.UNAUTHORIZED_REQUEST);
        }
        Optional<Auth> auth = repository.findByEmail(dto.getEmail());
        if (auth.isEmpty())
            throw new AuthServiceException(EErrorType.AUTH_NOT_FOUND);
        auth.get().setRole(dto.getERole());
        auth.get().setStatus(dto.getEStatus());
        update(auth.get());
        return true;
    }

    public List<FindAllResponseDto> findAllDto() {
        return findAll().stream().map(x-> IAuthMapper.INSTANCE.toFindAllResponseDto(x)).toList();
    }
}
