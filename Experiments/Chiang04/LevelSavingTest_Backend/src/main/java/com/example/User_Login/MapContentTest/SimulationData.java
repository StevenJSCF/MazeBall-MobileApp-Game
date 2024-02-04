package com.example.User_Login.MapContentTest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "MapContentTEST")
public class SimulationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("Bodies")
    private String Bodies;

    @JsonProperty("listOfCircleBodies")
    private String listOfCircleBodies;

    @JsonProperty("world")
    private String world;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBodies() {
        return Bodies;
    }

    public void setBodies(String bodies) {
        Bodies = bodies;
    }

    public String getListOfCircleBodies() {
        return listOfCircleBodies;
    }

    public void setListOfCircleBodies(String listOfCircleBodies) {
        this.listOfCircleBodies = listOfCircleBodies;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }}