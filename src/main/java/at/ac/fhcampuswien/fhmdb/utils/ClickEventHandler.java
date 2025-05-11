package at.ac.fhcampuswien.fhmdb.utils;


@FunctionalInterface
public interface ClickEventHandler<T> {
    void onClick(T t);
};
