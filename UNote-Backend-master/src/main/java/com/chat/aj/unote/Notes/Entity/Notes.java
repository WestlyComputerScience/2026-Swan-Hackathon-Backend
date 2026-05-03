package com.chat.aj.unote.Notes.Entity;


import com.chat.aj.unote.Accounts.Entity.Accounts;
import com.chat.aj.unote.Notes.NoteType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notes")
@Getter
@Setter
public class Notes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteType type;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String fileUrl;

    @Column
    private String fileName;      // original filename

    @Column
    private String fileType;      // MIME type

    @Column
    private Long fileSize;        // in bytes

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    @JsonIgnoreProperties("notes")
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"notes", "password", "createdAt", "updatedAt"})
    private Accounts user;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoTimestamp> timestamps = new ArrayList<>(); // Comment feature for videos
}
