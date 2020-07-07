package br.com.lucas.controller;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    private ServletContext context;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        this.context.log("CORSFilter HTTP Request: " + request.getMethod());

        // Autoriza
        HttpServletResponse response = (HttpServletResponse)res;
        response.addHeader("Access-Control-Allow-Origin"
                , "*");
        response.addHeader("Access-Control-Allow-Methods"
                ,"GET, OPTIONS, HEAD, PUT, POST, DELETE");
        response.addHeader("Access-Control-Allow-Headers"
                ,"*");

        // Para requisicoes com metodo OPTIONS
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }

        // Passa adiante demais requisicoes
        chain.doFilter(req, res);
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

}