package ro.secur.auth.security;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ro.secur.auth.configuration.JwtConfiguration;
import ro.secur.auth.security.filter.AuthenticationFilter;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationFilterTest {

    private static final String TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZXMiOlt7ImF1dGhvcml0eSI6IlVTRVIifV0sImV4cCI6MTU4Mjc1NDQwMH0.N29TttLVw_zECEUOPRoKrTgsOR7zQRaVOARJ6ZYZ6vTYKFJmO-vseuiwfVnAAq8WS4B89jIgwR4ah2vcqVEVXw";
    public static final String SECRET_KEY = "madkagdfgkjfhjrkojiurgoijdsgjkldfgdislfjspfo[fasdkfodkgdosmgbakw[pfkb";

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtConfiguration jwtConfig;

    @Mock
    private MockHttpServletRequest request;

    @Mock
    private MockHttpServletResponse response;

    private MockMvc mockMvc;

    private AuthenticationFilter authFilter;

    Authentication expectedAuthentication;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void init() throws JSONException {

        createRequest(createJsonObject());

        expectedAuthentication = new UsernamePasswordAuthenticationToken("test", "pass", Collections.singletonList(new SimpleGrantedAuthority("USER")));

        mockJwt();
        authFilter = new AuthenticationFilter(authenticationManager, jwtConfig);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(authFilter)
                .build();
    }

    private void mockJwt() {
        when(jwtConfig.secretKey()).thenReturn(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()));
        when(jwtConfig.getTokenExpirationDays()).thenReturn(14);
        when(jwtConfig.getAuthorizationHeader()).thenReturn(HttpHeaders.AUTHORIZATION);
        when(jwtConfig.getTokenPrefix()).thenReturn("Bearer ");
    }

    private void createRequest(JSONObject jsonRequestBody) {
        request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        request.setContent(jsonRequestBody.toString().getBytes());
    }

    private JSONObject createJsonObject() throws JSONException {
        JSONObject jsonRequestBody = new JSONObject();

        jsonRequestBody.put("username", "test");
        jsonRequestBody.put("password", "pass");

        return jsonRequestBody;
    }

    private void mockAuthenticationManager() {
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(expectedAuthentication);
    }

    @Test
    public void attemptAuthenticationSuccessful() {

        mockAuthenticationManager();

        Authentication result = authFilter.attemptAuthentication(request, response);
        assertNotNull(result);
        assertEquals(expectedAuthentication, result);
    }

    @Test
    public void whenPerformLogin_returnTokenInHeader() throws Exception {

        mockAuthenticationManager();

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/api/v1/login")
                        .content(createJsonObject().toString().getBytes()))
                .andExpect(status().isOk())
                .andReturn();

        String token = result.getResponse().getHeader(jwtConfig.getAuthorizationHeader());

        assertNotNull(token);
        assertEquals(TOKEN, token);
    }
}
