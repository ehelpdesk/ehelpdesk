package com.crm.ehelpdesk.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = MemberInfo.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MemberInfo implements Serializable {
  public static final String TABLE_NAME = "member_info";

  @Id
  @Column(name = "member_info_id")
  @GeneratedValue
  private Long memberInfoId;

  @Column(name = "name")
  private String name;

  @Column(name = "email_id")
  private String emailId;

  @Column(name = "mobile_no")
  private String mobileNo;

  @Column(name = "residential_address")
  private String residentialAddress;

  @Column(name = "location")
  private String location;


  public Long getMemberInfoId() {
    return memberInfoId;
  }

  public void setMemberInfoId(Long memberInfoId) {
    this.memberInfoId = memberInfoId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmailId() {
    return emailId;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getResidentialAddress() {
    return residentialAddress;
  }

  public void setResidentialAddress(String residentialAddress) {
    this.residentialAddress = residentialAddress;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MemberInfo that = (MemberInfo) o;
    return Objects.equals(memberInfoId, that.memberInfoId) &&
      Objects.equals(name, that.name) &&
      Objects.equals(emailId, that.emailId) &&
      Objects.equals(mobileNo, that.mobileNo) &&
      Objects.equals(residentialAddress, that.residentialAddress) &&
      Objects.equals(location, that.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(memberInfoId, name, emailId, mobileNo, residentialAddress, location);
  }
}
