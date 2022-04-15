package cinema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class SeatController {
    private final Cinema cinema = new Cinema(9, 9);
    ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("deprecation") // SNAKE_CASE is deprecated
    public SeatController() {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @GetMapping("/seats")
    public String getSeats() {
        try {
            return mapper.writeValueAsString(cinema);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/purchase")
    public Ticket purchaseTicket(@RequestBody Cinema.Seat seat) {
        int row = seat.getRow();
        int column = seat.getColumn();
        if (row < 0 || row > 9 || column < 0 || column > 9) {
            throw new CinemaExceptions.SeatOutOfBoundException();
        } else if (cinema.isSeatPurchased(row, column)) {
            throw new CinemaExceptions.PurchasedTicketException();
        }

        Ticket ticket = new Ticket(cinema.getSeat(row, column));
        cinema.addTicket(ticket);

        return ticket;
    }

    @PostMapping("/return")
    public Map<String, Cinema.Seat> returnTicket(@RequestBody Map<String, String> tokenJSON) {
        String token = tokenJSON.get("token");
        Map<String, Ticket> allTickets = cinema.getTickets();

        if (!allTickets.containsKey(token)) {
            throw new CinemaExceptions.WrongTokenException();
        }

        allTickets.get(token).getSeat().setPurchased(false);
        Ticket removedTicket = allTickets.remove(token);

        return Map.of("returned_ticket", removedTicket.getSeat());
    }

    @PostMapping("/stats")
    public Map<String, Integer> getStats(@RequestParam(required = false) String password) {
        if (password == null || !password.equals("super_secret")) {
            throw new CinemaExceptions.WrongPasswordException();
        }

        Map<String, Ticket> tickets = cinema.getTickets();
        List<Cinema.Seat> availableSeats = cinema.getAvailableSeats();

        int currentIncome = tickets.values().stream()
                .mapToInt(e -> e.getSeat().getPrice())
                .sum();

        int numberOfAvailableSeats = availableSeats.size();
        int numberOfPurchasedTickets = tickets.size();

        return Map.of(
                "current_income", currentIncome,
                "number_of_available_seats", numberOfAvailableSeats,
                "number_of_purchased_tickets", numberOfPurchasedTickets);
    }

    @ExceptionHandler(CinemaExceptions.CinemaException.class)
    public ResponseEntity<Map<String, String>> handleCinemaException(CinemaExceptions.CinemaException ex) {
        return ResponseEntity
                .status(ex.getRawStatusCode())
                .body(Map.of("error", Objects.requireNonNull(ex.getReason())));
    }
}