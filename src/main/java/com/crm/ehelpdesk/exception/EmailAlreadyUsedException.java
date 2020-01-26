package com.crm.ehelpdesk.exception;

import com.crm.ehelpdesk.constants.ErrorConstants;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    public EmailAlreadyUsedException() {
      super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }

}
