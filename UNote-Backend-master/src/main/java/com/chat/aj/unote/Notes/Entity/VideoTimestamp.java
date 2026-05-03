package com.chat.aj.unote.Notes.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video_timestamp")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private double timestamp;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Notes note;
}