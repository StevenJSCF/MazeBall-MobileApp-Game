package com.example.User_Login.MapContentTest2;


import jakarta.persistence.*;

@Entity
@Table(name = "Map_World")
public class WorldData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int WorldDataID;
    private double gravity_x;
    private double gravity_y;
    private boolean allow_sleep;
    private boolean warm_starting;
    private boolean continuous_physics;
    private boolean sub_stepping;
    private int body_count;
    private int joint_count;

    // getters and setters

    public int getWorldDataID() {
        return WorldDataID;
    }

    public void setWorldDataID(int worldDataID) {
        WorldDataID = worldDataID;
    }

    public double getGravity_x() {
        return gravity_x;
    }

    public void setGravity_x(double gravity_x) {
        this.gravity_x = gravity_x;
    }

    public double getGravity_y() {
        return gravity_y;
    }

    public void setGravity_y(double gravity_y) {
        this.gravity_y = gravity_y;
    }

    public boolean isAllow_sleep() {
        return allow_sleep;
    }

    public void setAllow_sleep(boolean allow_sleep) {
        this.allow_sleep = allow_sleep;
    }

    public boolean isWarm_starting() {
        return warm_starting;
    }

    public void setWarm_starting(boolean warm_starting) {
        this.warm_starting = warm_starting;
    }

    public boolean isContinuous_physics() {
        return continuous_physics;
    }

    public void setContinuous_physics(boolean continuous_physics) {
        this.continuous_physics = continuous_physics;
    }

    public boolean isSub_stepping() {
        return sub_stepping;
    }

    public void setSub_stepping(boolean sub_stepping) {
        this.sub_stepping = sub_stepping;
    }

    public int getBody_count() {
        return body_count;
    }

    public void setBody_count(int body_count) {
        this.body_count = body_count;
    }

    public int getJoint_count() {
        return joint_count;
    }

    public void setJoint_count(int joint_count) {
        this.joint_count = joint_count;
    }
}