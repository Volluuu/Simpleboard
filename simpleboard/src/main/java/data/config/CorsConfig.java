package data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
	
	@Bean
    public CorsFilter corsFilter() {
		//request 요청에 대한 권한 필터링
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");//리액트 설정 IP만 허용
        config.addAllowedHeader("*");//모든 헤더에 응답
        config.addAllowedMethod("*");//모든 post,get 등 허용
        config.setAllowCredentials(true);//내 서버가 응답을 할 때 Json을 JS에서 처리할수 있도록
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
	}
	
}
