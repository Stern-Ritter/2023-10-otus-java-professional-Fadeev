package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.dto.ClientDto;
import ru.otus.mappers.Mapper;
import ru.otus.model.Client;
import ru.otus.services.ClientService;

import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/client/create")
    public String showCreateClientForm(Model model) {
        ClientDto client = new ClientDto();
        model.addAttribute("client", client);
        return "createClient";
    }

    @GetMapping("/client")
    public String clientsList(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clients";
    }

    @PostMapping("/client")
    public RedirectView createClient(ClientDto clientDto) {
        Client client = Mapper.fromClientDtoToClient(clientDto);
        clientService.save(client);
        return new RedirectView("/client", true);
    }
}
