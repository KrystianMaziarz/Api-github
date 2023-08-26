package com.pl.gtihub.api.Api.github.gtihub.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class FinalResponse {

  String repositoryName;

  String ownerLogin;

  List<BranchDto> branches;
}
