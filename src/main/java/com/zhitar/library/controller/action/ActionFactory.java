package com.zhitar.library.controller.action;

import com.zhitar.library.controller.action.impl.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhitar.library.util.PropertiesUtil.*;

public class ActionFactory {

    private static final Logger LOG = Logger.getLogger(ActionFactory.class);

    private static Map<String, Action> requestActionMap = new HashMap<>();
    private static List<String> pathVariablesList = new ArrayList<>();

    static {
        requestActionMap.put(getValue("app.greeting.action"), new GreetingAction());
        requestActionMap.put(getValue("app.showbooks.action"), new ShowBooksAction());
        requestActionMap.put(getValue("app.login.action"), new LoginAction());
        requestActionMap.put(getValue("app.take.action"), new TakeBookAction());
        requestActionMap.put(getValue("app.filter.action"), new FilterAction());
        requestActionMap.put(getValue("app.register.action"), new RegisterAction());
        requestActionMap.put(getValue("app.logout.action"), new LogoutAction());
        requestActionMap.put(getValue("app.delete.action"), new DeleteAction());
        requestActionMap.put(getValue("app.edit.action"), new BookEditAction());
        requestActionMap.put(getValue("app.save.action"), new BookSaveAction());
        requestActionMap.put(getValue("app.readers.action"), new ReadersAction());
        requestActionMap.put(getValue("app.notfound.action"), new NotFoundAction());
        requestActionMap.put(getValue("app.give.action"), new GiveBookAction());
        requestActionMap.put(getValue("app.cancel.order.action"), new BookCancelOrderAction());

        pathVariablesList.add(getValue("app.take.action"));
        pathVariablesList.add(getValue("app.delete.action"));
    }

    public static Action getAction(HttpServletRequest request) {
        String path = request.getServletPath();
        LOG.info("getAction for " + path);
        Action action = findAction(path);
        if (action == null) {
            String actionPath = resolvePathVariable(path);
            LOG.debug("resolved path variable: " + actionPath);
            if (actionPath == null) {
                LOG.debug("get NotFoundAction");
                actionPath = "";
            }
            action = findAction(actionPath);
        }
        return action;
    }

    private static Action findAction(String path) {
        return requestActionMap.get(path);
    }

    private static String resolvePathVariable(String path) {
        for (String str : pathVariablesList) {
            String regex = str.replaceAll("[{][\\w]+[}]", "\\\\d+");
            if (path.matches(regex)) {
                return str;
            }
        }
        return null;
    }

}
