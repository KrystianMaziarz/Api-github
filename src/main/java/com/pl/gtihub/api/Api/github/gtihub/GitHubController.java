package com.pl.gtihub.api.Api.github.gtihub;

import com.pl.gtihub.api.Api.github.gtihub.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GitHubController {

  private final GitHubServiceImpl githubService;

  @GetMapping(value = "/repositories/{username}")
  @Operation(description = "Get a list of all repositories")
  public List<ResponseDto> getRepositories(
      @PathVariable @Schema(description = "Github username", example = "KrystianMaziarz")
          String username,
      @RequestHeader(value = HttpHeaders.ACCEPT)
          @Schema(description = "request header", example = "application/json")
          String acceptHeader) {
    return githubService.getRepositories(username, acceptHeader);
  }
}
