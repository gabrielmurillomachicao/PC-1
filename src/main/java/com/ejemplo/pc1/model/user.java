package com.ejemplo.pc1.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class user {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 @Column(nullable = false, unique = true)
 private String username;
 @Column(nullable = false, unique = true)
 private String email;
 @Column(nullable = false)
 private String password;
 @Builder.Default
 @Column(nullable = false)
 private String role= "ROLE_ADMIN";
}