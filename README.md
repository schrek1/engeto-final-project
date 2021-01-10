# Bus Ticket Reservation

This is the final project for the Java Academy.

## Requirements
Create a GUI application in Java for making seat reservations in buses. Imagine a clerk sitting in the ticket office
at the public transportation terminal. Customers will want to reserve 1 or more seats on a bus going between two cities
at a specific time.

The clerk wants to be able to do the following:
- Make a new reservation if there is a seat available on a bus chosen by the customer.
  - Customer is allowed to choose specific seats.
  - Customer will choose specific day.
  - In case the customer does not want a specific seat, the application should assingn the first available one.
- Print the reservation for the customer.

The bus company also needs to be able to view and export all the reservations made.
- The application must let the user choose where s/he wants to save the exported data.
- Also in case the application is turned off, it should be able to load the reservations, so one seat is not sold twice. 

## Data Format
All the data (inputs and outputs) will be provided in CSV format. Columns will be separated by a semicolon (`;`).
String values will _not_ be quoted, and they will not contain the semicolon (`;`) character. The first line of the file
serves as headers and should be skipped.

The timetable is available in the `data/` directory. It specifies the time, start and destination city and the bus capacity.
The timetable is the same each day.

The file with exported reservations must contain the same data as the timetable, only the bus capacity is replaced with
list of reserved seats.

## Testing
Obviously the application logic needs to be tested. So start with some interfaces/classes, whcih implement the basic logic.
Also represent your data as classes with the appropriate fields. It will help you greatly. Internally use `InputStream`
and `OutputStream` for imports/exports. It will make the logic testable.