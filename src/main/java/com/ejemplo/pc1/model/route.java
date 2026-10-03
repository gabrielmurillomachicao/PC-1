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
    @Transient
    @ManyToOne
    private User driver;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private user driverUser;
    @Column(nullable = false)
    private String origin;
    @Column(nullable = false)
    private String destination;
    @Enumerated(EnumType.STRING)
    private Routestatus status;
}
