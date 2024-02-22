package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.services.template.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;

public class CreateClientServlet extends HttpServlet {
    private static final String CREATE_CLIENT_PAGE_TEMPLATE = "create-client-form.html";

    private final TemplateProcessor templateProcessor;

    public CreateClientServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CREATE_CLIENT_PAGE_TEMPLATE, Collections.emptyMap()));
    }
}
