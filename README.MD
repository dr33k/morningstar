This application is a Registration and Ticketing System.
For the time being there are two roles which can be assumed by a user

PASSENGER (default assigned on registration)
ADMIN (assigned by another admin)

When in use please follow this order:

Create LOCATIONs first to be used by VOYAGEs(trips/journeys)

Create VOYAGEs next to be used by BOOKINGs(reservations)

Create BOOKINGs next to be used by USERs(passengers)

For each entity listed above, the 'status' field can be modified after creation.

A LOCATION may be deleted if it is UNUSUED. Else, it can only be disabled when not required. THis tries to make the database immutable

Only PENDING VOYAGEs may be deleted 

On creation of a BOOKING a TICKET is created automatically after payment.

A BOOKING may be deleted but ONLY if it is CANCELLED first

On deleting of a CANCELLED BOOKING, the TICKET is automatically deleted 

A LOCATION is identified by its 'stateCode' and 'stationNo'

The stateCode is the first 4 uppercase letters of any Nigerian State

The stationNo is a 2-digit number (01-99) signifying the number of a train station in that state

Dates of Birth should be Dates in the Past and dates of travel should be Dates in the Future

ENJOY
