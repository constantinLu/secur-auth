package ro.secur.auth.security;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ro.secur.auth.common.Role;
import ro.secur.auth.configuration.JwtConfiguration;
import ro.secur.auth.security.filter.AuthenticationFilter;
import ro.secur.auth.util.Api;

import javax.ws.rs.core.MediaType;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationFilterTest {

    public static final String SECRET_KEY = "madkagdfgkjfhjrkojiurgoijdsgjkldfgdislfjspfo[fasdkfodkgdosmgbakw[pfkb";

    @Mock
    private AuthenticationManager authManagerMock;

    @Mock
    private JwtConfiguration jwtConfigMock;

    @Mock
    private MockHttpServletRequest requestMock;

    @Mock
    private MockHttpServletResponse responseMock;

    private MockMvc mockMvc;

    private AuthenticationFilter authFilter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() throws JSONException {

        mockJwtConfig();
        authFilter = new AuthenticationFilter(authManagerMock, jwtConfigMock);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(authFilter)
                .build();
    }

    private void mockJwtConfig() {
        when(jwtConfigMock.secretKey()).thenReturn(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()));
        when(jwtConfigMock.getTokenExpirationDays()).thenReturn(14);
        when(jwtConfigMock.getAuthorizationHeader()).thenReturn(HttpHeaders.AUTHORIZATION);
        when(jwtConfigMock.getTokenPrefix()).thenReturn("Bearer ");
    }

    private void mockRequest(JSONObject jsonRequestBody) {
        requestMock = new MockHttpServletRequest();
        requestMock.setContentType(MediaType.APPLICATION_JSON);
        requestMock.setCharacterEncoding("UTF-8");
        requestMock.setContent(jsonRequestBody.toString().getBytes());
    }

    private JSONObject createJsonObject(String username, String password) throws JSONException {
        JSONObject jsonRequestBody = new JSONObject();

        jsonRequestBody.put("username", username);
        jsonRequestBody.put("password", password);

        return jsonRequestBody;
    }

    private void mockAuthManagerReturnsAuth(Authentication expectedAuth) {

        when(authManagerMock.authenticate(any(Authentication.class))).thenReturn(expectedAuth);
    }

    private void mockAuthManagerReturnsException() {

        when(authManagerMock.authenticate(any(Authentication.class))).thenThrow(AuthenticationException.class);
    }

    private void mockAuthManagerReturnsBadCredentials() {

        when(authManagerMock.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);
    }

    @SneakyThrows
    @Test
    public void whenAttemptAuthentication_returnAuth() {

        Authentication expectedAuth = new UsernamePasswordAuthenticationToken("test", "pass",
                Collections.singletonList(new SimpleGrantedAuthority(Role.USER.toString())));

        mockAuthManagerReturnsAuth(expectedAuth);
        mockRequest(createJsonObject("test", "pass"));

        Authentication actualAuth = authFilter.attemptAuthentication(requestMock, responseMock);

        assertEquals(expectedAuth, actualAuth);
    }

//    TODO uncomment this after error handling task is finished
//    @SneakyThrows
//    @Test
//    public void whenAttemptAuthentication_returnException(){
//
//        mockAuthManagerReturnsException();
//        mockRequest(createJsonObject());
//
//        assertThrows(AuthenticationException.class, ()->authFilter.attemptAuthentication(requestMock, responseMock));
//    }

    @SneakyThrows
    @Test
    public void whenPerformLogin_returnStatusIsOk() {

        Authentication expectedAuth = new UsernamePasswordAuthenticationToken("test", "pass",
                Collections.singletonList(new SimpleGrantedAuthority(Role.USER.toString())));

        mockAuthManagerReturnsAuth(expectedAuth);

        int actualStatus = mockMvc
                .perform(MockMvcRequestBuilders.post(Api.LOGIN_URL)
                        .content(createJsonObject("test", "pass").toString().getBytes()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getStatus();

        assertEquals(HttpStatus.OK.value(), actualStatus);
    }

    @SneakyThrows
    @Test
    public void whenPerformLogin_returnStatusIsUnauthorized() {

        mockAuthManagerReturnsBadCredentials();

        int actualStatus = mockMvc
                .perform(MockMvcRequestBuilders.post(Api.LOGIN_URL)
                        .content(createJsonObject("test", "pass").toString().getBytes()))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse().getStatus();

//        TODO check why when credentials are wrong in postman 403 is returned
        assertEquals(HttpStatus.UNAUTHORIZED.value(), actualStatus);
    }

    @Test
    public void whenPerformLogin_returnTokenInHeader() throws Exception {

        Authentication expectedAuth = new UsernamePasswordAuthenticationToken("test", "pass",
                Collections.singletonList(new SimpleGrantedAuthority(Role.USER.toString())));

        mockAuthManagerReturnsAuth(expectedAuth);

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post(Api.LOGIN_URL)
                        .content(createJsonObject("test", "pass").toString().getBytes()))
                .andExpect(status().isOk())
                .andReturn();

        String actualToken = result.getResponse().getHeader(jwtConfigMock.getAuthorizationHeader());

        assertNotNull(actualToken);
    }
}
