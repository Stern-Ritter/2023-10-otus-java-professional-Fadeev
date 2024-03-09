package ru.otus.mappers;

import ru.otus.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Mapper {
    public static Client fromClientDtoToClient(ClientDto clientDto) {
        return new Client(
                null,
                clientDto.getName(),
                new Address(null, clientDto.getAddress()),
                Arrays.stream(clientDto.getPhones().split("\\s*,\\s*"))
                        .map(phoneNumber -> new Phone(null, phoneNumber)).collect(Collectors.toSet()));
    }
}
