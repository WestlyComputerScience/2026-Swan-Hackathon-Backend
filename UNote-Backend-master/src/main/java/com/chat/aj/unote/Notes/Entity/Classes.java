package com.chat.aj.unote.Notes.Entity;

import com.chat.aj.unote.Accounts.Entity.Accounts;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classes")
@Getter
@Setter
@NoArgsConstructor
public class Classes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = true)
    private String professor;

    @Column(nullable = true)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private Accounts user;

    @OneToMany(mappedBy = "myClass", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("myClass")
    private List<Unit> units = new ArrayList<>();
}
