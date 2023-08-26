package com.pl.gtihub.api.Api.github.gtihub;

import com.pl.gtihub.api.Api.github.gtihub.dto.FinalResponse;
import java.util.List;

public interface GitHubService {

  List<FinalResponse> getRepositories(String username, String acceptHeader);
}
