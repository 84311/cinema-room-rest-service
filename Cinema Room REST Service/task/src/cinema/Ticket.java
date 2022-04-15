package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Ticket {
    private final String token;
    @JsonProperty("ticket")
    private final Cinema.Seat seat;

    public Ticket(Cinema.Seat seat) {
        this.seat = seat;
        this.token = UUID.randomUUID().toString();
        this.seat.setPurchased(true);
    }

    public Cinema.Seat getSeat() {
        return seat;
    }

    public String getToken() {
        return token;
    }
}