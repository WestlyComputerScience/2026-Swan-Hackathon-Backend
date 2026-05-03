package com.chat.aj.unote.Accounts.Service;

import com.chat.aj.unote.Accounts.Dto.AccountsDto;
import com.chat.aj.unote.Accounts.Entity.Accounts;
import com.chat.aj.unote.Accounts.Exceptions.AlreadyExistsException;
import com.chat.aj.unote.Accounts.Exceptions.ResourceNotFoundException;
import com.chat.aj.unote.Accounts.request.AccountUpdateRequest;
import com.chat.aj.unote.Accounts.request.CreateAccountRequest;

public interface IAccountService {
    public Accounts getAccountById(Long accountId) throws ResourceNotFoundException;
    public Accounts createAccount(CreateAccountRequest request) throws AlreadyExistsException;
    public Accounts updateAccount(AccountUpdateRequest request, Long accountId) throws ResourceNotFoundException;
    public void deleteAccount(Long accountId) throws ResourceNotFoundException;
    public AccountsDto convertToDto(Accounts acc);
    public Accounts getAuthenticatedAccount();
}
