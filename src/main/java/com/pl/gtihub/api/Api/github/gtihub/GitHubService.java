package com.pl.gtihub.api.Api.github.gtihub;

import com.pl.gtihub.api.Api.github.gtihub.dto.ResponseDto;
import java.util.List;

public interface GitHubService {

  List<ResponseDto> getRepositories(String username, String acceptHeader);
}
