package com.example.User_Login.MapContentTest2;


import jakarta.persistence.*;

@Entity
@Table(name = "Map_Listbodies")
public class CircleBody {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int listOfCircleID;
    private String type;
    private double linearVelocity_x;
    private double linearVelocity_y;
    private double angularVelocity;
    private double linearDamping;
    private double angularDamping;
    private double gravityScale;
    private double position_x;
    private double position_y;
    private double angle;

    public int getListOfCircleID() {
        return listOfCircleID;
    }

    public void setListOfCircleID(int listOfCircleID) {
        this.listOfCircleID = listOfCircleID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLinearVelocity_x() {
        return linearVelocity_x;
    }

    public void setLinearVelocity_x(double linearVelocity_x) {
        this.linearVelocity_x = linearVelocity_x;
    }

    public double getLinearVelocity_y() {
        return linearVelocity_y;
    }

    public void setLinearVelocity_y(double linearVelocity_y) {
        this.linearVelocity_y = linearVelocity_y;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public double getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(double linearDamping) {
        this.linearDamping = linearDamping;
    }

    public double getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(double angularDamping) {
        this.angularDamping = angularDamping;
    }

    public double getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(double gravityScale) {
        this.gravityScale = gravityScale;
    }

    public double getPosition_x() {
        return position_x;
    }

    public void setPosition_x(double position_x) {
        this.position_x = position_x;
    }

    public double getPosition_y() {
        return position_y;
    }

    public void setPosition_y(double position_y) {
        this.position_y = position_y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
