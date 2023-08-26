package com.pl.gtihub.api.Api.github.gtihub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BranchGithubResponseDto {

  @JsonProperty("name")
  String name;

  @JsonProperty("commit")
  CommitGithubResponseDto commitDto;
}
