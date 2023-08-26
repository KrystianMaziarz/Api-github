package com.pl.gtihub.api.Api.github.gtihub;

import com.pl.gtihub.api.Api.github.gtihub.dto.BranchDto;
import com.pl.gtihub.api.Api.github.gtihub.dto.FinalResponse;
import java.util.List;

public class GithubDataFactory {

  public static final String VALID_HEADER = "application/json";

  static List<FinalResponse> getFinalResponseDto() {
    return List.of(
        new FinalResponse(
            "Horus",
            "KrystianMaziarz",
            List.of(
                getBranchResponse("developer", "081901061a5e52e3c7e37224515005771f9d3a4e"),
                getBranchResponse("refactoring_V2", "6cb93a74a20d21b372384e1e4fd5bca888382006"),
                getBranchResponse("refactoring", "d8c4cd6295acc963494fb3537a808269bc49939d"))),
        new FinalResponse(
            "microservice_parent",
            "KrystianMaziarz",
            List.of(
                getBranchResponse("developer", "762dc4801888590c3772a102b38f665a1ae294d5"),
                getBranchResponse("master", "d32c199d62e14a0e1c91889698608d1db70d2ed4"))));
  }

  static BranchDto getBranchResponse(String name, String sha) {

    return new BranchDto(name, sha);
  }
}
