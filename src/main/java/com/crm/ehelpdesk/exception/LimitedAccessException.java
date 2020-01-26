package com.crm.ehelpdesk.exception;

import com.crm.ehelpdesk.constants.ErrorConstants;

public class LimitedAccessException extends BadRequestAlertException {

  public LimitedAccessException(Object state) {
    super(ErrorConstants.LIMITED_STATE_ACCESS, "It Seems you Don't have access to "+state+" state. " +
      "Please contact Admin for Further Details", "mainQuerySearch", "limitedstateaccess");
  }

}
