package com.pl.gtihub.api.Api.github.gtihub.dto;

import lombok.*;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class BranchDto {

  String name;

  String sha;
}
