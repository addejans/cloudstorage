package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;

    private EncryptionService encryptionService;

    public CredentialService(EncryptionService encryptionService, CredentialMapper credentialMapper) {
        this.encryptionService = encryptionService;
        this.credentialMapper = credentialMapper;
    }

    public void addCredential(Credential credential) {
        String key = this.encryptionService.generateKey();
        credential.setKey(key);
        credential.setPassword(this.encryptPassword(credential));
        this.credentialMapper.insert(credential);
    }

    public List<Credential> getCredentialByUserId(Integer id) {
        return this.credentialMapper.getAllCredentialByUserId(id);
    }

    public void deleteCredential(Integer id) {
        this.credentialMapper.delete(id);
    }

    public void editCredential(Credential credential) {
        Credential cred = this.credentialMapper.retrieveKeyByCredentialId(credential.getCredentialId());
        credential.setKey(cred.getKey());
        String encodedPassword = this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encodedPassword);
        this.credentialMapper.update(credential);
    }

    public String encryptPassword(Credential credential) {
        return this.encryptionService.encryptValue(credential.getPassword(), credential.getKey());
    }


}
