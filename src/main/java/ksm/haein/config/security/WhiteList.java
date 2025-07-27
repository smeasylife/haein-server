package ksm.haein.config.security;

import org.springframework.http.HttpMethod;

public record WhiteList(HttpMethod method, String url) {
}
