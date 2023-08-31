package com.tc.brewery.entity;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class JwtResponse {
    private String jwrToken;
    private String username;
    private Long userId;
}
