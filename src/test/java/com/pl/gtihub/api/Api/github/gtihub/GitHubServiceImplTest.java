package com.pl.gtihub.api.Api.github.gtihub;

import static com.pl.gtihub.api.Api.github.gtihub.GithubDataFactory.VALID_HEADER;
import static com.pl.gtihub.api.Api.github.gtihub.GithubDataFactory.getFinalResponseDto;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.pl.gtihub.api.Api.github.gtihub.exception.ServerNotRespondingException;
import com.pl.gtihub.api.Api.github.gtihub.exception.UserNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class GitHubServiceImplTest {

  private MockRestServiceServer mockServer;

  private final RestTemplate restTemplate = new RestTemplate();

  private final HeaderValidator headerValidator = new HeaderValidator();

  private final GitHubService gitHubService = new GitHubServiceImpl(restTemplate, headerValidator);

  @BeforeEach
  void setup() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  @SneakyThrows
  void shouldGetRepositoriesWhenGithubUsernameExists() {
    // given
    var userName = "KrystianMaziarz";
    var firstRepoName = "Horus";
    var secondRepoName = "microservice_parent";
    var jsonResponseExample = loadFile("src/test/resources/github/repositoryResponseExample.json");
    var jsonFirstRepoResponseExample =
        loadFile("src/test/resources/github/firstRepoResponseExample.json");
    var jsonSecondRepoResponseExample =
        loadFile("src/test/resources/github/secondRepoResponseExample.json");
    var expectedSearchGithubUrl = String.format("https://api.github.com/users/%s/repos", userName);
    var expected = getFinalResponseDto();

    var expectedSearchFirstRepo =
        String.format("https://api.github.com/repos/%s/%s/branches", userName, firstRepoName);
    var expectedSearchSecondRepo =
        String.format("https://api.github.com/repos/%s/%s/branches", userName, secondRepoName);

    mockServer
        .expect(requestTo(expectedSearchGithubUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(jsonResponseExample, MediaType.APPLICATION_JSON));

    mockServer
        .expect(requestTo(expectedSearchFirstRepo))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(jsonFirstRepoResponseExample, MediaType.APPLICATION_JSON));

    mockServer
        .expect(requestTo(expectedSearchSecondRepo))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withSuccess(jsonSecondRepoResponseExample, MediaType.APPLICATION_JSON));

    // when
    var result = gitHubService.getRepositories(userName, VALID_HEADER);

    // then
    Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldThrowUserNotFoundExceptionWhenUserNotFound() {
    // given
    var wrongUsername = "nameThatNotExists";
    var expectedSearchGithubUrl =
        String.format("https://api.github.com/users/%s/repos", wrongUsername);

    mockServer
        .expect(requestTo(expectedSearchGithubUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.NOT_FOUND));

    // when //then
    assertThrows(
        UserNotFoundException.class,
        () -> gitHubService.getRepositories(wrongUsername, VALID_HEADER));
  }

  @Test
  void shouldThrowServerNotResponseExceptionWhenServerIsDown() {
    // given
    var wrongUsername = "nameThatNotExists";
    var expectedSearchGithubUrl =
        String.format("https://api.github.com/users/%s/repos", wrongUsername);

    mockServer
        .expect(requestTo(expectedSearchGithubUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

    // when //then
    assertThrows(
        ServerNotRespondingException.class,
        () -> gitHubService.getRepositories(wrongUsername, VALID_HEADER));
  }

  @Test
  void shouldThrowServerNotResponseExceptionWhenHttpStatusIs5xx() {
    // given
    var wrongUsername = "nameThatNotExists";
    var expectedSearchGithubUrl =
        String.format("https://api.github.com/users/%s/repos", wrongUsername);

    mockServer
        .expect(requestTo(expectedSearchGithubUrl))
        .andExpect(method(HttpMethod.GET))
        .andRespond(withStatus(HttpStatus.BAD_GATEWAY));

    // when //then
    assertThrows(
        ServerNotRespondingException.class,
        () -> gitHubService.getRepositories(wrongUsername, VALID_HEADER));
  }

  private String loadFile(String path) throws Exception {
    return Files.readString(Path.of(path));
  }
}
