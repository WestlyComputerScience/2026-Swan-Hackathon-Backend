package com.chat.aj.unote.Accounts.Service;

import com.chat.aj.unote.Accounts.Dto.AccountsDto;
import com.chat.aj.unote.Accounts.Entity.Accounts;
import com.chat.aj.unote.Accounts.Exceptions.AlreadyExistsException;
import com.chat.aj.unote.Accounts.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Accounts.repository.AccountsRepository;
import com.chat.aj.unote.Accounts.request.AccountUpdateRequest;
import com.chat.aj.unote.Accounts.request.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountsService implements IAccountService {
    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Accounts getAccountById(Long accountId) throws ResourceNotFoundException {
        return accountsRepository.findById(accountId).orElseThrow(
                () -> new ResourceNotFoundException("Account id: " + accountId + " was not found"));
    }

    @Override
    public Accounts createAccount(CreateAccountRequest request) throws AlreadyExistsException {
        if (accountsRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExistsException(
                    "The account " + request.getUsername() + " already exists"
            );
        }

        Accounts acc = new Accounts();
        acc.setUsername(request.getUsername());
        acc.setPassword(passwordEncoder.encode(request.getPassword()));
        return accountsRepository.save(acc);
    }

    @Override
    public Accounts updateAccount(AccountUpdateRequest request, Long accountId) throws ResourceNotFoundException {
        return accountsRepository.findById(accountId).map(
                existingUser ->{
                    existingUser.setUsername(request.getUsername());
                    return accountsRepository.save(existingUser);
                }).orElseThrow(() -> new ResourceNotFoundException("Account id: " + accountId + " was not found"));
    }

    @Override
    public void deleteAccount(Long accountId) throws ResourceNotFoundException {
        accountsRepository.findById(accountId)
                .ifPresentOrElse(accountsRepository::delete,
                        () -> {throw new ResourceNotFoundException("Account id " + accountId + " was not found");
                });
    }

    @Override
    public AccountsDto convertToDto(Accounts acc) {
        if (acc == null) return null;

        AccountsDto dto = new AccountsDto();
        dto.setId(acc.getId());
        dto.setUsername(acc.getUsername());
        return dto;
    }

    @Override
    public Accounts getAuthenticatedAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return accountsRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Account not found with username: " + username));
    }
}
