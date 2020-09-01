package com.stephen.springboot.config.auth;

import com.stephen.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.ArrayList;

@RequiredArgsConstructor
@EnableWebSecurity //Spring Security 설정들을 활성화시켜 줍니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable()// h2-console 화면 사용을 위해 해당 옵션들을 disabled 합니다.
                .and()
                    .authorizeRequests() //URL별 권한 관리를 설정하는 옵션의 시작점. 이게 선언되어야 antMathers 사용이 가능함.
                    //권한 관리 대상을 지정하는 옵션
                    //URL, HTTP 메서드별로 관리가 가능함.
                    // "/"등 지정된 URL들은 permitAll()옵션을 통해 전체 열람 권한을 주었다.
                    // /api/v1/** 주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 지정함.
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    // 설정된 값들 이외의 나머지 URL를 나타낸다.
                    // authenticated()를 통해 나머지 URL들은 모두 인증된 사용자들에게만 허용하게 한다.
                    // 인증된 사용자, 즉 로그인한 사용자들을 이야기한다.
                    .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/") //로그아웃 기능에 대한 여러 설정의 진입점. 로그아웃 성공시 /주소로 이동.
                .and()
                    .oauth2Login() // OAuth2 로그인 기능에 대한 여러 설정의 진입점.
                        .userInfoEndpoint() //OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당.
                            .userService(customOAuth2UserService);

    }
}



















