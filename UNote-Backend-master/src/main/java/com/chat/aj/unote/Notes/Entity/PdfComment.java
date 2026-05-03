package com.chat.aj.unote.Notes.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pdf_comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PdfComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private int x;
    private int y;
    private int width;
    private int height;
    private int pageNumber;

    @Column(nullable = false)
    private String username;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id", nullable = false)
    private Notes note;
}
