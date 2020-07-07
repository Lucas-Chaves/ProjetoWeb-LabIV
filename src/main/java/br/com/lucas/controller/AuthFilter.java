package br.com.lucas.controller;

import br.com.lucas.dao.UserDao;
import br.com.lucas.entity.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Objects;
import java.util.StringTokenizer;


public class AuthFilter implements Filter {

    private ServletContext context;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        this.context.log("Filtro acessado!");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Verifica se tem o header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            // Divide o conteúdo do header por espacos
            StringTokenizer st = new StringTokenizer(authHeader);
            // Se possui conteúdo
            if (st.hasMoreTokens()) {
                String basic = st.nextToken();
                // Verifica se possui o prefixo Basic
                if (basic.equalsIgnoreCase("Basic")) {
                    try {
                        // Extrai as credenciais (Base64)
                        String credentials =
                                new String(
                                        Base64.getDecoder()
                                                .decode(st.nextToken()));
                        this.context.log("Credentials: " + credentials);
                        // Separa as credenciais em usuario e senha
                        Integer p = credentials.indexOf(":");
                        if (p != -1) {
                            String _username =
                                    credentials.substring(0, p).trim();
                            String _password =
                                    credentials.substring(p + 1).trim();

                            UserDao userDao = new UserDao();
                            User _user = userDao.buscarPorEmail(_username);
                            this.context.log(_user.getUserEmail());
                            this.context.log(_user.getUserPass());

                            // Se nao bate com configuracao retorna erro
                            if (!Objects.equals(_user.getUserEmail(), _username) ||
                                    !Objects.equals(_user.getUserPass(), _password)) {
                                unauthorized(response, "Bad credentials");
                                return;
                            }
                            //admin role,
                            this.context.log("Request Type " + request.getMethod());

                            if (request.getMethod().equals("PUT")
                                    && !_user.getUserRole().toLowerCase().equals("admin")) {
                                unauthorized(response, "No permission to access this resource");
                                return;
                            }
                            if (request.getMethod().equals("DELETE")
                                    && !_user.getUserRole().toLowerCase().equals("admin")) {
                                unauthorized(response, "No permission to access this resource");
                                return;
                            }
                            // Prossegue com a requisicao
                            chain.doFilter(req, res);
                        } else {
                            unauthorized(response,
                                    "Invalid authentication token");
                        }
                    } catch (UnsupportedEncodingException e) {
                        throw new Error("Couldn't retrieve authentication", e);
                    }
                }
            }
        } else {
            unauthorized(response);
        }

    }

    @Override
    public void destroy() {
        // Aqui pode-se desalocar qualquer recurso

    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.context = config.getServletContext();
        this.context.log("Filtro inicializado!");
    }

    private void unauthorized(HttpServletResponse response, String message)
            throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + "Protect" + "\"");
        response.sendError(401, message);
    }

    private void unauthorized(HttpServletResponse response) throws IOException {
        unauthorized(response, "Unauthorized");
    }

}