package doczilla.filesharing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class UserDao {
    /**
     * @param login логин пользователя
     * @param pass  пароль пользователя
     * @return пользователя по логину и паролю
     */
    public static User loadUserByLoginAndPass(String login, String pass) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(AppProperties.get("users_path")));
        String searchUserInfo = reader.lines()
                .filter(userInfo -> userInfo.split(" ")[0].equals(login) && userInfo.split(" ")[1].equals(pass))
                .findAny()
                .orElse(null);
        return searchUserInfo == null ? null : new User(searchUserInfo.split(" ")[0], searchUserInfo.split(" ")[1]);
    }
}
