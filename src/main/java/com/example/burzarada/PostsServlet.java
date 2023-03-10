package com.example.burzarada;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet("/posts")
public class PostsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        if (request.getParameterMap().size() == 0) {
            response.getWriter().println(returnAllPosts());

        } else if (request.getParameterMap().containsKey("title")) {
            String title = request.getParameter("title");

            if (title.length() == 0) {
                response.getWriter().println(new JSONObject().put("title", "naslov!"));
                return;
            }

            response.getWriter().println(returnPost("title", title));

        } else if (request.getParameterMap().containsKey("city")) {
            String city = request.getParameter("city");

            if (city.length() == 0) {
                response.getWriter().println(new JSONObject().put("city", "grad!"));
                return;
            }

            response.getWriter().println(returnPost("city", city));

        } else if (request.getParameterMap().containsKey("type")) {
            String type = request.getParameter("type");

            if (type.length() == 0) {
                response.getWriter().println(new JSONObject().put("type", "tip!"));
                return;
            }

            response.getWriter().println(returnPost("type", type));

        } else if (!request.getParameterMap().containsKey("delete") && request.getParameterMap().containsKey("id")) {
            String id = request.getParameter("id");

            if (id.length() == 0) {
                response.getWriter().println(new JSONObject().put("posts", "id!"));
                return;
            }

            response.getWriter().println(returnPost("id", id));

        } else if (request.getParameterMap().containsKey("update")) {
            updatePost(request);
            response.setStatus(200);

        } else if (request.getParameterMap().containsKey("delete")) {
            deletePost(request);
            response.setStatus(200);

        } else {
            response.setStatus(404);
            response.getWriter().println(new JSONObject().put("bad request!", false));
        }
    }

    private JSONObject returnAllPosts() {
        DbAccess db = new DbAccess();

        JSONArray resArr = new JSONArray();

        JSONObject respJson = new JSONObject();

        List<Post> posts = db.getAllPosts();

        if (posts == null) {
            respJson.put("posts", null);
            return respJson;
        }

        for (Post post : posts) {
            resArr.add(post.getPost());
        }

        respJson.put("posts", resArr);

        return respJson;
    }

    private JSONObject returnPost(String parameter, String value) {
        DbAccess db = new DbAccess();

        JSONArray resArr = new JSONArray();

        JSONObject respJson = new JSONObject();

        List<Post> posts = db.getPost(parameter, value);

        if (posts == null) {
            respJson.put("posts", null);
            return respJson;
        }

        for (Post post : posts) {
            resArr.add(post.getPost());
        }

        if (resArr.size() == 0)
            respJson.put("posts", null);
        else
            respJson.put("posts", resArr);

        return respJson;
    }

    public void updatePost(HttpServletRequest request) throws IOException {

        String what = request.getParameter("what");
        String value1 = request.getParameter("value1");
        String where = request.getParameter("where");
        String value2 = request.getParameter("value2");

        new DbAccess().updatePost(what, value1, where, value2);
    }

    public void deletePost(HttpServletRequest request) throws IOException {
        new DbAccess().deletePost(request.getParameter("id"));
    }

    // ########################################
    // $$$$$ CREATE POST
    // ########################################

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        JSONObject respJson = new JSONObject();

        // todo
    }
}
