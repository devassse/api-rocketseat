package mz.co.keomasoftware.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mz.co.keomasoftware.todolist.user.IUserRepository;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        System.out.println("Verify Path " + servletPath);

        if(servletPath.startsWith("/tasks/")) {
            //Pegar dados de Utilizador
            var authorization = request.getHeader("Authorization");
            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
            var authStringfy = new String(authDecoded);

            String[] credentials = authStringfy.split(":");
            String username = credentials[0];
            String password = credentials[1];
            
            //Validar Autenticidade
            var loggedUser = this.userRepository.findByUsername(username);

            if(loggedUser == null) {
                response.sendError(401);
            }else {

                System.out.println("Logged User" + loggedUser);

                //Validar Senha
                var verifyPassord = BCrypt.verifyer().verify(password.toCharArray(), loggedUser.getPassword());

                if(verifyPassord.verified){
                    //Seguir viajem
                    request.setAttribute("userId", loggedUser.getId());
                    filterChain.doFilter(request, response);

                }else {
                    response.sendError(401);
                }
            }
            // filterChain.doFilter(request, response);
        }else{
            filterChain.doFilter(request, response);
        }

        
    }

    // @Override
    // public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    //         throws IOException, ServletException {
    //     System.out.println("Chegou no Filter");
    //     chain.doFilter(request, response);
    // }
    

}
