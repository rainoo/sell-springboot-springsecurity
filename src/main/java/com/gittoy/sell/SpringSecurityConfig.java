package com.gittoy.sell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SpringSecurityConfig
 * Create by GaoYu 2017/11/9 21:20
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserService myUserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("admin").password("123456").roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("gaoyu").password("123456").roles("ADMIN");
//        auth.inMemoryAuthentication().withUser("demo").password("123456").roles("USER");

        auth.userDetailsService(myUserService).passwordEncoder(new MyPasswordEncoder());
        auth.jdbcAuthentication().usersByUsernameQuery("").authoritiesByUsernameQuery("").passwordEncoder(new MyPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll() // 项目主路径允许访问。
                .anyRequest().authenticated() // 除此以外，所有请求都需要验证。
                .and()
                .logout().permitAll() // 注销可以不需要验证就可以访问。
                .and()
                .formLogin(); // 允许表单登录。
        http.csrf().disable(); // 关闭CSRF认证。
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }
}

/** -------------------------------------------------------------------------------------------

 Spring Security

 优点：
 1，提供了一套安全框架，而且这个框架是可以用的；
 2，提供了许多用户认证功能，实现相关接口即可，节约大量开发工作；
 3，基于Spring，易于集成到spring项目中，切封装了许多方法。

 缺点：
 1，配置文件多，角色被“编码”到配置文件和源文件中，RBAC不明显；
 2，对于系统中用户、角色、权限之间的关系，没有可操作的界面；
 3，大数据量情况下，几乎不可用。
 ------------------------------------------------------------------------------------------- */