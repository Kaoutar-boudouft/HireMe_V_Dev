package com.example.hireme.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="medias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entity;
    private Long entityId;
    private String type;
    private String path;

    public Media(String entity, Long entity_id, String type, String path) {
        this.entity = entity;
        this.entityId = entity_id;
        this.type = type;
        this.path = path;
    }

    public Media(String entity, Long entity_id, String type) {
        this.entity = entity;
        this.entityId = entity_id;
        this.type = type;
    }
}
