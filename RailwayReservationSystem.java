import java.util.*;

class Train {
    String name;
    String route;
    boolean[] seats;

    public Train(String name, String route) {
        this.name = name;
        this.route = route;
        this.seats = new boolean[100];
        Arrays.fill(this.seats, true); // Initialize all seats as available
    }

    public int availableSeats() {
        int count = 0;
        for (boolean seat : seats) {
            if (seat) {
                count++;
            }
        }
        return count;
    }
}

class User {
    String username;
    String password;
    String email;
    Train bookedTrain;
    int bookedSeatIndex;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bookedTrain = null;
        this.bookedSeatIndex = -1; // No seat booked initially
    }
}

public class RailwayReservationSystem {
    static List<Train> trains = new ArrayList<>();
    static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        initializeTrains();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Forgot Password\n4. Exit");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    register(sc);
                    break;
                case 2:
                    login(sc);
                    break;
                case 3:
                    forgotPassword(sc);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void initializeTrains() {
        trains.add(new Train("Vande Bharath 143", "Lingampally to Ernakulam"));
        trains.add(new Train("Vande Bharath 565", "Lingampally to Ernakulam"));
        trains.add(new Train("Shabari Express", "Lingampally to Ernakulam"));
        trains.add(new Train("Memu 2234", "Lingampally to Ernakulam"));
        trains.add(new Train("Chhattisgarh Express 1823", "Lingampally to Ernakulam"));
    }

    public static void register(Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
        } else {
            users.put(username, new User(username, password, email));
            System.out.println("Registration successful.");
        }
    }

    public static void login(Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (users.containsKey(username) && users.get(username).password.equals(password)) {
            System.out.println("Login successful.");
            User user = users.get(username);
            userMenu(sc, user);
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    public static void forgotPassword(Scanner sc) {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();

        if (users.containsKey(username) && users.get(username).email.equals(email)) {
            System.out.println("Your password is: " + users.get(username).password);
        } else {
            System.out.println("Invalid username or email.");
        }
    }

    public static void userMenu(Scanner sc, User user) {
        while (true) {
            System.out.println("\n1. Book Seat\n2. Cancel Booking\n3. Check Seat Availability\n4. Logout");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            if (choice == 1) {
                bookSeat(sc, user);
            } else if (choice == 2) {
                cancelBooking(user);
            } else if (choice == 3) {
                checkAvailability();
            } else if (choice == 4) {
                break;
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    public static void bookSeat(Scanner sc, User user) {
        if (user.bookedTrain != null) {
            System.out.println("You have already booked a seat in another train.");
            return;
        }

        System.out.println("Available trains:");
        List<Train> availableTrains = new ArrayList<>();
        for (Train train : trains) {
            if (train.availableSeats() > 0) {
                availableTrains.add(train);
            }
        }

        if (availableTrains.isEmpty()) {
            System.out.println("Seats not available.");
            return;
        }

        for (int i = 0; i < availableTrains.size(); i++) {
            Train train = availableTrains.get(i);
            System.out.println((i + 1) + ". " + train.name + " (" + train.route + ") - Available Seats: " + train.availableSeats());
        }

        System.out.print("Select train (1-" + availableTrains.size() + "): ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (choice >= 1 && choice <= availableTrains.size()) {
            Train selectedTrain = availableTrains.get(choice - 1);
            for (int i = 0; i < selectedTrain.seats.length; i++) {
                if (selectedTrain.seats[i]) {
                    selectedTrain.seats[i] = false;
                    user.bookedTrain = selectedTrain;
                    user.bookedSeatIndex = i;
                    System.out.println("Seat booked successfully in " + selectedTrain.name + ".");
                    return;
                }
            }
        } else {
            System.out.println("Invalid train selection.");
        }
    }

    public static void cancelBooking(User user) {
        if (user.bookedTrain == null) {
            System.out.println("You have no active bookings to cancel.");
            return;
        }

        user.bookedTrain.seats[user.bookedSeatIndex] = true; // Free the seat
        System.out.println("Your booking in " + user.bookedTrain.name + " has been cancelled.");
        user.bookedTrain = null;
        user.bookedSeatIndex = -1;
    }

    public static void checkAvailability() {
        System.out.println("Train Seat Availability:");
        for (Train train : trains) {
            System.out.println(train.name + " (" + train.route + ") - Available Seats: " + train.availableSeats());
        }
    }
}
