package ru.otus.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("address")
public class Address {
    @Id
    @EqualsAndHashCode.Include
    private final Long id;

    @Column("street")
    private final String street;

    @PersistenceCreator
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
