package service;


import com.volkan.dto.request.LoginRequestDto;
import com.volkan.dto.request.RegisterRequestDto;
import com.volkan.dto.request.ResetPasswordRequestDto;
import com.volkan.dto.request.UpdateRoleRequestDto;
import com.volkan.dto.response.FindAllResponseDto;
import com.volkan.exception.AuthServiceException;
import com.volkan.exception.EErrorType;
import com.volkan.rabbitmq.model.ResetPasswordModel;
import com.volkan.rabbitmq.producer.ResetPasswordProducer;
import com.volkan.repository.IAuthRepository;
import com.volkan.repository.entity.Auth;
import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import com.volkan.service.AuthService;
import com.volkan.utility.CodeGenerator;
import com.volkan.utility.JwtTokenManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private IAuthRepository repository;

    @Mock
    private ResetPasswordProducer resetPasswordProducer;

    @Mock
    private JwtTokenManager tokenManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegisterSuccess() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("register@gmail.com");
        dto.setPassword("123456Aa");
        Auth mockedAuth = Auth.builder().email(dto.getEmail()).password(CodeGenerator.encode(dto.getPassword())).build();
        when(repository.isEmail(anyString())).thenReturn(false);
        Auth result = authService.register(dto);

        assertNotNull(result);
        assertEquals(mockedAuth, result);
       Mockito.verify(repository, times(1)).save(any(Auth.class));
    }

    @Test
    public void testRegisterErrorEmail() {
        // Arrange
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("register@gmail.com");
        dto.setPassword("123456Aa");

        when(repository.isEmail(anyString())).thenReturn(true);

        AuthServiceException exception = assertThrows(AuthServiceException.class, () -> authService.register(dto));
        assertEquals(EErrorType.REGISTER_ERROR_EMAIL, exception.getErrorType());
        Mockito.verify(repository, never()).save(any(Auth.class));
    }

    @Test
    public void testForgotMyPassword() {
        String email = "register@gmail.com";
        Auth auth = new Auth();
        auth.setEmail(email);
        auth.setStatus(EStatus.ACTIVE);
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(auth));

        String result = authService.forgotMyPassword(email);

        verify(repository, times(1)).findByEmail(email);
        verify(resetPasswordProducer, times(1)).sendNewPassword(any(ResetPasswordModel.class));

        assertNotNull(auth);
        assertEquals(auth.getStatus(),EStatus.PASSIVE);
        assertEquals(result,"Password has been sent by e-mail, password needs to be updated for activating the profile!");

    }

    @Test
    public void testForgotMyPassword_EmailNotFound () {
        String email = "register@gmail.com";
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(AuthServiceException.class, () -> authService.forgotMyPassword(email));

        verify(resetPasswordProducer, never()).sendNewPassword(any(ResetPasswordModel.class));
    }

    @Test
    public void testForgotMyPassword_AuthDeleted () {
        String email = "register@gmail.com";
        Auth auth = Auth.builder().email(email).status(EStatus.DELETED).build();
        Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(auth));
        assertThrows(AuthServiceException.class, () -> authService.forgotMyPassword(email));
        verify(resetPasswordProducer, never()).sendNewPassword(any(ResetPasswordModel.class));
    }

    @Test
    public void testResetPassword() {
        String email = "register@gmail.com";
        String password = CodeGenerator.encode("Aa123456");
        ResetPasswordRequestDto dto = ResetPasswordRequestDto.builder()
                .email(email)
                .password(password)
                .newPassword(password)
                .reNewPassword(password)
                .build();
        Auth auth = Auth.builder().status(EStatus.PASSIVE).password(password).build();

        Mockito.when(repository.findOptionalByEmailIgnoreCaseAndPassword(anyString(),anyString())).thenReturn(Optional.of(auth));

        Boolean result = authService.resetPassword(dto);
        verify(repository, times(1)).save(auth);

        Assertions.assertTrue(result);
    }
    @Test
    public void testResetPassword_LoginError() {
        Mockito.when(repository.findOptionalByEmailIgnoreCaseAndPassword(anyString(),anyString())).thenReturn(Optional.empty());
        String email = "register@gmail.com";
        String password = CodeGenerator.encode("Aa123456");
        ResetPasswordRequestDto dto = ResetPasswordRequestDto.builder()
                .email(email)
                .password(password)
                .newPassword(password)
                .reNewPassword(password)
                .build();
        assertThrows(AuthServiceException.class, () -> authService.resetPassword(dto));
    }

    @Test
    public void testResetPassword_AuthDeleted() {
        String email = "register@gmail.com";
        String password = CodeGenerator.encode("Aa123456");
        ResetPasswordRequestDto dto = ResetPasswordRequestDto.builder()
                .email(email)
                .password(password)
                .newPassword(password)
                .reNewPassword(password)
                .build();
        Auth auth = Auth.builder().status(EStatus.DELETED).password(password).build();
        Mockito.when(repository.findOptionalByEmailIgnoreCaseAndPassword(anyString(),anyString())).thenReturn(Optional.of(auth));
        assertThrows(AuthServiceException.class, () -> authService.resetPassword(dto));
    }

    @Test
    public void testResetPassword_PasswordUnmatched() {
        String email = "register@gmail.com";
        String password = CodeGenerator.encode("Aa123456");
        ResetPasswordRequestDto dto = ResetPasswordRequestDto.builder()
                .email(email)
                .password(password)
                .newPassword(password)
                .reNewPassword("AA123456")
                .build();
        Auth auth = Auth.builder().status(EStatus.ACTIVE).password(password).build();
        Mockito.when(repository.findOptionalByEmailIgnoreCaseAndPassword(anyString(),anyString())).thenReturn(Optional.of(auth));
        assertThrows(AuthServiceException.class, () -> authService.resetPassword(dto));
    }

    @Test
    public void testLogin_LoginErrorUsernamePassword() {
        String email = "register@gmail.com";
        String password = "Aa123456";
        LoginRequestDto dto = LoginRequestDto.builder().email(email).password(password).build();
        Mockito.when(repository.findOptionalByEmailIgnoreCaseAndPassword(email,CodeGenerator.encode(password))).thenReturn(Optional.empty());
        assertThrows(AuthServiceException.class, () -> authService.login(dto));
    }
    @Test
    public void testLogin_StatusNotActive() {
        String email = "register@gmail.com";
        String password = "Aa123456";
        Auth auth = Auth.builder().status(EStatus.PASSIVE).password(password).build();
        LoginRequestDto dto = LoginRequestDto.builder().email(email).password(password).build();
        Mockito.when(repository.findOptionalByEmailIgnoreCaseAndPassword(email,CodeGenerator.encode(password))).thenReturn(Optional.of(auth));
        assertThrows(AuthServiceException.class, () -> authService.login(dto));
    }

    @Test
    public void testLogin_AuthDeleted() {
        String email = "register@gmail.com";
        String password = "Aa123456";
        Auth auth = Auth.builder().status(EStatus.DELETED).password(password).build();
        LoginRequestDto dto = LoginRequestDto.builder().email(email).password(password).build();
        Mockito.when(repository.findOptionalByEmailIgnoreCaseAndPassword(email,CodeGenerator.encode(password))).thenReturn(Optional.of(auth));
        assertThrows(AuthServiceException.class, () -> authService.login(dto));
    }

    @Test
    public void testLogin() {
        String email = "register@gmail.com";
        String password = "Aa123456";
        Long id = 1L;
        LoginRequestDto dto = LoginRequestDto.builder().email(email).password(password).build();
        Optional<Auth> auth = Optional.of(Auth.builder().id(id).email(email).role(ERole.USER_ROLE).status(EStatus.ACTIVE).password(password).build());
        Mockito.when(repository.findOptionalByEmailIgnoreCaseAndPassword(email,CodeGenerator.encode(password))).thenReturn(auth);

        Optional<String> token = Optional.of("ABCDE");
        when(tokenManager.createToken(auth.get().getId(),auth.get().getRole(), auth.get().getStatus())).thenReturn(Optional.of(token.get()));

        assertEquals( token.get(),authService.login(dto));
        verify(tokenManager, times(1)).createToken(auth.get().getId(), auth.get().getRole(), auth.get().getStatus());
        verifyNoMoreInteractions(tokenManager);
    }

    @Test
    public void testDeleteAuth() {
        Long id = 1L;
        Auth auth = new Auth();
        auth.setId(id);
        auth.setStatus(EStatus.ACTIVE);

        when(repository.findById(id)).thenReturn(Optional.of(auth));

        Boolean result = authService.deleteAuth(id);

        verify(repository, times(1)).findById(id);

        assertTrue(result);
        assertEquals(EStatus.DELETED, auth.getStatus());
    }

    @Test
    public void testDelete_AuthNotFound() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AuthServiceException.class, () -> authService.deleteAuth(id));

        verify(repository, times(1)).findById(id);
    }

    @Test
    public void testUpdateAuthInvalidToken() {
        ERole role = ERole.USER_ROLE;
        String email = "volkangenel@hotmail.com";
        EStatus status = EStatus.ACTIVE;
        String token = "ABCDE";
        UpdateRoleRequestDto dto = UpdateRoleRequestDto.builder()
                .eRole(role)
                .email(email)
                .eStatus(status)
                .token(token)
                .build();
        when(tokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.empty());
        assertThrows(AuthServiceException.class, () -> authService.updateAuth(dto));
    }

    @Test
    public void testUpdateAuth_AuthNotFound() {
        ERole role = ERole.USER_ROLE;
        String email = "volkangenel@hotmail.com";
        EStatus status = EStatus.ACTIVE;
        String token = "ABCDE";
        Optional<Long> id = Optional.of(1L);
        UpdateRoleRequestDto dto = UpdateRoleRequestDto.builder()
                .eRole(role)
                .email(email)
                .eStatus(status)
                .token(token)
                .build();
        when(tokenManager.getIdFromToken(dto.getToken())).thenReturn(id);
        when(authService.findById(id.get())).thenReturn(Optional.empty());
        assertThrows(AuthServiceException.class, () -> authService.updateAuth(dto));
    }

    @Test
    public void testUpdateAuth_UnauthorizedRequest() {
        ERole role = ERole.USER_ROLE;
        String email = "volkangenel@gmail.com";
        EStatus status = EStatus.ACTIVE;
        String token = "ABCDE";
        Optional<Long> id = Optional.of(1L);
        UpdateRoleRequestDto dto = UpdateRoleRequestDto.builder()
                .eRole(role)
                .email(email)
                .eStatus(status)
                .token(token)
                .build();
        Auth auth = Auth.builder().email(email).build();

        when(tokenManager.getIdFromToken(dto.getToken())).thenReturn(id);
        when(authService.findById(id.get())).thenReturn(Optional.of(auth));
        assertThrows(AuthServiceException.class, () -> authService.updateAuth(dto));
    }

    @Test
    public void testUpdateAuth_EmailNotFound() {
        ERole role = ERole.USER_ROLE;
        String email = "volkangenel@hotmail.com";
        EStatus status = EStatus.ACTIVE;
        String token = "ABCDE";
        Optional<Long> id = Optional.of(1L);
        UpdateRoleRequestDto dto = UpdateRoleRequestDto.builder()
                .eRole(role)
                .email(email)
                .eStatus(status)
                .token(token)
                .build();
        Auth auth = Auth.builder().email(email).build();

        when(tokenManager.getIdFromToken(dto.getToken())).thenReturn(id);
        when(authService.findById(id.get())).thenReturn(Optional.of(auth));
        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        assertThrows(AuthServiceException.class, () -> authService.updateAuth(dto));
    }

    @Test
    public void testUpdateAuth() {
        ERole role = ERole.USER_ROLE;
        String email = "volkangenel@hotmail.com";
        EStatus status = EStatus.ACTIVE;
        String token = "ABCDE";
        Optional<Long> id = Optional.of(1L);
        UpdateRoleRequestDto dto = UpdateRoleRequestDto.builder()
                .eRole(role)
                .email(email)
                .eStatus(status)
                .token(token)
                .build();
        Auth auth = Auth.builder().email(email).build();

        when(tokenManager.getIdFromToken(dto.getToken())).thenReturn(id);
        when(authService.findById(id.get())).thenReturn(Optional.of(auth));
        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.of(auth));

        assertEquals(true,authService.updateAuth(dto));
    }
    @Test
    public void testFindAllDto() {
        Auth auth = Auth.builder()
                .id(1L)
                .email("example@egmail.com")
                .status(EStatus.ACTIVE).build();

        Auth auth2 = Auth.builder()
                .id(2L)
                .email("example2@egmail.com")
                .status(EStatus.ACTIVE).build();

        List<Auth> authList = new ArrayList<>();
        authList.add(auth);
        authList.add(auth2);

        when(repository.findAll()).thenReturn(authList);

        List<FindAllResponseDto> responseDtoList = authService.findAllDto();

        assertEquals(2, responseDtoList.size());

        FindAllResponseDto responseDto1 = responseDtoList.get(0);
        assertEquals(auth.getId(), responseDto1.getId());
        assertEquals(auth.getEmail(), responseDto1.getEmail());
        assertEquals(auth.getStatus(), responseDto1.getStatus());

        FindAllResponseDto responseDto2 = responseDtoList.get(1);
        assertEquals(auth2.getId(), responseDto2.getId());
        assertEquals(auth2.getEmail(), responseDto2.getEmail());
        assertEquals(auth2.getStatus(), responseDto2.getStatus());

    }



}
