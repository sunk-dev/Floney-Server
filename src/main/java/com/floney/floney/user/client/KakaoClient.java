package com.floney.floney.user.client;

import com.floney.floney.common.exception.OAuthResponseException;
import com.floney.floney.user.dto.response.KakaoUserResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Component
public class KakaoClient implements ClientProxy {

    private Long id;
    private String email;
    private String nickname;

    @Override
    public void init(String authToken) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode(StandardCharsets.UTF_8)
                .build().toUri();

        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer ".concat(authToken));
        HttpEntity<String> request = new HttpEntity<>(header);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoUserResponse> result = restTemplate.exchange(uri, HttpMethod.GET, request, KakaoUserResponse.class);

        this.id = result.getBody().getId();
        this.email = result.getBody().getKakaoAccount().getEmail();
        this.nickname = result.getBody().getKakaoAccount().getProfile().getNickname();

    }

}
