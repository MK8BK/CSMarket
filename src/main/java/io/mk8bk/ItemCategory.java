package io.mk8bk;

public record ItemCategory(String categoryName) {
    @Override
    public String toString(){
        return "["+categoryName+"]";
    }
}
