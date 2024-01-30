package code.grind.giftedschoolonline.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;


@Configuration
public class SecurityConfiguration {

    private final RSAPublicKey publicKey;

    private final RSAPrivateKey privateKey;



    @Value("${api.endpoint.base-url}")
    private String baseUrl;

    private final CustomBasicAuthEntryPoint customBasicAuthEntryPoint;

    private final CustomBearerTokenAuthEntryPoint customBearerTokenAuthEntryPoint;

    private final CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler;

    public SecurityConfiguration(CustomBasicAuthEntryPoint customBasicAuthEntryPoint, CustomBearerTokenAuthEntryPoint customBearerTokenAuthEntryPoint, CustomBearerTokenAccessDeniedHandler customBearerTokenAccessDeniedHandler) throws NoSuchAlgorithmException {
        this.customBasicAuthEntryPoint = customBasicAuthEntryPoint;
        this.customBearerTokenAuthEntryPoint = customBearerTokenAuthEntryPoint;
        this.customBearerTokenAccessDeniedHandler = customBearerTokenAccessDeniedHandler;

        //Generate a public/private key pair in java
        KeyPairGenerator keyPairGenerator= KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);//The generated key will have a size of 2048 bits.
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (RSAPublicKey) keyPair.getPublic();
        this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
            return http
                    .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                            .requestMatchers(HttpMethod.GET, this.baseUrl+ "/artifacts/**").permitAll()
                            .requestMatchers(HttpMethod.GET, this.baseUrl+ "/users/**").hasAuthority("ROLE_admin")//Protect this role
                            .requestMatchers(HttpMethod.POST, this.baseUrl+ "/users").hasAuthority("ROLE_admin")//Protect this role
                            .requestMatchers(HttpMethod.PUT, this.baseUrl+ "/users/**").hasAuthority("ROLE_admin")//Protect this role
                            .requestMatchers(HttpMethod.DELETE, this.baseUrl+ "/users/**").hasAuthority("ROLE_admin")//Protect this role
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                            //Disallow everything else
                            .anyRequest().authenticated() //Put this at the last
                    )
                    .headers(headers -> headers.frameOptions().disable())//This is for H2 browser Access
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults())//add cors to your spring security application.
                    .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.customBasicAuthEntryPoint))
                    .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt().and()
                            .authenticationEntryPoint(this.customBearerTokenAuthEntryPoint)
                            .accessDeniedHandler(this.customBearerTokenAccessDeniedHandler))
                    .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        JWKSource<SecurityContext> jwkset = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkset);
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }
}
