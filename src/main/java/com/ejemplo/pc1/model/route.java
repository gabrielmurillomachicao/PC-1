package com.ejemplo.pc1.model;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;
import org.apache.catalina.User;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User driver;
    private String origin;
    private String destination;
    private Routestatus status;
}
