package com.hendisantika.usermanagement.exception;

/**
 * Created by IntelliJ IDEA.
 * Project : user-management
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 22/03/20
 * Time: 10.06
 */
public class UsernameOrIdNotFound extends Exception {

    private static final long serialVersionUID = 1668398822129870029L;

    public UsernameOrIdNotFound() {
        super("Usuario o Id no encontrado");
    }

    public UsernameOrIdNotFound(String message) {
        super(message);
    }
}