package ru.otus.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "phone")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Phone {
    @Id
    private final Long id;

    @EqualsAndHashCode.Include
    @Column("number")
    private final String number;

    @PersistenceCreator
    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
