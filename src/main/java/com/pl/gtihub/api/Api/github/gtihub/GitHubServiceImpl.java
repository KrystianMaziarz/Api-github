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
public class GitHubServiceImpl implements GitHubService {

  private final RestTemplate restTemplate;

  private final HeaderValidator headerValidator;

  public List<ResponseDto> getRepositories(String userName, String acceptHeader) {

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
              new ParameterizedTypeReference<List<RepositoryGithubResponseDto>>() {});

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
            uri,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<BranchGithubResponseDto>>() {});

    return Optional.ofNullable(response.getBody())
        .map(getBranchGithubResponseDtoList())
        .orElse(Collections.emptyList());
  }

  private List<ResponseDto> processResponse(
      List<RepositoryGithubResponseDto> repositoryGithubResponseDtoList) {
    return repositoryGithubResponseDtoList.stream()
        .filter(repositoryResponseDto -> Boolean.FALSE.equals(repositoryResponseDto.isFork()))
        .map(getResponseDto())
        .toList();
  }

  private Function<RepositoryGithubResponseDto, ResponseDto> getResponseDto() {
    return repositoryResponseDto ->
        new ResponseDto(
            repositoryResponseDto.getRepositoryName(),
            repositoryResponseDto.getOwnerLogin().getLogin(),
            getBranches(
                repositoryResponseDto.getOwnerLogin().getLogin(),
                repositoryResponseDto.getRepositoryName()));
  }

  private static Function<List<BranchGithubResponseDto>, List<BranchDto>>
      getBranchGithubResponseDtoList() {
    return responseBody ->
        responseBody.stream()
            .map(
                branchGithubResponseDto ->
                    new BranchDto(
                        branchGithubResponseDto.getName(),
                        Optional.ofNullable(branchGithubResponseDto.getCommitDto())
                            .map(CommitGithubResponseDto::getSha)
                            .orElse(null)))
            .toList();
  }
}
