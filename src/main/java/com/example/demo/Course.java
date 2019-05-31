package com.example.demo;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min=4)
    private String title;

    @NotNull
    @Size(min=10)
    private String description;

    @NotNull
    @Min(3)
    private int credit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

   @ManyToMany(mappedBy = "courses", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   private Collection<User> users;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }


    public Collection<User> getUsers() {
       return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}