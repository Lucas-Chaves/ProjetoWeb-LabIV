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
        // Recupera o parâmetro id (de user?id=<valor>)
        Long id = Long.valueOf(req.getParameter("id"));
        // Busca user com o id
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Recuperamos o corpo da requisição e transformamos o JSON em objeto
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(req.getReader(), User.class);
        // Salvamos no Banco de Dados
        UserDao userDao = new UserDao();
        userDao.salvar(user);
        // Retornamos o registro gerado
        String userJson = mapper.writeValueAsString(user);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        // O código 201 requer que retornemos um header de Location
        resp.setStatus(201);
        String location = req.getServerName() + ":" + req.getServerPort()
                + req.getContextPath() + "/user?id=" + user.getIdUser();
        resp.setHeader("Location", location);
        PrintWriter out = resp.getWriter();
        out.print(userJson);
        out.flush();
    }


}


    
