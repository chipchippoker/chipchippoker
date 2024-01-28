package com.chipchippoker.backend.common.util.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.common.exception.UnAuthorizedException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {

	@Value("${jwt.secretKey}")
	private String secretKey;

	@Value("${jwt.access.expiration}")
	private Long accessTokenExpirationPeriod;

	@Value("${jwt.refresh.expiration}")
	private Long refreshTokenExpirationPeriod;

	@Value("${jwt.access.header}")
	private String accessHeader;

	@Value("${jwt.refresh.header}")
	private String refreshHeader;

	/*
	JWT 생성
	@param userNum
	@return String
	 */
	public String createAccessToken(Long id, String nickname) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("id", id)
			.claim("nickname", nickname)
			.setSubject(accessHeader)
			.setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod))
			.signWith(SignatureAlgorithm.HS256, secretKey) //signature 부분
			.compact();
	}

	public String createRefreshToken(Long id, String nickname) {
		return Jwts.builder()
			.setSubject(refreshHeader)
			.claim("id", id)
			.claim("nickname", nickname)
			.setExpiration(new Date(new Date().getTime() + refreshTokenExpirationPeriod))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact(); //signature 부분
	}

	public Long getId(String accessToken, String refreshToken) {
		try {
			if (refreshToken == null) {
				return Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(accessToken)
					.getBody()
					.get("id", Long.class);
			}
			// 토큰 재발급의 경우 access,refresh 토큰 둘다 검사함
			Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(accessToken);

			return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(refreshToken)
				.getBody()
				.get("id", Long.class);
		} catch (ExpiredJwtException e) {
			if (refreshToken == null) {
				throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESS_TOKEN_EXPIRED);
			}
			try {
				return Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(refreshToken)
					.getBody()
					.get("id", Long.class);
			} catch (ExpiredJwtException ex) {
				throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_REFRESH_TOKEN_EXPIRED);
			} catch (Exception exception) {
				throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
			}
		} catch (Exception e) {
			throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
		}

	}

	public String getNickName(String accessToken, String refreshToken) {
		//1. JWT 추출
		if (accessToken == null || accessToken.isEmpty()) {
			if (isValidateToken(refreshToken)) {
				throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESS_TOKEN);
			} else {
				throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
			}
		} else {
			if (isValidateToken(accessToken)) {
				return Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(accessToken).getBody().get("nickname", String.class);
			} else {
				if (isValidateToken(refreshToken)) {
					throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESS_TOKEN);
				} else {
					throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
				}
			}
		}
	}

	public boolean isValidateToken(String token) {
		try {
			Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
