package vn.javaweb.ComputerShop.component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.javaweb.ComputerShop.repository.UserRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter  extends OncePerRequestFilter {

    private final MessageComponent messageComponent;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;


    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if ( isBypassPath(request)){
            filterChain.doFilter(request, response);
        }else {
            String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                responseErrorMessage(response , HttpServletResponse.SC_UNAUTHORIZED,
                messageComponent.getLocalizedMessage("jwt.token.unauthorized", request.getLocale()));
                log.warn("Missing or invalid Authorization header");
                return;
            }

            String token = authorizationHeader.substring(7);
            boolean isValidToken = jwtUtils.validateToken(token);
            if ( !isValidToken ){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                responseErrorMessage(response , HttpServletResponse.SC_UNAUTHORIZED ,messageComponent.getLocalizedMessage("jwt.token.invalid", request.getLocale())  );
                log.warn("Token is invalid or expired");
                return;
            }

            // Setup Security ContextHolder if needed
            String username = jwtUtils.extractUsernameEnhanced(token);
            String permission = jwtUtils.extractPermissionEnhanced(token);

            UserDetails userDetails = (UserDetails) userRepository.findUserEntityByEmail(username).get();
            // we don't need password here, so set to null , because we already check token validity , we trust the token
            // only need field password in method handleLogin() , at this layer , we need field password to check with database
            // at this method  we check 2 times : firstly check manually password input an password in database (call directly data in DB and check )
            // , secondly check with spring security => so we need to set password at this method
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            // check Spring Security
            try{
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (Exception ex){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                responseErrorMessage(response , HttpServletResponse.SC_UNAUTHORIZED ,messageComponent.getLocalizedMessage("jwt.token.unauthorized", request.getLocale()) );
                log.warn("Authentication failed in authenticationManager layering");
                return;
            }
            log.info("Current authenticated user: {}, with permission: {}", username, permission);
            // If authentication is successful, set the authentication in the SecurityContextHolder
            // this allows us to use principal in controller

            // End JWT filter processing
            filterChain.doFilter(request, response);
        }
    }

    public void responseErrorMessage ( HttpServletResponse response, int statusError, String messageError ) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        String errorJson = String.format("{\"timestamp\":%d, \"status\":%d, \"error\":\"%s\", \"message\":\"%s\"}",
                System.currentTimeMillis(),
                statusError,
                ("Unauthorized"),
                messageError);
        response.getWriter().write(errorJson);
        response.getWriter().flush();
    }
    public boolean isBypassPath( HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        String [] bypassPaths = {
                "/api/v1/auth",
                "/api/v1/products",
                "/api/v1/payment/momo-endpoint",
                "/resources",
        };

        for ( String bypassPath : bypassPaths){
            if ( path.startsWith(bypassPath) ){
                log.info("Bypassing JWT for path: {}", path);
                return  true;
            }
        }


        // Bypass detail
//        List<Pair<String , String >> bypassDetails = Arrays.asList(
//                Pair.of("/api/v1/payment/momo-endpoint" , "GET"),
//                Pair.of("/api/auth/reset-password" , "POST")
//        );
//
//        for ( Pair<String , String > bypassDetail : bypassDetails ){
//            if ( path.startsWith(bypassDetail.getLeft()) && method.equalsIgnoreCase(bypassDetail.getRight() ) ){
//                return true;
//            }
//        }

        return false;

    };
}
