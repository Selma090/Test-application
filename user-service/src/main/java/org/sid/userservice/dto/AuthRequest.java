package org.sid.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private Long id;
    private String id_stellantis;
    private String nom;
    private String prenom;
    private String email;
    private String password;
}
