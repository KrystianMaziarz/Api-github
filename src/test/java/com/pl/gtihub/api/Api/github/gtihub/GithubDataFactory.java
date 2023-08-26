package com.pl.gtihub.api.Api.github.gtihub;

import com.pl.gtihub.api.Api.github.gtihub.dto.BranchDto;
import com.pl.gtihub.api.Api.github.gtihub.dto.ResponseDto;
import java.util.List;

public class GithubDataFactory {

  public static final String VALID_HEADER = "application/json";

  static List<ResponseDto> getFinalResponseDto() {
    return List.of(
        new ResponseDto(
            "Horus",
            "KrystianMaziarz",
            List.of(
                new BranchDto("developer", "081901061a5e52e3c7e37224515005771f9d3a4e"),
                new BranchDto("refactoring_V2", "6cb93a74a20d21b372384e1e4fd5bca888382006"),
                new BranchDto("refactoring", "d8c4cd6295acc963494fb3537a808269bc49939d"))),
        new ResponseDto(
            "microservice_parent",
            "KrystianMaziarz",
            List.of(
                new BranchDto("developer", "762dc4801888590c3772a102b38f665a1ae294d5"),
                new BranchDto("master", "d32c199d62e14a0e1c91889698608d1db70d2ed4"))));
  }
}
