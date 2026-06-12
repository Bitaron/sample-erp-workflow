package com.company.erp.userManagement.dto;
public class RegisterUserResponseDto {
    private Long id;
    private String name;
    private Long departmentId;
    private Long roleId;
    private String userName;
    public RegisterUserResponseDto() {}
    public RegisterUserResponseDto(Long id, String name, Long departmentId, Long roleId, String userName) {
        this.id = id; this.name = name; this.departmentId = departmentId; this.roleId = roleId; this.userName = userName;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
