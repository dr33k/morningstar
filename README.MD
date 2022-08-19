The application seen above is a Railway Registration and Ticketing System.
For the time being there are two roles which can be assumed by a user

PASSENGER (default assigned on registration)
ADMIN (assigned by another admin)

When in use please follow this order:

Create LOCATIONs first to be used by VOYAGEs(trips/journeys)

Create VOYAGEs next to be used by BOOKINGs(reservations)

Create BOOKINGs next to be used by USERs(passengers)

The first user in the hosted application:https://dashboard.heroku.com/apps/railway-spring-app
is John Doe with login credentials:


email:admin@safuser.com

password:software

John Doe has ADMIN privileges.
For each entity listed above, the 'status' field can be modified after creation.

A LOCATION may be deleted if it has not been used for any VOYAGES. If so, it can only be rendered 'INACTIVE' when not required

A VOYAGE may not be deleted

A BOOKING may be deleted but ONLY if it is 'CANCELLED' first (If the status is modified to 'CANCELLED')

On creation of a BOOKING a TICKET is created automatically for the user by the Postgres Database.

On 'CANCELLING' of a BOOKING, the TICKET is automatically deleted 


A LOCATION is identified by its 'stateCode' and 'stationNo'

The stateCode is the first 4 uppercase letters of any Nigerian State

The stationNo is a 2 digit number (01-99) signifying the number of a train station in that state

Dates of Birth should be Dates in the Past

Dates of Travel should be Dates in the Future

ENJOY!!!
