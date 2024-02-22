package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.services.client.ClientService;
import ru.otus.services.template.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"squid:S1948"})
public class ClientsServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_STREET = "street";
    private static final String PARAM_PHONE_NUMBER = "phone-number";
    private static final String PARAM_CLIENTS = "clients";

    private final TemplateProcessor templateProcessor;
    private final ClientService clientService;

    public ClientsServlet(TemplateProcessor templateProcessor, ClientService clientService) {
        this.templateProcessor = templateProcessor;
        this.clientService = clientService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> clients = clientService.findAll();
        paramsMap.put(PARAM_CLIENTS, clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(PARAM_NAME);
        String street = request.getParameter(PARAM_STREET);
        String phoneNumber = request.getParameter(PARAM_PHONE_NUMBER);

        Address address = new Address(street);
        Phone phone = new Phone(phoneNumber);
        Client client = new Client(name, address, List.of(phone));

        clientService.saveClient(client);
        response.sendRedirect("/clients");
    }
}
