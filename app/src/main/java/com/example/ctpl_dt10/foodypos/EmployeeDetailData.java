package com.example.ctpl_dt10.foodypos;

public class EmployeeDetailData {

    private String name,email,role,active,acctId;

    public EmployeeDetailData(String name, String email, String role, String active) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public EmployeeDetailData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }
}
