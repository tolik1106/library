package com.zhitar.library.util;

import com.zhitar.library.domain.Book;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class BookActionUtil {
    private BookActionUtil() {}

    public static void checkIsDebtor(HttpServletRequest request, Collection<Book> books) {
        boolean debtor = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date limitDate = calendar.getTime();
        for (Book userBook : books) {
            Date ownedDate = userBook.getOwnedDate();
            if (ownedDate.before(limitDate)) {
                debtor = true;
                break;
            }
        }
        request.setAttribute("debtor", debtor);
    }
}
