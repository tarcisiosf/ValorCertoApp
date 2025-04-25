package br.com.valorcerto.app.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(tableName = "users", indices = {@Index(value = "email", unique = true)}) //índice único em email
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userId;
    private String name;
    private String email;
    private String passwordHash;

    public User(){
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}