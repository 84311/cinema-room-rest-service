package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CinemaExceptions {

    private CinemaExceptions() {
    }

    public static class CinemaException extends ResponseStatusException {
        public CinemaException(HttpStatus status) {
            super(status);
        }

        public CinemaException(HttpStatus status, String reason) {
            super(status, reason);
        }
    }

    public static class SeatOutOfBoundException extends CinemaException {
        public SeatOutOfBoundException() {
            super(HttpStatus.BAD_REQUEST, "The number of a row or a column is out of bounds!");
        }

        public SeatOutOfBoundException(HttpStatus status) {
            super(status);
        }

        public SeatOutOfBoundException(HttpStatus status, String reason) {
            super(status, reason);
        }
    }

    public static class PurchasedTicketException extends CinemaException {
        public PurchasedTicketException() {
            super(HttpStatus.BAD_REQUEST, "The ticket has been already purchased!");
        }

        public PurchasedTicketException(HttpStatus status) {
            super(status);
        }

        public PurchasedTicketException(HttpStatus status, String reason) {
            super(status, reason);
        }
    }

    public static class WrongTokenException extends CinemaException {
        public WrongTokenException() {
            super(HttpStatus.BAD_REQUEST, "Wrong token!");
        }

        public WrongTokenException(HttpStatus status) {
            super(status);
        }

        public WrongTokenException(HttpStatus status, String reason) {
            super(status, reason);
        }
    }

    public static class WrongPasswordException extends CinemaException {
        public WrongPasswordException() {
            super(HttpStatus.UNAUTHORIZED, "The password is wrong!");
        }

        public WrongPasswordException(HttpStatus status) {
            super(status);
        }

        public WrongPasswordException(HttpStatus status, String reason) {
            super(status, reason);
        }
    }
}