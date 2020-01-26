export interface IUser {
    id?: any;
    login?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    activated?: boolean;
    langKey?: string;
    authority?: string;
    createdBy?: string;
    createdDate?: Date;
    lastModifiedBy?: string;
    lastModifiedDate?: Date;
    password?: string;
    owner?: string;
    accessibleStates?: any[];
}

export class User implements IUser {
    constructor(
        public id?: any,
        public login?: string,
        public firstName?: string,
        public lastName?: string,
        public email?: string,
        public activated?: boolean,
        public langKey?: string,
        public authority?: string,
        public createdBy?: string,
        public createdDate?: Date,
        public lastModifiedBy?: string,
        public lastModifiedDate?: Date,
        public password?: string,
        public owner?: string,
        public accessibleStates?: any[]
    ) {
        this.id = id ? id : null;
        this.login = login ? login : null;
        this.firstName = firstName ? firstName : null;
        this.lastName = lastName ? lastName : null;
        this.email = email ? email : null;
        this.activated = activated ? activated : false;
        this.langKey = langKey ? langKey : null;
        this.authority = authority ? authority : null;
        this.createdBy = createdBy ? createdBy : null;
        this.createdDate = createdDate ? createdDate : null;
        this.lastModifiedBy = lastModifiedBy ? lastModifiedBy : null;
        this.lastModifiedDate = lastModifiedDate ? lastModifiedDate : null;
        this.password = password ? password : null;
        this.owner = owner ? owner : null;
        this.accessibleStates = accessibleStates ? accessibleStates : null;
    }
}
