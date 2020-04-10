//package com.yo.yoshare.security.config;
//
//import com.yo.yoshare.security.bo.MemberDetails;
//import com.yo.yoshare.security.service.MemberDetailsService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
//import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
//import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//
//import java.util.Arrays;
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
//
//
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private DataSource dataSource;
//
//
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
////        clients.inMemory()
////                //client的id和密码
////                .withClient("voluntaryClient")
////                .secret(passwordEncoder.encode("199weszxd"))
////
////                //给client一个id,这个在client的配置里要用的
////                .resourceIds("resource1")
////
////                //允许的申请token的方式,测试用例在test项目里都有.
////                //authorization_code授权码模式,这个是标准模式
////                //implicit简单模式,这个主要是给无后台的纯前端项目用的
////                //password密码模式,直接拿用户的账号密码授权,不安全
////                //client_credentials客户端模式,用clientid和密码授权,和用户无关的授权方式
////                //refresh_token使用有效的refresh_token去重新生成一个token,之前的会失效
////                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
////
////                //授权的范围,每个resource会设置自己的范围.
////                .scopes("all", "voluntary")
////
////                //这个是设置要不要弹出确认授权页面的.
////                .autoApprove(false)
////
////                //这个相当于是client的域名,重定向给code的时候会跳转这个域名
////                .redirectUris("http://www.baidu.com");
////
////                /*.and()
////
////                .withClient("client2")
////                .secret(passwordEncoder.encode("123123"))
////                .resourceIds("resource1")
////                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
////                .scopes("all")
////                .autoApprove(false)
////                .redirectUris("http://www.qq.com");*/
//        clients.withClientDetails(clientDetailsService);
//    }
//
//
//    @Resource(name = "aa")
//    private ClientDetailsService clientDetailsService;
//
//    @Autowired
//    private TokenStore tokenStore;
//
//    @Autowired
//    private JwtAccessTokenConverter tokenConverter;
//
//    //配置token管理服务
//    @Bean
//    public AuthorizationServerTokenServices tokenServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setClientDetailsService(clientDetailsService);
//        defaultTokenServices.setSupportRefreshToken(true);
//
//        //配置token的存储方法
//        defaultTokenServices.setTokenStore(tokenStore);
//        defaultTokenServices.setAccessTokenValiditySeconds(18000);
//        defaultTokenServices.setRefreshTokenValiditySeconds(9999999);
//
//        //配置token增加,把一般token转换为jwt token
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenConverter));
//        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
//        return defaultTokenServices;
//    }
//
//    //密码模式才需要配置,认证管理器
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//
//
//    //把上面的各个组件组合在一起
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager)//认证管理器
//                .authorizationCodeServices(new InMemoryAuthorizationCodeServices())//授权码管理
//                .tokenServices(tokenServices())//token管理
//                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
//    }
//
//    //配置哪些接口可以被访问
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.tokenKeyAccess("permitAll()")///oauth/token_key公开
//                .checkTokenAccess("permitAll()");///oauth/check_token公开
////                .allowFormAuthenticationForClients();//允许表单认证
////        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
//    }
//
//}
