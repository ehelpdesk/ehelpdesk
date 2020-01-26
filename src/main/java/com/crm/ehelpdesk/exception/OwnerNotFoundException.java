package com.crm.ehelpdesk.exception;

import com.crm.ehelpdesk.constants.ErrorConstants;

public class OwnerNotFoundException extends BadRequestAlertException {

  public OwnerNotFoundException() {
    super(ErrorConstants.OWNER_NOT_FOUND, "Owner Not Found", "userManagement", "ownernotfound");
  }

}
