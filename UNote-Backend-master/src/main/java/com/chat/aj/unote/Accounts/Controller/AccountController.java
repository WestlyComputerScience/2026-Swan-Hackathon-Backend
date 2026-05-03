package com.chat.aj.unote.Accounts.Controller;

import com.chat.aj.unote.Accounts.Dto.AccountsDto;
import com.chat.aj.unote.Accounts.Entity.Accounts;
import com.chat.aj.unote.Accounts.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Accounts.Service.IAccountService;
import com.chat.aj.unote.Accounts.request.AccountUpdateRequest;
import com.chat.aj.unote.Accounts.response.ApiResponse;
import com.chat.aj.unote.Accounts.security.accounts.AccountsDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/accounts")
public class AccountController {
    private final IAccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountsDetails userDetails = (AccountsDetails) auth.getPrincipal();
        Accounts acc = accountService.getAccountById(userDetails.getId());
        AccountsDto dto = accountService.convertToDto(acc);
        return ResponseEntity.ok(new ApiResponse("Find account success!", dto));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateAccount(@RequestBody AccountUpdateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountsDetails userDetails = (AccountsDetails) auth.getPrincipal();
        Accounts acc = accountService.updateAccount(request, userDetails.getId());
        AccountsDto accDto = accountService.convertToDto(acc);
        return ResponseEntity.ok(new ApiResponse("Update account success!", accDto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountsDetails userDetails = (AccountsDetails) auth.getPrincipal();
        accountService.deleteAccount(userDetails.getId());
        return ResponseEntity.ok(new ApiResponse("Delete account success!", null));
    }
}