package com.company.erp.userManagement.dto;
public class UserDto {
    private Long id;
    private String name;
    private String department;
    private String role;
    private String userName;
    public UserDto() {}
    public UserDto(Long id, String name, String department, String role, String userName) {
        this.id = id; this.name = name; this.department = department; this.role = role; this.userName = userName;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
