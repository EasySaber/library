package com.example.sshomework.dto.view;

/**
 * @author Aleksey Romodin
 */
public interface View {
    interface Public {

    }
    interface All extends Public {

    }
    interface Private {

    }

    /**
     * Добавление нового автора.
     */
    interface AddAuthor {

    }

    /**
     * Список, написанных автором, книг. После добавления нового автора.
     */
    interface AuthorOfAllTheBook {

    }

    /**
     * Список, взятых пользователем, книг. Поиск по id пользователя. Расширенная версия.
     */
    interface PersonOfAllTheBook extends PersonOfAllTheBookSmall{

    }
    /**
     * Список, взятых пользователем, книг. Поиск по id пользователя.
     */
    interface PersonOfAllTheBookSmall  {

    }

    /**
     * Книга-автор-жанры
     */
    interface Book {

    }
    /**
     * Книга-автор-жанры, расширенная версия.
     */
    interface BookPost extends Book {

    }

    /**
     * Обновление жанров книги
     */
    interface UpdateBookGenres {

    }



}
