package com.officemaneger.areas.log.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    private LocalDateTime date;

    @Basic
    private String username;

    @Column(name = "user_IP")
    private String userIP;

    @Column(name = "class_name")
    private String className;

    @Column(name = "class_method")
    private String classMethod;

    @Column(name = "action_execution")
    private long actionExecution;

    @Column(name = "overall_execution")
    private long overallExecution;

    @Column(name = "is_get_page")
    private boolean isGetPage;

    @Column(name = "is_invalid_credentials")
    private boolean isInvalidCredentials;

    public Log() {
    }

    public Log(String username, String userIP, String className, String classMethod, long actionExecution,
               long overallExecution, boolean isGetPage, boolean isInvalidCredentials) {
        this.username = username;
        this.userIP = userIP;
        this.className = className;
        this.classMethod = classMethod;
        this.actionExecution = actionExecution;
        this.overallExecution = overallExecution;
        this.isGetPage = isGetPage;
        this.isInvalidCredentials = isInvalidCredentials;
        this.date = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public long getActionExecution() {
        return actionExecution;
    }

    public void setActionExecution(long actionExecution) {
        this.actionExecution = actionExecution;
    }

    public long getOverallExecution() {
        return overallExecution;
    }

    public void setOverallExecution(long overallExecution) {
        this.overallExecution = overallExecution;
    }

    public boolean getIsGetPage() {
        return isGetPage;
    }

    public void setIsGetPage(boolean isGetPage) {
        this.isGetPage = isGetPage;
    }

    public boolean getIsInvalidCredentials() {
        return isInvalidCredentials;
    }

    public void setInvalidCredentials(boolean isInvalidCredentials) {
        this.isInvalidCredentials = isInvalidCredentials;
    }
}
