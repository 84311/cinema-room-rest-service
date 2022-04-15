package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cinema {
    private final int totalRows;
    private final int totalColumns;

    @JsonIgnore
    private final List<Seat> allSeats = new ArrayList<>();
    @JsonIgnore
    private final HashMap<String, Ticket> tickets = new HashMap<>();

    public Cinema(int totalRows, int totalColumns) {
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;
        initAllSeatsAsAvailable();
    }

    private void initAllSeatsAsAvailable() {
        for (int i = 0; i < totalRows; i++) {
            for (int j = 0; j < totalColumns; j++) {
                Seat seat = new Seat(i + 1, j + 1);
                seat.price = i < 4 ? 10 : 8;
                allSeats.add(seat);
            }
        }
    }

    @JsonProperty("available_seats")
    public List<Seat> getAvailableSeats() {
        return allSeats.stream()
                .filter(e -> !e.isPurchased())
                .collect(Collectors.toList());
    }

    public boolean isSeatPurchased(int row, int column) {
        return getSeat(row, column).purchased;
    }

    public Seat getSeat(int row, int column) {
        return allSeats.get((row - 1) * totalColumns + column - 1);
    }

    public void addTicket(Ticket ticket) {
        this.tickets.put(ticket.getToken(), ticket);
    }

    public Map<String, Ticket> getTickets() {
        return tickets;
    }

    static class Seat {
        private final int row;
        private final int column;
        @JsonIgnore
        boolean purchased;
        private int price;

        public Seat(int row, int column) {
            this.row = row;
            this.column = column;
            this.price = 0;
            purchased = false;
        }

        public int getRow() {
            return row;
        }

        public int getColumn() {
            return column;
        }

        public int getPrice() {
            return price;
        }

        public boolean isPurchased() {
            return purchased;
        }

        public void setPurchased(boolean purchased) {
            this.purchased = purchased;
        }
    }
}
