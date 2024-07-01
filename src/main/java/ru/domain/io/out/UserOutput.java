package ru.domain.io.out;

import java.util.List;

public interface UserOutput {
    void println(String message);

    void printList(List<String> list);
}
