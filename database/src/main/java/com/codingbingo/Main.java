package com.codingbingo;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

public class Main {

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, "com.codingbingo.fastreader.dao");

        Entity book = schema.addEntity("Book");

        book.addIdProperty().primaryKey().autoincrement();
        book.addStringProperty("bookName").notNull();
        book.addStringProperty("bookImagePath");
        book.addStringProperty("description");
        book.addStringProperty("tags");
        book.addStringProperty("writer");
        book.addStringProperty("charSet");
        book.addStringProperty("bookPath");
        book.addIntProperty("processStatus").notNull();

        Entity chapter = schema.addEntity("Chapter");

        chapter.addIdProperty().primaryKey().autoincrement();
        chapter.addStringProperty("title");
        chapter.addLongProperty("position").notNull();
        chapter.addIntProperty("pageCount");
        chapter.addBooleanProperty("isRead");
        Property bookIdProperty = chapter.addLongProperty("bookId").notNull().getProperty();
        chapter.addToOne(book, bookIdProperty);

        new DaoGenerator().generateAll(schema, "~/Git/FastReader/app/src/main/java");
    }

}
