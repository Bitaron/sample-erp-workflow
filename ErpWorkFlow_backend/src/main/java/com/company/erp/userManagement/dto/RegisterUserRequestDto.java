package com.company.erp.userManagement.dto;
public class RegisterUserRequestDto {
    private String name;
    private Long departmentId;
    private Long roleId;
    private String userName;
    private String password;
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
