package com.pl.gtihub.api.Api.github.gtihub.configuration;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;

class EnsureApplicationJsonNegotiationStrategy extends HeaderContentNegotiationStrategy {
  @Override
  public List<MediaType> resolveMediaTypes(NativeWebRequest request)
      throws HttpMediaTypeNotAcceptableException {
    List<MediaType> mediaTypes = new ArrayList<>(super.resolveMediaTypes(request));
    if (notIncludesApplicationJson(mediaTypes)) {
      mediaTypes.add(MediaType.APPLICATION_JSON);
    }
    return mediaTypes;
  }

  private boolean notIncludesApplicationJson(List<MediaType> mediaTypes) {
    return mediaTypes.stream()
        .noneMatch(mediaType -> mediaType.includes(MediaType.APPLICATION_JSON));
  }
}
