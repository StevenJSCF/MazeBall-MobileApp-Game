package com.example.User_Login.Accounts;

import com.example.User_Login.Map.Map;
import jakarta.persistence.*;

@Entity
@Table(name = "account_map")
public class AccountMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Map map;

    // Add any additional fields you need for this relationship

    // Constructors, getters, and setters
}