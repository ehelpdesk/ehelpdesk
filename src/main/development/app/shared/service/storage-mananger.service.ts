import { Injectable } from '@angular/core';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';

@Injectable()
export class StorageManagerService {
    constructor(private $localStorage: LocalStorageService, private $sessionStorage: SessionStorageService) {}

    getSessionStorage(variableName) {
        return this.$sessionStorage.retrieve(variableName);
    }

    setSessionStorage(variableName, variableValue) {
        this.$sessionStorage.store(variableName, variableValue);
    }

    clearSessionStorage(variableName) {
        this.$sessionStorage.clear(variableName);
    }

    getLocalStorage(variableName) {
        return this.$localStorage.retrieve(variableName);
    }

    setLocalStorage(variableName, variableValue) {
        this.$localStorage.store(variableName, variableValue);
    }

    clearLocalStorage(variableName) {
        this.$localStorage.clear(variableName);
    }
}
