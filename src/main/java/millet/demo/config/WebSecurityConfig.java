package millet.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
// import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
// import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.oauth2.jwt.JwtDecoder;
// import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import jakarta.servlet.Filter;
// import com.bezkoder.spring.security.jwt.AuthEntryPointJwt;
// import millet.demo.config.AuthTokenFilter;
// import com.bezkoder.spring.security.services.UserDetailsServiceImpl;
import millet.demo.service.CustomUserDetailsService;
// import millet.demo.filter.JwtAuthenticationFilter;
@Configuration
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
  @Autowired
  CustomUserDetailsService userDetailsService;
  
  
  @Autowired
  private AuthenticationEntryPoint unauthorizedHandler;

  @Bean
  public Filter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }
    @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
  // @Bean
  // public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
  //   return authConfig.getAuthenticationManager();
  // }
  // @Override
  // public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
  //   authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  // }
  // @Bean
  // @Override
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
 
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> 
          auth.requestMatchers("/**", "/*").permitAll()
              .requestMatchers("/**", "/**").permitAll()
              .anyRequest().authenticated()
        );
        
    
    http.authenticationProvider(authenticationProvider());

    http.addFilterAt(this.authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }
  // @Bean
  // public AuthTokenFilter authenticationJwtTokenFilter() {
  //   return new AuthTokenFilter();
  // }

}
