package data.config;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import data.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;


//웹 보안 어노테이션
//추가적인 설정을 위해  extends WebSecurityConfigurerAdapter 추가
@EnableWebSecurity //시큐리티 필터가 필터 체인에 등록하는 어노테이션
//@CrossOrigin //시큐리티 인증이 필요한 요청은 모두 거부, 인증이 필요하지 않는 요청만 허용
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
		private final Logger log = LoggerFactory.getLogger(getClass());
		private final TokenProvider tokenProvider;
		private final CorsFilter corsFilter;
		

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	    
//	    @Bean // 로그인 시 실행되는 메소드
//	    public AuthenticationProvider authenticationProvider(){return new LoginAuthenticationProvider();}
//
//	    @Bean // 로그인 성공 시 실행되는 메소드 
//	    public AuthenticationSuccessHandler successHandlerHandler() {
//	        return new LoginSuccessHandler();
//	    }
//
//	    @Bean // 로그인 실패 시 실행되는 메소드
//	    public AuthenticationFailureHandler failureHandlerHandler() {
//	        return new LoginFailureHandler();
//	    }

	    
	    @Bean
		public AccessDeniedHandler accessDeniedHandler() {
			log.warn("accessDeniedHandler");
			return (request, response, e) -> {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.setContentType("text/plain;charset=UTF-8");
				response.getWriter().write("ACCESS DENIED");
				response.getWriter().flush();
				response.getWriter().close(); 
			};
		}

		@Bean
		public AuthenticationEntryPoint authenticationEntryPoint() {
			return (request, response, e) -> {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("text/plain;charset=UTF-8");
				response.getWriter().write("UNAUTHORIZED");
				response.getWriter().flush();
				response.getWriter().close();
			};
		}

	   @Override
	    public void configure(WebSecurity web) throws Exception {
	        web.ignoring().antMatchers("/css/**", "/js/**");
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
//	    	http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class );//해당 필터는 일반적인 필터라 시큐리티 필터 이전 혹은 이후에 걸어줘야 정상 동작한다
	        http
	                .csrf().disable()// 세션을 사용하지 않고 JWT 토큰을 활용하여 진행, csrf토큰검사를 비활성화
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션, 쿠키를 사용하지 않고 진행하기 위한 셋팅
	                .and()
	                .formLogin().disable() 
	                .httpBasic().disable() //http Basic 사용하지 않겠다. ID, PW보호를 위해
	                .addFilter(corsFilter) //@CrossOrigin (인증X),인증(O)일때는 시큐리티 필터에 등록 필요
	                .authorizeRequests() // 인증절차에 대한 설정을 진행
	                .antMatchers("/*", "/user/*", "/board/**").permitAll() // 설정된 url은 인증되지 않더라도 누구든 접근 가능
	                .anyRequest().authenticated()// 위 페이지 외 인증이 되어야 접근가능(ROLE에 상관없이)
	                .and()
//	                .loginPage("/user/login")  // 접근이 차단된 페이지 클릭시 이동할 url
//	                .loginProcessingUrl("/user/login") // 로그인시 맵핑되는 url
//	                .usernameParameter("user_id")      // view form 태그 내에 로그인 할 id 에 맵핑되는 name ( form 의 name )
//	                .passwordParameter("user_pass")      // view form 태그 내에 로그인 할 password 에 맵핑되는 name ( form 의 name )
//	                .successHandler(successHandlerHandler()) // 로그인 성공시 실행되는 메소드
//	                .failureHandler(failureHandlerHandler()) // 로그인 실패시 실행되는 메소드
//	                .permitAll()
//	                .and()
//	                .logout() // 로그아웃 설정
//	                .logoutUrl("/user/logout") // 로그아웃시 맵핑되는 url
//	                .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
//	                .invalidateHttpSession(true)// 세션 clear
//	                .and()
	                .cors(); 
	      
	    }
		
}
