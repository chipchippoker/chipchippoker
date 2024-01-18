package com.chipchippoker.backend.common.util.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.chipchippoker.backend.common.dto.ErrorBase;
import com.chipchippoker.backend.common.exception.InvalidException;
import com.chipchippoker.backend.common.exception.UnAuthorizedException;
import com.chipchippoker.backend.common.util.jwt.dto.TokenResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
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
	public String createAccessToken(Long id,String nickname) {
		Date now = new Date();
		return Jwts.builder()
			.setHeaderParam("type", "jwt")
			.claim("id", id)
			.claim("nickname",nickname)
			.setSubject(accessHeader)
			.setExpiration(new Date(now.getTime() + accessTokenExpirationPeriod))
			.signWith(SignatureAlgorithm.HS256, secretKey) //signature 부분
			.compact();
	}

	public String createRefreshToken(Long id,String nickname) {
		return Jwts.builder()
			.setSubject(refreshHeader)
			.claim("id", id)
			.claim("nickname",nickname)
			.setExpiration(new Date(new Date().getTime() + refreshTokenExpirationPeriod))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact(); //signature 부분
	}

	/*
	JWT에서 userIdx 추출
	@return int
	@throws BaseException
	 */
	public Long getId(String accessToken, String refreshToken) {
		//1. JWT 추출
		if (accessToken == null || accessToken.isEmpty()) {
			if (isValidateToken(refreshToken)) {
				throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESSTOKEN);
			} else {
				throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
			}
		} else {
			if (isValidateToken(accessToken)) {
				return Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(accessToken).getBody().get("id", Long.class);
			} else {
				if (isValidateToken(refreshToken)) {
					throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESSTOKEN);
				} else {
					throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
				}
			}
		}
	}

	public String getNickName(String accessToken, String refreshToken) {
		//1. JWT 추출
		if (accessToken == null || accessToken.isEmpty()) {
			if (isValidateToken(refreshToken)) {
				throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESSTOKEN);
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
					throw new UnAuthorizedException(ErrorBase.E401_UNAUTHORIZED_ACCESSTOKEN);
				} else {
					throw new InvalidException(ErrorBase.E400_INVALID_TOKEN);
				}
			}
		}
	}
	public TokenResponse reissuanceTokens(String refreshToken) {
		Long id = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(refreshToken).getBody().get("id", Long.class);
		String nickname = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(refreshToken).getBody().get("nickname", String.class);
		String newAccessToken = createAccessToken(id,nickname);
		String newRefreshToken = createRefreshToken(id,nickname);
		return new TokenResponse(newAccessToken, newRefreshToken);
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
