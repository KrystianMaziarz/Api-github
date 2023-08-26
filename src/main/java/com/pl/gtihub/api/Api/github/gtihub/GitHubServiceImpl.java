package com.pl.gtihub.api.Api.github.gtihub;

import com.pl.gtihub.api.Api.github.gtihub.dto.*;
import com.pl.gtihub.api.Api.github.gtihub.exception.ServerNotRespondingException;
import com.pl.gtihub.api.Api.github.gtihub.exception.UserNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubServiceImpl implements GitHubService {

  private final RestTemplate restTemplate;

  private final HeaderValidator headerValidator;

  public List<FinalResponse> getRepositories(String userName, String acceptHeader) {

    headerValidator.checkHeader(acceptHeader);

    var apiUrl = "https://api.github.com/users/{userName}/repos";
    final var uri =
        UriComponentsBuilder.fromUriString(apiUrl)
            .uriVariables(Map.of("userName", userName))
            .build()
            .toUriString();

    try {
      var response =
          restTemplate.exchange(
              uri,
              HttpMethod.GET,
              null,
              new ParameterizedTypeReference<List<RepositoryResponse>>() {});

      return Optional.ofNullable(response.getBody())
          .map(this::processResponse)
          .orElse(Collections.emptyList());

    } catch (HttpClientErrorException | HttpServerErrorException e) {
      HttpStatusCode statusCode = e.getStatusCode();
      if (statusCode instanceof HttpStatus status)
        switch (status) {
          case NOT_FOUND -> throw new UserNotFoundException(userName);
          case INTERNAL_SERVER_ERROR -> throw new ServerNotRespondingException(
              "Server not responding");
          default -> throw new ServerNotRespondingException("Unexpected error occurred");
        }
      throw new RuntimeException();
    }
  }

  public List<BranchDto> getBranches(String username, String repoName) {
    var apiUrl = "https://api.github.com/repos/{username}/{repoName}/branches";
    final var uri =
        UriComponentsBuilder.fromUriString(apiUrl)
            .uriVariables(Map.of("username", username, "repoName", repoName))
            .build()
            .toUriString();

    var response =
        restTemplate.exchange(
            uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<BranchResponse>>() {});

    return Optional.ofNullable(response.getBody())
        .map(
            body ->
                body.stream()
                    .map(
                        branchResponse ->
                            new BranchDto(
                                branchResponse.getName(),
                                Optional.ofNullable(branchResponse.getCommitDto())
                                    .map(CommitDto::getSha)
                                    .orElse(null)))
                    .toList())
        .orElse(Collections.emptyList());
  }

  private List<FinalResponse> processResponse(List<RepositoryResponse> repositoryResponses) {
    return repositoryResponses.stream()
        .filter(repositoryResponse -> Boolean.FALSE.equals(repositoryResponse.isFork()))
        .map(getRepositoryResponseFinalResponse())
        .toList();
  }

  private Function<RepositoryResponse, FinalResponse> getRepositoryResponseFinalResponse() {
    return repositoryResponse ->
        new FinalResponse(
            repositoryResponse.getRepositoryName(),
            repositoryResponse.getOwnerLogin().getLogin(),
            getBranches(
                repositoryResponse.getOwnerLogin().getLogin(),
                repositoryResponse.getRepositoryName()));
  }
}
