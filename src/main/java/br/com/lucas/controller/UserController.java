package br.com.lucas.controller;

import br.com.lucas.dao.UserDao;
import br.com.lucas.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Recupera o parâmetro id (de trabalho?id=<valor>)
        Long id = Long.valueOf(req.getParameter("id"));
        // Busca trabalho com o id
        UserDao userDao = new UserDao();
        User user = userDao.buscar(id);
        // Usamos o Jackson para transformar o objeto em um JSON(String)
        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(user);
        // Formatamos a resposta
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(200);
        PrintWriter out = resp.getWriter();
        out.print(userJson);
        out.flush();
    }
}


    
