package com.chat.aj.unote.Accounts.Controller;

import com.chat.aj.unote.Accounts.Dto.AccountsDto;
import com.chat.aj.unote.Accounts.Entity.Accounts;
import com.chat.aj.unote.Accounts.Exceptions.AlreadyExistsException;
import com.chat.aj.unote.Accounts.Service.IAccountService;
import com.chat.aj.unote.Accounts.request.AccountLoginRequest;
import com.chat.aj.unote.Accounts.request.CreateAccountRequest;
import com.chat.aj.unote.Accounts.response.ApiResponse;
import com.chat.aj.unote.Accounts.security.accounts.AccountsDetails;
import com.chat.aj.unote.Accounts.security.jwt.JwtAuthenticationResponse;
import com.chat.aj.unote.Accounts.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CONFLICT;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthenticationController {
    private final IAccountService accountService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody AccountLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AccountsDetails userDetails = (AccountsDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody CreateAccountRequest request) {
        try {
            Accounts acc = accountService.createAccount(request);
            AccountsDto accDto = accountService.convertToDto(acc);
            return ResponseEntity.ok(new ApiResponse("Created account successfully!", accDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
