package com.pl.gtihub.api.Api.github.gtihub.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepositoryResponse {

  @JsonProperty("name")
  String repositoryName;

  @JsonProperty("owner")
  OwnerDto ownerLogin;

  @JsonProperty("fork")
  boolean isFork;
}
