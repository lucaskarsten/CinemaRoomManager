package cinema

fun main() {
    calcTickets()
}

fun calcTickets() {
    println("Enter the number of rows:")
    print("> ")
    val rows = readln().toInt()
    println("Enter the number of seats in each row:")
    print("> ")
    val seats = readln().toInt()

    val cinema = Cinema(_name = "Cinema", _rows = rows, _seats = seats)

    while (true) {
        val option = menu()
        when (option) {
            1 -> cinema.displayGrid()
            2 -> buyTicket(cinema, rows, seats)
            3 -> statistics(cinema)
            0 -> return
            else -> println("Wrong input!")
        }
    }
}

fun menu(): Int {
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")
    print(">")
    return readln().toInt()
}

fun statistics(cinema: Cinema) {
    val totalTickets = cinema.rows * cinema.seats
    val purchasedTickets = cinema.bookedSeats.size
    val percentage = if (totalTickets > 0) (purchasedTickets.toDouble() / totalTickets) * 100 else 0.0
    val formatPercentage = "%.2f".format(percentage)
    val currentIncome = cinema.currentIncome
    val totalIncome = cinema.totalIncome

    println("Number of purchased tickets: $purchasedTickets")
    println("Percentage: $formatPercentage%")
    println("Current income: $$currentIncome")
    println("Total income: $$totalIncome")
}

fun buyTicket(cinema: Cinema, rows: Int, seats: Int) {
    println("Enter a row number:")
    print("> ")
    val row = readln().toInt()
    println("Enter a seat number in that row:")
    print("> ")
    val seat = readln().toInt()

    if (row !in 1..rows || seat !in 1..seats) {
        println("Wrong input!")
        return
    }

    if (cinema.isSeatBooked(row, seat)) {
        println("That ticket has already been purchased!")
        buyTicket(cinema, rows, seats)
    }

    val totalSeats = rows * seats
    val ticketPrice: Int

   if (totalSeats <= 60) {
        ticketPrice = 10
    } else {
        val firstHalfRows = rows / 2
        ticketPrice = if (row <= firstHalfRows) 10 else 8
    }

    cinema.bookSeat(row, seat, ticketPrice)

    println("Ticket price: $$ticketPrice")
}

class Cinema(_name: String, _rows: Int, _seats: Int) {
    var name = _name
    var rows = _rows
    var seats = _seats

    var bookedSeats = mutableListOf<Pair<Int, Int>>()

    var currentIncome = 0
    var totalIncome = calculateTotalIncome()

    fun displayGrid() {
        println("$name:")

        print("  ")
        for (column in 1..seats) {
            print(" $column")
        }
        println()

        for (row in 1..rows) {
            print("$row ")
            for (column in 1..seats) {
                if (bookedSeats.contains(Pair(row, column))) {
                    print("B ")
                } else {
                    print("S ")
                }
            }
            println()
        }
    }

    fun isSeatBooked(row: Int, seat: Int): Boolean {
        return bookedSeats.contains(Pair(row, seat))
    }

    fun bookSeat(row: Int, seat: Int, ticketPrice: Int) {
        bookedSeats.add(Pair(row, seat))
        currentIncome += ticketPrice
    }

    private fun calculateTotalIncome(): Int {
        return if (rows * seats <= 60) {
            rows * seats * 10
        } else {
            val firstHalfRows = rows / 2
            (firstHalfRows * seats * 10) + ((rows - firstHalfRows) * seats * 8)
        }
    }
}
