package com.crm.ehelpdesk.dto;

public class SearchResultDTO {

  private Object state;
  private Object operator;
  private Object mobile;
  private Object customerName;
  private Object dateOfBirth;
  private Object fatherName;
  private Object localAddress;
  private Object permanentAddress;
  private Object alternateNumber;
  private Object emailAddress;
  private Object uid;
  private Object aadharNumber;
  private Object dateOfActivation;

  public SearchResultDTO(Object state, Object operator, Object mobile, Object customerName, Object dateOfBirth,
                         Object fatherName, Object localAddress, Object permanentAddress, Object alternateNumber,
                         Object emailAddress, Object uid, Object aadharNumber, Object dateOfActivation) {
    this.state = state;
    this.operator = operator;
    this.mobile = mobile;
    this.customerName = customerName;
    this.dateOfBirth = dateOfBirth;
    this.fatherName = fatherName;
    this.localAddress = localAddress;
    this.permanentAddress = permanentAddress;
    this.alternateNumber = alternateNumber;
    this.emailAddress = emailAddress;
    this.uid = uid;
    this.aadharNumber = aadharNumber;
    this.dateOfActivation = dateOfActivation;
  }

  public Object getState() {
    return state;
  }

  public void setState(Object state) {
    this.state = state;
  }

  public Object getOperator() {
    return operator;
  }

  public void setOperator(Object operator) {
    this.operator = operator;
  }

  public Object getMobile() {
    return mobile;
  }

  public void setMobile(Object mobile) {
    this.mobile = mobile;
  }

  public Object getCustomerName() {
    return customerName;
  }

  public void setCustomerName(Object customerName) {
    this.customerName = customerName;
  }

  public Object getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Object dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Object getFatherName() {
    return fatherName;
  }

  public void setFatherName(Object fatherName) {
    this.fatherName = fatherName;
  }

  public Object getLocalAddress() {
    return localAddress;
  }

  public void setLocalAddress(Object localAddress) {
    this.localAddress = localAddress;
  }

  public Object getPermanentAddress() {
    return permanentAddress;
  }

  public void setPermanentAddress(Object permanentAddress) {
    this.permanentAddress = permanentAddress;
  }

  public Object getAlternateNumber() {
    return alternateNumber;
  }

  public void setAlternateNumber(Object alternateNumber) {
    this.alternateNumber = alternateNumber;
  }

  public Object getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(Object emailAddress) {
    this.emailAddress = emailAddress;
  }

  public Object getUid() {
    return uid;
  }

  public void setUid(Object uid) {
    this.uid = uid;
  }

  public Object getAadharNumber() {
    return aadharNumber;
  }

  public void setAadharNumber(Object aadharNumber) {
    this.aadharNumber = aadharNumber;
  }

  public Object getDateOfActivation() {
    return dateOfActivation;
  }

  public void setDateOfActivation(Object dateOfActivation) {
    this.dateOfActivation = dateOfActivation;
  }
}
