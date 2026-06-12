package com.company.erp.userManagement.dto;
public class CurrentUserResponseDto {
    private Long id;
    private String name;
    private String role;
    private String department;
    public CurrentUserResponseDto() {}
    public CurrentUserResponseDto(Long id, String name, String role, String department) {
        this.id = id; this.name = name; this.role = role; this.department = department;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
